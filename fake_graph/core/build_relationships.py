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
            # Save the file
            file_name = f"{__path__}/{name_relation_file}_{id_file}.csv"
            df.to_csv(file_name, index=False)

            # Save the path to read from import folder
            file_name_to_read = f"{modulo_name}/{name_relation_file}_{id_file}.csv"
            file.writelines(file_name_to_read + "\n")
            del data, df, file_name
            gc.collect()

        # Ejecuta los metodos para crear las Relaciones en paralelo
        self.parallel_process_data.generate_data_in_ranges(
            total=A_TOTAL, 
            task_func=crear, 
            description=f"Building: {descript}"
        )


    #%% Building relacion de N a 1 donde: A - Review solo puede estar relacionada a un B, pero B puede tener varios A
    def crear_relaciones_Na1(self, A_TOTAL, B_TOTAL, descript, descript_g, modulo_name, name_relation_file):
        __path__ = Path.get_module_path(modulo_name)
        
        # Crea el dataSet con los Valores
        def crear(data):                
            rango_idns = data["rango_idn"]
            file = data["file"]
            id_file = data["i"]        
            idn_begin = rango_idns[0]
            idn_end = rango_idns[1]
            relaciones = set()
            
            for i in tqdm(range(idn_end-idn_begin+1),  desc=descript_g, leave=False):
                # Creo la relacion de A a B siendo un B un id int random en rango de 1 a total_b
                idn_nodo_destino = random.randint(1, B_TOTAL)
                relaciones.add((idn_begin+i, idn_nodo_destino))
            
            df = pd.DataFrame(relaciones, columns=['nodo_origen_id', 'nodo_destino_id'])
            # Save the file
            file_name = f"{__path__}/{name_relation_file}_{id_file}.csv"
            df.to_csv(file_name, index=False)

            # Save the path to read from import folder
            file_name_to_read = f"{modulo_name}/{name_relation_file}_{id_file}.csv"
            file.writelines(file_name_to_read + "\n")
            del data, df, file_name
            gc.collect()

        # Ejecuta los metodos para crear las Relaciones en paralelo    
        # crear_relaciones_en_paralelo(total=A_TOTAL, func=crear, descript=f"Building: {descript}")

        # Ejecuta los metodos para crear las Relaciones en paralelo
        self.parallel_process_data.generate_data_in_ranges(
            total=A_TOTAL, 
            task_func=crear, 
            description=f"Building: {descript}"
        )
