from libs.config import conn
from libs.const import TOTAL_SOCIAL_USER
from libs.procesar_en_lotes import cargar_nodos_a_neo4j, crear_nodos_en_paralelo
from libs.util import camino_al_modulo
from tqdm import tqdm
from libs.config import fake
import pandas as pd
from libs.fecha import generar_fecha_nacimiento, generar_fecha_registro
import random, re, gc

unicos = set()

class UserRepo:
    global unicos
    unicos = set()
    #%% Crea y Carga los Datos de los Usuarios de la Red Social
    def load_nodes(self):
        # Variables
        global unicos
        columnas = ['dateBorn','registrationDate','fullName','userName','email','language','onVacation','isActive',"friendRequestCount"]
        descript = 'Creating Users'
        descript_lotes = 'Creating Batches of Users for the Social Network'

        __path__ = camino_al_modulo(modulo_name='usuarios')

        def crear(data):
            global unicos
            rango_idns = data["rango_idn"]
            # variables para el fichero
            file = data["file"]
            id_file = data["i"]
            # rango de los ids para los usuarios
            idn_begin = rango_idns[0]
            idn_end = rango_idns[1]
            data = []

            for _ in tqdm(range(idn_end-idn_begin+1),  desc=descript_lotes, leave=False):
                # aqui se ejecuta todo el codigo en rango
                dateBorn = generar_fecha_nacimiento()
                registrationDate = generar_fecha_registro(dateBorn)
                # falta eliminar las tildes del nickname
                # validar unico
                fullName = ''
                userName = ''
                while True:
                    fullName = fake.name()
                    nombre_limpiado = re.sub(r'\W+', '', fullName.lower())
                    userName = nombre_limpiado[:6] + str(fake.random_number(digits=4))
                    if userName not in unicos:
                        break
                domain = fake.random_element(["gmail.com", "hotmail.com", "yahoo.com", "outlook.com"])
                email = f"{userName}@{domain}"

                language = 'ES'
                onVacation = random.choice([True, False])
                isActive = random.choice([True, False])
                
                unicos.add(userName)
                data.append([dateBorn,registrationDate,fullName,userName,email,language,onVacation,isActive,0])
                
            # Crear el DataFrame
            df = pd.DataFrame(data, columns=columnas)
            # Guardar el fichero
            file_name = f"{__path__}/user_{id_file}.csv"
            df.to_csv(file_name,index=False)
            file.writelines(file_name + "\n")
            del data, df, file_name
            gc.collect()

        # Creamos los nodos en paralelo
        crear_nodos_en_paralelo(total=TOTAL_SOCIAL_USER, func=crear, descript=descript+': '+format(TOTAL_SOCIAL_USER, ','))
        
        # Save the Data to Neo4j
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
                            }} IN TRANSACTIONS OF 500 ROWS
                                          """.format(ruta=ruta))

        cargar_nodos_a_neo4j(func=save, descript="Storing Users")