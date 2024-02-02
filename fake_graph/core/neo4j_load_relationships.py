from libs.neo4j import Neo4jConnection
from core.neo4j_loader import Neo4jLoader

class Neo4jLoadRealationships:

    def __init__(self) -> None:
        self.neo4j_loader = Neo4jLoader()
        pass
    
    #%% Almacenar relaciones con propiedades de tiempo en [relacion], todo en lotes
    def almacenar_relacion_con_prop_tiempo_en_relacion(self,
                                            nodo_inicio, 
                                            nodo_destino, 
                                            nombre_relacion, 
                                            descript,
                                            nombre_prop_A,
                                            nombre_prop_B,
                                            nombre_prop_nueva,                                         
                                            direccion_o_d=True):
        # almacenar la relacion por bloques en paralelo, es decir los ficheros que tienen los bloques
        # en la funcion de guardar se colocara la funcion que crea la propiedad de tiempo al mismo tiempo
        relacion = (lambda: f"(o)-[r:{nombre_relacion}]->(d)" if direccion_o_d else f"(o)<-[r:{nombre_relacion}]-(d)")()
        
        conn = Neo4jConnection()

        # Save the Data to Neo4j
        def insertar(ruta):
            
            conn.query_insert("""
                CALL apoc.load.json("file:///{ruta}")
                    YIELD value AS row
                    CALL{{
                        with row                        
                        MATCH (o:{nodo_inicio} {{idn: row.nodo_origen_id}})
                        MATCH (d:{nodo_destino} {{idn: row.nodo_destino_id}})                    
                        MERGE {relacion}
                        WITH o, d, r                    
                        WITH r, CASE WHEN o.{nombre_prop_A} > d.{nombre_prop_B} THEN o.{nombre_prop_A} ELSE d.{nombre_prop_B} END AS baseMoment
                        WITH r, datetime.realtime() as now, datetime(baseMoment) as lowerBounds
                        WITH r, now, lowerBounds, duration.inSeconds(lowerBounds, now) as durationInSeconds
                        WITH r, now, lowerBounds, durationInSeconds, durationInSeconds.seconds / 2 as offset
                        WITH r, now, lowerBounds, durationInSeconds, offset, lowerBounds + duration({{seconds:offset}}) as inbetweenDate
                        set r.{nombre_prop_nueva} = localdatetime(inbetweenDate)
                    }}""".format(ruta=ruta,
                                nodo_inicio=nodo_inicio,                            
                                nodo_destino=nodo_destino, 
                                relacion=relacion,
                                nombre_prop_A = nombre_prop_A,
                                nombre_prop_B = nombre_prop_B,
                                nombre_prop_nueva = nombre_prop_nueva))        

        self.neo4j_loader.cargar_relaciones_a_neo4j(func=insertar, descript=descript)
        conn.close()