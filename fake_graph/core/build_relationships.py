from collections import defaultdict
import gc
import random

from libs.path import Path
from core.parallel_data_creation import ParallelDataCreation

import pandas as pd
from tqdm import tqdm


class BuildRelationship:

    def __init__(self):
        self.parallel_process_data = ParallelDataCreation()
        

    #%% Building la relacion de N a M donde: A - Usuarios tiene varios B - Usuarios como Seguidores     
    def crear_relaciones_NaM(self,
            A_TOTAL, 
            B_TOTAL, 
            rel_min, 
            rel_max, 
            descript, 
            descript_g, 
            descript_l, 
            modulo_name, 
            name_relation_file, 
            unicos=True):
        
        __path__ = Path.get_module_path(modulo_name)
        global unicos_de_duplas                
        unicos_de_duplas = defaultdict(set)
        
        # Crea el dataSet con los Valores
        def crear(data):        
            global unicos_de_duplas                
                    
            rango_idns = data["rango_idn"]
            file = data["file"]
            id_file = data["i"]
            idn_begin = rango_idns[0]
            idn_end = rango_idns[1]
            relaciones = set()
            
            for i in tqdm(range(idn_end-idn_begin+1),  desc=descript_g, leave=False):            
                idn_nodo_origen = idn_begin+i            
                total_relaciones_desde_nodo_origen = random.randint(rel_min, rel_max)            
                # Creamos todas las relaciones en cuestion para el nodo origen
                if total_relaciones_desde_nodo_origen != 0:
                    for _ in tqdm(range(total_relaciones_desde_nodo_origen),  desc=descript_l, leave=False):
                        # el while siempre será true a no ser que encontremos una dupla de unicos que no exista ya
                        if unicos:
                            while True:                    
                                idn_nodo_destino = random.randint(1, B_TOTAL)
                                if (idn_nodo_destino not in unicos_de_duplas[idn_nodo_origen]):
                                    break
                            # Agregamos la dupla a la lista de unicos_de_duplas
                            unicos_de_duplas[idn_nodo_origen].add(idn_nodo_destino)                
                            relaciones.add((idn_nodo_origen, idn_nodo_destino))
                        else:
                            idn_nodo_destino = random.randint(1, B_TOTAL)
                            relaciones.add((idn_nodo_origen, idn_nodo_destino))

            df = pd.DataFrame(relaciones, columns=['nodo_origen_id', 'nodo_destino_id'])
            # Guardar el fichero
            file_name = f"{__path__}/{name_relation_file}_{id_file}.json"    
            df.to_json(file_name,orient="records")
            file.writelines(file_name + "\n")
            del data, df, file_name
            gc.collect()

        # Ejecuta los metodos para crear las Relaciones en paralelo
        self.parallel_process_data.generate_data_in_ranges(
            total=A_TOTAL, 
            task_func=crear, 
            description=f"Building: {descript}"
        )