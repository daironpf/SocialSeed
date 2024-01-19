import concurrent.futures
from tqdm import tqdm
import os, time
from libs.util import lista_de_idn_begin

def crear_nodos_en_paralelo(total, func, descript):
    archivo = open("temp/lista.txt", "w")
    id_listas = lista_de_idn_begin(total)
    #print(f"lista: {id_listas}")
    max_workers = os.cpu_count() or 1
    # Procesar cada archivo en paralelo
    with concurrent.futures.ThreadPoolExecutor(max_workers) as executor:
        with tqdm(total=len(id_listas), desc=descript, leave=True) as pbar:
            # Utilizar executor.submit() para ejecutar la función func en paralelo para cada elemento
            futures = [
                executor.submit(func, data={"rango_idn": id_listas[i], 'i': i, 'file': archivo})
                for i in range(len(id_listas))
            ]
            # Esperar a que todos los futuros se completen
            for future in concurrent.futures.as_completed(futures):
                _ = future.result()
                pbar.update(1)
        # Actualizar el progreso total
        pbar.refresh()
    archivo.close()

def cargar_nodos_a_neo4j(func, descript):
    # Cargamos el fichero lista.txt
    with open("temp/lista.txt", "r") as file:
        filenames = file.read().splitlines()

        max_workers = os.cpu_count() or 1
        # Procesar cada archivo en paralelo con reintento
        with concurrent.futures.ThreadPoolExecutor(1) as executor:
            with tqdm(total=len(filenames), desc=descript, leave=True) as pbar:
                # Utilizar executor.submit() para ejecutar la función func en paralelo para cada nombre de archivo
                futures = [executor.submit(func, ruta=nombre_archivo) for nombre_archivo in filenames]

                # Esperar a que todos los futuros se completen con reintento
                for future in concurrent.futures.as_completed(futures):
                    while True:
                        try:
                            result = future.result()
                            break  # Si tiene éxito, salimos del bucle de reintento
                        except Exception as e:
                            print(f"Error: {str(e)}. Reintentando en 1 segundo...")
                            time.sleep(10)  # Esperar antes de reintentar
                    pbar.update(1)

            # Actualizar el progreso total
            pbar.refresh()

    # Cierra la conexión con la base de datos de Neo4j
    os.remove("temp/lista.txt")