from libs.neo4j import Neo4jConnection
from libs.fecha import generar_fecha_nacimiento, generar_fecha_registro
from libs.loader import Loader

from core.parallel_data_creation import ParallelDataCreation
from core.neo4j_loader import Neo4jLoader
from core.build_relationships import BuildRelationship
from libs.string_generator import StringGenerator

from tqdm import tqdm
from load.faker import fake
import pandas as pd
import random, gc
from libs.path import Path

unicos = set()


class SocialUserRepository:
    global unicos
    unicos = set()

    def __init__(self, total_user) -> None:
        self.TOTAL_SOCIAL_USER = total_user
        self.modulo_name = 'users'
        self.neo4j_loader = Neo4jLoader()
        self.parallel_process_data = ParallelDataCreation()
        self.build_relationship = BuildRelationship()

    # %% Create and Load the data of SocialUser
    def load_nodes(self):        
        # Variables
        global unicos
        columnas = ['dateBorn', 'registrationDate', 'fullName', 'userName', 'email', 'language', 'onVacation',
                    'isActive', "friendRequestCount"]
        descript = 'Creating Users'
        descript_lotes = 'Creating Batches of Users for the Social Network'

        __path__ = Path.get_module_path(self.modulo_name)

        def build(data):
            global unicos
            rango_idns = data["rango_idn"]
            # variables para el fichero
            file = data["file"]
            id_file = data["i"]
            # rango de los ids para los usuarios
            idn_begin = rango_idns[0]
            idn_end = rango_idns[1]
            data = []

            for _ in tqdm(range(idn_end - idn_begin + 1), desc=descript_lotes, leave=False):
                # aqui se ejecuta todo el codigo en rango
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
                # email = generar_direccion_email(user_name,fake)

                email = StringGenerator.generate_email(user_name)

                language = 'ES'
                onVacation = random.choice([True, False])
                isActive = random.choice([True, False])

                unicos.add(user_name)
                data.append([dateBorn, registrationDate, fullName, user_name, email, language, onVacation, isActive, 0])

            # Create the DataFrame
            df = pd.DataFrame(data, columns=columnas)
            # Save the file
            file_name = f"{__path__}/{id_file}.csv"
            df.to_csv(file_name, index=False)

            # Save the path to read from import folder
            file_name_to_read = f"{self.modulo_name}/{id_file}.csv"
            file.writelines(file_name_to_read + "\n")
            del data, df, file_name
            gc.collect()

        # Execute the function: [ build ] in parallel
        self.parallel_process_data.generate_data_in_ranges(total=self.TOTAL_SOCIAL_USER, task_func=build,
                                                      description=f'{descript}: {self.TOTAL_SOCIAL_USER:,}')

        # Save the Data to Neo4j
        conn = Neo4jConnection()

        def save(ruta):
            conn.query_insert("""LOAD CSV WITH HEADERS FROM "file:///{ruta}" AS row
                            CALL {{
                                WITH row
                                    MERGE (id:IdNumerico {{name:'SocialUser'}})
                                        ON CREATE SET id.total = 1
                                        ON MATCH SET id.total = id.total + 1
                                    WITH id.total AS nid, row
                                        MERGE (u:SocialUser {{identifier:randomUUID(),
                                            idn: nid,
                                            dateBorn : localdatetime(row.dateBorn),
                                            registrationDate : localdatetime(row.registrationDate),
                                            fullName : row.fullName,
                                            userName : row.userName,
                                            email : row.email,
                                            language : row.language,
                                            onVacation : row.onVacation,
                                            isActive : row.isActive,
                                            friendRequestCount : row.friendRequestCount
                                        }})
                            }} IN TRANSACTIONS OF 1000 ROWS
                                          """.format(ruta=ruta))

        self.neo4j_loader.load_nodes(task_func=save, description="Storing Users")
        conn.close()

    # %% Create and Load the Friends relationship
    def load_friends(self, friends_per_user_min, friends_per_user_max):
        print(self.TOTAL_SOCIAL_USER)
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
        
        from core.neo4j_load_relationships import Neo4jLoadRealationships
        save_relaciones = Neo4jLoadRealationships()
        save_relaciones.almacenar_relacion_con_prop_tiempo_en_relacion(
                            nodo_inicio = "SocialUser",
                            nodo_destino = "SocialUser",
                            nombre_relacion = "FRIEND_OF",
                            descript = "Storing: (SocialUser)-[:FRIEND_OF]->(SocialUser)",
                            nombre_prop_A = "registrationDate",
                            nombre_prop_B = "registrationDate",
                            nombre_prop_nueva = "friendshipDate")
        
        # Total de amigos que tiene el usuario en cuestion, private int friendCount;
        # loader = Loader("Calculating the Total Friends",
        #         "The total friend count has been assigned to the .friendCount property on the (:SocialUser) node").start()
        # conn = Neo4jConnection()
        # conn.query_insert("""
        #             CALL apoc.periodic.iterate(
        #                 "MATCH (u:SocialUser) RETURN u, [(u)-[:FRIEND_OF]-(amigo) | amigo] AS amigos",
        #                 "WITH u, amigos
        #                     SET u.friendCount = size(amigos)",
        #             {batchSize: 10000, parallel: false})""")
        # loader.stop()
        # conn.close()     
