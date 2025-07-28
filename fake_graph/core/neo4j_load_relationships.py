from libs.neo4j import Neo4jConnection
from core.neo4j_loader import Neo4jLoader

class Neo4jLoadRealationships:

    def __init__(self) -> None:
        self.neo4j_loader = Neo4jLoader()
        
    def save_relationships(self, nodo_inicio, nodo_destino, nombre_relacion, descript, direccion_o_d=True):
        relacion = (lambda: f"(o)-[:{nombre_relacion}]->(d)" if direccion_o_d else f"(o)<-[:{nombre_relacion}]-(d)")()
        
        #relacion = (lambda: "(o)-[:{nombre_relacion}]->(d)" if direccion_o_d else "(o)<-[:{nombre_relacion}]-(d)")()
        
        conn = Neo4jConnection()
        # Save the Data to Neo4j
        def insertar(path):        
            conn.query_insert("""LOAD CSV WITH HEADERS FROM "file:///{path}" AS row
                CALL{{
                        WITH row
                            MATCH (o:{nodo_inicio} {{idn: toInteger(row.nodo_origen_id)}})
                            MATCH (d:{nodo_destino} {{idn: toInteger(row.nodo_destino_id)}})
                            MERGE {relacion}
                    }} IN TRANSACTIONS OF 1000 ROWS
                              """.format(path=path,
                                nodo_inicio=nodo_inicio, 
                                nodo_destino=nodo_destino,
                                relacion=relacion))
        
        self.neo4j_loader.load_relationships(task_func=insertar, description=descript)
        conn.close()