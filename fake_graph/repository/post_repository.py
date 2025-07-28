"""
A repository class for managing Post data and relationships in a social network.

Author: Dairon Pérez Frías
GitHub: https://github.com/daironpf
License: Apache License 2.0 (https://www.apache.org/licenses/LICENSE-2.0)
"""
# Import custom modules and classes
from libs.neo4j import Neo4jConnection
from libs.fecha import generar_fecha_base
from libs.loader import Loader
from libs.path import Path
from load.configuration import config

from core.parallel_data_creation import ParallelDataCreation
from core.neo4j_loader import Neo4jLoader
from core.build_relationships import BuildRelationship
from core.neo4j_load_relationships_with_time import Neo4jLoadRealationshipsWithTime

# Import necessary libraries and modules
from tqdm import tqdm
from load.faker import fake
import pandas as pd
import random, gc

class PostRepository:

    # Initialize the SocialUserRepository class
    def __init__(self, total_post) -> None:
        # Initialize instance variables
        self.TOTAL_POST = total_post
        self.modulo_name = 'post'
        self.neo4j_loader = Neo4jLoader()
        self.parallel_process_data = ParallelDataCreation()
        self.build_relationship = BuildRelationship()
        self.save_relations_with_time = Neo4jLoadRealationshipsWithTime()
        
    def load_nodes(self, 
                    hashtag_per_post_min,
                    hashtag_per_post_max,
                    words_per_post_min,
                    words_per_post_max):
        #Variables
        columnas = ['idn','content','updateDate','imageUrl','isActive','hashtags']
        base_url = 'https://socialseed.com/sdn/images/'
        
        descript='Creating Post'
        descript_lotes='Creating Batches of Posts'

        # Get the path to the user module
        __path__ = Path.get_module_path(self.modulo_name)

        def generate_content_with_hashtags(hashtags) -> str:
            # Generates a list of words with a range established in the configuration file
            num_words = random.randint(words_per_post_min, words_per_post_max)
            content_words = fake.words(nb=num_words)
            
            # Mix the hashtags with the words of the content
            mixed_content = content_words + ['#' + hashtag for hashtag in hashtags]
            # Shuffle the list
            random.shuffle(mixed_content)
            
            # Join the words back into a single String
            return ' '.join(mixed_content)
        
        def generate_hashtags():           
            # generate fake hashtags in range established in config file
            return [fake.word() for _ in range(random.randint(hashtag_per_post_min, hashtag_per_post_max))]
        
        def build_string_join_elements_by_point(array):
            # Use the join method of the string class to join the elements of the array with a dot
            return ".".join(array)
        
        def build(data):
            """
            Generates post data in a specified range and saves it to a CSV file.

            Args:
                data (dict): Dictionary containing data for generating posts.
            """
            # Extract data from the input dictionary
            rango_idns = data["rango_idn"]            
            file = data["file"]
            id_file = data["i"]            
            idn_begin = rango_idns[0]
            idn_end = rango_idns[1]
            data = []

            # Generate user data in the specified range
            for i in tqdm(range(idn_end - idn_begin+1),  desc=descript_lotes, leave=False):                
                idn = idn_begin + i
                hashtags = generate_hashtags()                
                content  = generate_content_with_hashtags(hashtags)
                updateDate = generar_fecha_base(annobase=1990,annoend=2021)

                image_name = fake.word() + '.jpg'
                imageUrl = f'{base_url}{image_name}'

                isActive = random.choice([True, False])

                list_of_hashtag = build_string_join_elements_by_point(hashtags)
                data.append([idn,content,updateDate,imageUrl,isActive,list_of_hashtag])

            # Create a DataFrame and save it to a CSV file
            df = pd.DataFrame(data, columns=columnas)
            file_name = f"{__path__}/{id_file}.csv"
            df.to_csv(file_name, index=False)
            
            # Save the file path for import
            file_name_to_read = f"{self.modulo_name}/{id_file}.csv"
            file.writelines(file_name_to_read + "\n")
            del data, df, file_name
            gc.collect()
                
        # Generate post data in parallel
        self.parallel_process_data.generate_data_in_ranges(
            total=self.TOTAL_POST, 
            task_func=build,
            description=f'{descript}: {self.TOTAL_POST:,}'
            )
        
        # Save the Data to Neo4j
        conn = Neo4jConnection()

        def save(path):
            """
            Save post data and its related HashTag from CSV files to Neo4j.

            Args:
                path (str): Path to the CSV file.
            """
            conn.query_insert("""LOAD CSV WITH HEADERS FROM "file:///{path}" AS row
                            CALL {{
                                WITH row
                                MERGE (p:Post {{identifier:randomUUID(),
                                    idn: toInteger(row.idn),
                              
                                    content: row.content,
                                    imageUrl: row.imageUrl,
                              
                                    updateDate : localdatetime(row.updateDate),
                              
                                    isActive : toBoolean(row.isActive)
                                }})
                                WITH row, p, split(row.hashtags, ".") AS valores
                                FOREACH (val IN valores |
                                    MERGE (h:HashTag {{name: val}})
                                        ON CREATE SET h.postTaggedIn = 1, h.identifier = randomUUID()
                                        ON MATCH SET h.postTaggedIn = h.postTaggedIn + 1
                                    MERGE (p)-[:TAGGED_WITH]->(h)
                                ) 
                            }} IN TRANSACTIONS OF 1000 ROWS
                                          """.format(path=path))

        self.neo4j_loader.load_nodes(task_func=save, description="Storing Posts")

        # Create numeric index for HashTag Nodes
        loader = Loader("Creating numerical index for HashTags",
                "The numerical index has been assigned to the .idn property on the (:HashTag) node").start()
        conn.query_insert("""
                          CALL {
                            // return HashTag in order by identifier
                            MATCH (ht:HashTag)
                            WITH ht
                            ORDER BY ht.identifier
                            // assigned idn to each node
                            WITH COLLECT(ht) AS batch                            
                            UNWIND RANGE(0, SIZE(batch)-1) AS i
                            WITH batch[i] AS ht, i + 1 AS newIndex
                            SET ht.idn = newIndex
                          } IN TRANSACTIONS OF 1000 ROWS
                        """)
        loader.stop()
        conn.close()