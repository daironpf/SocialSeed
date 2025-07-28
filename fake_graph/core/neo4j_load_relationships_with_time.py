from libs.neo4j import Neo4jConnection
from core.neo4j_loader import Neo4jLoader

class Neo4jLoadRealationshipsWithTime:

    def __init__(self) -> None:
        self.neo4j_loader = Neo4jLoader()
            
    #%% Almacenar relaciones con propiedades de tiempo en [relacion]
    def store_relation_with_time_property(self,
                                            nodo_inicio, 
                                            nodo_destino, 
                                            nombre_relacion, 
                                            descript,
                                            nombre_prop_A,
                                            nombre_prop_B,
                                            nombre_prop_nueva,                                         
                                            direccion_o_d=True):
        
        # se construye la relacion con la direccion especificada teniendo a [ o ] como nodo origen y a [ d ] como nodo destino
        # si direccion_o_d es verdadero entonces la direccion del a relacion será (o)-[:NOMBRE_RELACION]->(d)
        relacion = (lambda: f"(o)-[r:{nombre_relacion}]->(d)" if direccion_o_d else f"(o)<-[r:{nombre_relacion}]-(d)")()
        
        conn = Neo4jConnection()
        # Save the Data to Neo4j
        def insertar(path):
            conn.query_insert("""LOAD CSV WITH HEADERS FROM "file:///{path}" AS row
                    CALL{{
                        WITH row
                            MATCH (o:{nodo_inicio} {{idn: toInteger(row.nodo_origen_id)}})
                            MATCH (d:{nodo_destino} {{idn: toInteger(row.nodo_destino_id)}})                    
                            MERGE {relacion}
                            WITH o, d, r                    
                                WITH r, CASE WHEN o.{nombre_prop_A} > d.{nombre_prop_B} THEN o.{nombre_prop_A} ELSE d.{nombre_prop_B} END AS baseMoment
                                WITH r, datetime.realtime() as now, datetime(baseMoment) as lowerBounds
                                WITH r, now, lowerBounds, duration.inSeconds(lowerBounds, now) as durationInSeconds
                                WITH r, now, lowerBounds, durationInSeconds, durationInSeconds.seconds / 2 as offset
                                WITH r, now, lowerBounds, durationInSeconds, offset, lowerBounds + duration({{seconds:offset}}) as inbetweenDate
                                    set r.{nombre_prop_nueva} = localdatetime(inbetweenDate)
                    }} IN TRANSACTIONS OF 1000 ROWS
                            """.format(path=path,
                                nodo_inicio=nodo_inicio,                            
                                nodo_destino=nodo_destino, 
                                relacion=relacion,
                                nombre_prop_A = nombre_prop_A,
                                nombre_prop_B = nombre_prop_B,
                                nombre_prop_nueva = nombre_prop_nueva))                        
        
        self.neo4j_loader.load_relationships(task_func=insertar, description=descript)
        conn.close()