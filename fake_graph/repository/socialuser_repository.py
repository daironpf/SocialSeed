"""
A repository class for managing SocialUser data and relationships in a social network.

Author: Dairon Pérez Frías
GitHub: https://github.com/daironpf
License: Apache License 2.0 (https://www.apache.org/licenses/LICENSE-2.0)
"""
# Import custom modules and classes
from libs.neo4j import Neo4jConnection
from libs.fecha import generar_fecha_nacimiento, generar_fecha_registro
from libs.loader import Loader
from libs.string_generator import StringGenerator
from libs.path import Path

from core.parallel_data_creation import ParallelDataCreation
from core.neo4j_loader import Neo4jLoader
from core.build_relationships import BuildRelationship
from core.neo4j_load_relationships_with_time import Neo4jLoadRealationshipsWithTime
from core.neo4j_load_relationships import Neo4jLoadRealationships

# Import necessary libraries and modules
from tqdm import tqdm
from load.faker import fake
import pandas as pd
import random, gc

# Define a set to store unique usernames
unicos = set()

class SocialUserRepository:
    """
    A repository class for managing SocialUser data and relationships in a social network.

    Attributes:
        TOTAL_SOCIAL_USER (int): Total number of SocialUser instances to be created.
        modulo_name (str): Name of the module to store user data.
        neo4j_loader (Neo4jLoader): Instance of Neo4jLoader class for loading data into Neo4j.
        parallel_process_data (ParallelDataCreation): Instance of ParallelDataCreation class for parallel data processing.
        build_relationship (BuildRelationship): Instance of BuildRelationship class for building relationships between users.
        save_relations_with_time (Neo4jLoadRealationshipsWithTime): Instance of Neo4jLoadRealationshipsWithTime class for saving relationships with time properties.
    """
    global unicos
    unicos = set()

    # Initialize the SocialUserRepository class
    def __init__(self, total_user) -> None:
        # Initialize instance variables
        self.TOTAL_SOCIAL_USER = total_user
        self.modulo_name = 'users'
        self.neo4j_loader = Neo4jLoader()
        self.parallel_process_data = ParallelDataCreation()
        self.build_relationship = BuildRelationship()
        self.save_relations_with_time = Neo4jLoadRealationshipsWithTime()
        self.save_relations = Neo4jLoadRealationships()
    
    def load_nodes(self):
        """
        Creates and loads SocialUser data.

        This method generates user data in batches, saves it to CSV files, and loads it into the Neo4j database.
        """        
        # Define column names for user data
        columnas = ['idn','dateBorn', 'registrationDate', 'fullName', 'userName', 'email', 'language', 'onVacation',
                    'isActive', "friendRequestCount"]
        
        global unicos
        descript = 'Creating Users'
        descript_lotes = 'Creating Batches of Users for the Social Network'

         # Get the path to the user module
        __path__ = Path.get_module_path(self.modulo_name)

        # Define a function to create user data
        def build(data):
            """
            Generates user data in a specified range and saves it to a CSV file.

            Args:
                data (dict): Dictionary containing data for generating users.
            """
            global unicos
            # Extract data from the input dictionary
            rango_idns = data["rango_idn"]            
            file = data["file"]
            id_file = data["i"]            
            idn_begin = rango_idns[0]
            idn_end = rango_idns[1]
            data = []

            # Generate user data in the specified range
            for i in tqdm(range(idn_end - idn_begin + 1), desc=descript_lotes, leave=False):
                idn = idn_begin + i
                dateBorn = generar_fecha_nacimiento()
                registrationDate = generar_fecha_registro(dateBorn)
                # falta eliminar las tildes del nickname

                # validar unico
                fullName = ''
                user_name = ''
                while True:
                    fullName = fake.name()
                    user_name = StringGenerator.generate_username(fullName)
                    if user_name not in unicos:
                        break

                email = StringGenerator.generate_email(user_name)
                language = 'ES'
                onVacation = random.choice([True, False])
                isActive = random.choice([True, False])

                unicos.add(user_name)
                data.append([idn, dateBorn, registrationDate, fullName, user_name, email, language, onVacation, isActive, 0])

            # Create a DataFrame and save it to a CSV file
            df = pd.DataFrame(data, columns=columnas)            
            file_name = f"{__path__}/{id_file}.csv"
            df.to_csv(file_name, index=False)

            # Save the file path for import
            file_name_to_read = f"{self.modulo_name}/{id_file}.csv"
            file.writelines(file_name_to_read + "\n")
            del data, df, file_name
            gc.collect()

        # Generate user data in parallel
        self.parallel_process_data.generate_data_in_ranges(
            total=self.TOTAL_SOCIAL_USER, 
            task_func=build,
            description=f'{descript}: {self.TOTAL_SOCIAL_USER:,}'
            )

        # Save the Data to Neo4j
        conn = Neo4jConnection()
        def save(path):
            """
            Saves user data from CSV files to Neo4j.

            Args:
                path (str): Path to the CSV file.
            """
            conn.query_insert("""LOAD CSV WITH HEADERS FROM "file:///{path}" AS row
                            CALL {{
                                WITH row                                    
                                MERGE (u:SocialUser {{identifier:randomUUID(),
                                    idn: toInteger(row.idn),
                              
                                    dateBorn : localdatetime(row.dateBorn),
                                    registrationDate : localdatetime(row.registrationDate),
                              
                                    fullName : row.fullName,
                                    userName : row.userName,
                                    email : row.email,
                                    language : row.language,
                              
                                    onVacation : toBoolean(row.onVacation),
                                    isActive : toBoolean(row.isActive),
                                    isDeleted : toBoolean("false"),
                              
                                    friendRequestCount : toInteger(row.friendRequestCount)
                                }})
                            }} IN TRANSACTIONS OF 1000 ROWS
                                          """.format(path=path))

        self.neo4j_loader.load_nodes(task_func=save, description="Storing Users")
        conn.close()

    # Create and load Friends relationship
    def load_friends(self, friends_per_user_min, friends_per_user_max):
        """
        Creates and loads friend relationships between users.

        Args:
            friends_per_user_min (int): Minimum number of friends per user.
            friends_per_user_max (int): Maximum number of friends per user.
        """
        # Create and load Friend relationships
        self.build_relationship.crear_relaciones_NaM(
                A_TOTAL = self.TOTAL_SOCIAL_USER,
                B_TOTAL = self.TOTAL_SOCIAL_USER,
                rel_min = friends_per_user_min,
                rel_max = friends_per_user_max,
                descript =  "(SocialUser)-[:FRIEND_OF]->(SocialUser)",
                descript_g = "Creating Batches of Friends",
                descript_l = "Creating Friends",
                modulo_name = self.modulo_name,
                name_relation_file = "amigos"
                )
        
        # Store relationships with time property
        self.save_relations_with_time.store_relation_with_time_property(
                            nodo_inicio = "SocialUser",
                            nodo_destino = "SocialUser",
                            nombre_relacion = "FRIEND_OF",
                            descript = "Storing: (SocialUser)-[:FRIEND_OF]->(SocialUser)",
                            nombre_prop_A = "registrationDate",
                            nombre_prop_B = "registrationDate",
                            nombre_prop_nueva = "friendshipDate")
        
        # Calculate total friends count for each user
        loader = Loader("Calculating the Total Friends",
                "The total friend count has been assigned to the .friendCount property on the (:SocialUser) node").start()
        conn = Neo4jConnection()
        conn.query_insert("""
                          CALL {
                            MATCH (u:SocialUser)
                            OPTIONAL MATCH (u)-[:FRIEND_OF]-(amigo)
                            WITH u, COLLECT(amigo) AS amigos
                            SET u.friendCount = size(amigos)
                          } IN TRANSACTIONS OF 1000 ROWS
                        """)
        conn.close()
        loader.stop()
        
    # Create and load the Follows relationship
    def load_followers(self, follow_per_user_min, follow_per_user_max):
        """
        Creates and loads follower relationships between users.

        Args:
            follow_per_user_min (int): Minimum number of follows per user.
            follow_per_user_max (int): Maximum number of follows per user.
        """
        # Create Follows relationships
        self.build_relationship.crear_relaciones_NaM(
                        A_TOTAL = self.TOTAL_SOCIAL_USER,
                        B_TOTAL = self.TOTAL_SOCIAL_USER,
                        rel_min = follow_per_user_min,
                        rel_max = follow_per_user_max,
                        descript = "(SocialUser)-[:FOLLOWED_BY]->(SocialUser)",
                        descript_g = "Creating Batches of Followers",
                        descript_l = "Creating Followers",
                        modulo_name = self.modulo_name,
                        name_relation_file = "seguidores")
        
        # Store relationships with time property
        self.save_relations_with_time.store_relation_with_time_property(
                            nodo_inicio = "SocialUser",
                            nodo_destino = "SocialUser",
                            nombre_relacion = "FOLLOWED_BY",
                            descript = "Storing: (SocialUser)-[:FOLLOWED_BY]->(SocialUser)",
                            nombre_prop_A = "registrationDate",
                            nombre_prop_B = "registrationDate",
                            nombre_prop_nueva = "followDate")
            
        # Calculate total following count for each user
        conn = Neo4jConnection()

        loader = Loader("Calculating Followed Users",
                "The total number of followed users has been stored in the .followingCount property of the (:SocialUser) node").start()
        conn.query_insert("""
                        CALL {
                            MATCH (u:SocialUser)
                            OPTIONAL MATCH (u)-[:FOLLOWED_BY]->(following)
                            WITH u, COLLECT(following) AS followsCount
                            SET u.followingCount = size(followsCount)
                          } IN TRANSACTIONS OF 5000 ROWS
                        """)
        loader.stop()

        # Calculate total followers count for each user
        loader = Loader("Calculating Followers",
                "The total number of followers has been stored in the .followersCount property of the (:SocialUser) node").start()
        conn.query_insert("""
                    CALL {
                            MATCH (u:SocialUser)
                            OPTIONAL MATCH (u)<-[:FOLLOWED_BY]-(follower)
                            WITH u, COUNT(follower) AS followersCount
                            SET u.followersCount = toInteger(followersCount)
                          } IN TRANSACTIONS OF 5000 ROWS
                    """)
        loader.stop()

        conn.close()

    # Build and Save: (Post)-[:POSTED_BY]->(SocialUser)
    def load_posted_post(self, TOTAL_POST):
        self.build_relationship.crear_relaciones_Na1(
                        A_TOTAL = TOTAL_POST,
                        B_TOTAL = self.TOTAL_SOCIAL_USER,
                        descript =  "(Post)-[:POSTED_BY]->(SocialUser)",                        
                        descript_g = "Creating Batches of Posts Published by Users",
                        modulo_name = self.modulo_name,
                        name_relation_file = "posted_Post")
        
        # save the relationship
        self.save_relations.save_relationships(nodo_inicio="Post",
                            nodo_destino="SocialUser",
                            nombre_relacion="POSTED_BY",
                            descript = "Storing: (Post)-[:POSTED_BY]->(SocialUser)",
                            direccion_o_d=True)
        
        # Assigning a valid time for the Post publication
        conn = Neo4jConnection()
        loader = Loader("Assigning a valid time for the Post publication",
                "The exact moment when the Post was posted has been recorded in the property: postDate of the relationship: POSTED_BY").start()        
        conn.query_insert("""
                          CALL {
                            MATCH (t:Post)-[r:POSTED_BY]->(o:SocialUser)
                            WITH r, datetime.realtime() as now, datetime(o.registrationDate) as lowerBounds
                                    WITH r, now, lowerBounds, duration.inSeconds(lowerBounds, now) as durationInSeconds
                                    WITH r, now, lowerBounds, durationInSeconds, durationInSeconds.seconds / 2 as offset
                                    WITH r, now, lowerBounds, durationInSeconds, offset, lowerBounds + duration({seconds:offset}) as inbetweenDate
                                    set r.postDate = localdatetime(inbetweenDate)
                            } IN TRANSACTIONS OF 1000 ROWS
                          """)
        loader.stop()

        # Assigning a valid update time for the Post, based on the publication moment
        loader = Loader("Assigning a valid update time for the Post, based on the publication moment",
                        "The exact moment of the last update has been recorded in the property: updateDate of the Post").start()
        
        conn.query_insert("""
                          CALL {
                            MATCH (p:Post)-[r:POSTED_BY]->(u)                            
                            WITH p, r, datetime.realtime() as now, datetime(r.postDate) as lowerBounds
                                    WITH p, now, lowerBounds, duration.inSeconds(lowerBounds, now) as durationInSeconds
                                    WITH p, now, lowerBounds, durationInSeconds, durationInSeconds.seconds / 2 as offset
                                    WITH p, now, lowerBounds, durationInSeconds, offset, lowerBounds + duration({seconds:offset}) as inbetweenDate
                                    set p.updateDate = localdatetime(inbetweenDate)
                            } IN TRANSACTIONS OF 1000 ROWS
                          """)
        conn.close()
        loader.stop()
        
    # Build and Save: (Post)<-[:LIKE]-(SocialUser)
    def load_liked_posts(self, TOTAL_POST, post_liked_per_user_min, post_liked_per_user_max):
        self.build_relationship.crear_relaciones_NaM(
                A_TOTAL = self.TOTAL_SOCIAL_USER,
                B_TOTAL = TOTAL_POST,
                rel_min = post_liked_per_user_min,
                rel_max = post_liked_per_user_max,
                descript =  "(SocialUser)-[:LIKE]->(Post)",
                descript_g = "Creating Batches of User Reactions to Posts",
                descript_l = "Creating LIKE",                        
                modulo_name = self.modulo_name,
                name_relation_file = "likes")

        # save the relationship
        self.save_relations.save_relationships(nodo_inicio="SocialUser",
                            nodo_destino="Post",
                            nombre_relacion="LIKE",
                            descript="Storing: (SocialUser)-[:LIKE]->(Post)")
        
        # Assigning a valid time for the LIKE relationship
        conn = Neo4jConnection()
        loader = Loader("Assigning a valid time for when the user gave a LIKE to the Post",
                        "The exact moment when the user reacted to the Post has been recorded in the property: likeDate of the relationship: LIKE").start()
        conn.query_insert("""
                          CALL {
                            MATCH (p:Post)<-[r:LIKE]-(u)
                            MATCH (p)-[rp:POSTED_BY]->(us)                            
                            WITH r, datetime.realtime() as now, datetime(rp.postDate) as lowerBounds
                                    WITH r, now, lowerBounds, duration.inSeconds(lowerBounds, now) as durationInSeconds
                                    WITH r, now, lowerBounds, durationInSeconds, durationInSeconds.seconds / 2 as offset
                                    WITH r, now, lowerBounds, durationInSeconds, offset, lowerBounds + duration({seconds:offset}) as inbetweenDate
                                    set r.likeDate = localdatetime(inbetweenDate)
                            } IN TRANSACTIONS OF 1000 ROWS
                          """)        
        conn.close()
        loader.stop()

    # Build and Save: (HashTag)<-[:INTERESTED_IN_HASHTAG]-(SocialUser)
    def load_interested_in_hashtag(self, TOTAL_HASHTAG, interest_hashtag_per_user_min, interest_hashtag_per_user_max):
        self.build_relationship.crear_relaciones_NaM(
                A_TOTAL = self.TOTAL_SOCIAL_USER,
                B_TOTAL = TOTAL_HASHTAG,
                rel_min = interest_hashtag_per_user_min,
                rel_max = interest_hashtag_per_user_max,
                descript =  "(SocialUser)-[:INTERESTED_IN_HASHTAG]->(HashTag)",
                descript_g = "Creating Batches of User interested in HashTag",
                descript_l = "Creating INTERESTED_IN_HASHTAG",
                modulo_name = self.modulo_name,
                name_relation_file = "interest_in_hashtag")

        # save the relationship
        self.save_relations.save_relationships(
                nodo_inicio="SocialUser",
                nodo_destino="HashTag",
                nombre_relacion="INTERESTED_IN_HASHTAG",
                descript="Storing: (SocialUser)-[:INTERESTED_IN_HASHTAG]->(HashTag)")
        
        # Assigning a valid time for the INTERESTED_IN_HASHTAG relationship
        conn = Neo4jConnection()
        loader = Loader("Assigning a valid time for when the user interested to the HashTag",
                        "The exact moment when the user interested to the HashTag has been recorded in the property: interestDate of the relationship: INTERESTED_IN_HASHTAG").start()
        conn.query_insert("""
                          CALL {
                            MATCH (h:HashTag)<-[r:INTERESTED_IN_HASHTAG]-(u:SocialUser)
                            WITH r, datetime.realtime() as now, datetime(u.registrationDate) as lowerBounds
                                    WITH r, now, lowerBounds, duration.inSeconds(lowerBounds, now) as durationInSeconds
                                    WITH r, now, lowerBounds, durationInSeconds, durationInSeconds.seconds / 2 as offset
                                    WITH r, now, lowerBounds, durationInSeconds, offset, lowerBounds + duration({seconds:offset}) as inbetweenDate
                                    set r.interestDate = localdatetime(inbetweenDate)
                            } IN TRANSACTIONS OF 1000 ROWS
                          """)
        loader.stop()

        loader = Loader("Calculates the total number of interested users for each HashTag",
                        "Store the total number of interested users for each HashTag in the property: socialUserInterestIn of the HashTag node").start()
        conn.query_insert("""
                          CALL {
                            MATCH (h:HashTag)<-[r:INTERESTED_IN_HASHTAG]-(u:SocialUser)
                            WITH h, COUNT(u) AS total
                            SET h.socialUserInterestIn = total
                            } IN TRANSACTIONS OF 1000 ROWS
                          """)
        conn.close()
        loader.stop()