print("STARTING THE CREATION OF THE FAKE GRAPH")
import time
from libs.config import conn

tiempo_inicio = time.time()
 
def final():
    # Tomar el tiempo de finalización
    tiempo_fin = time.time()

    # Calcular la duración total en segundos
    duracion_total_segundos = tiempo_fin - tiempo_inicio

    # Convertir la duración total a horas, minutos y segundos
    horas = int(duracion_total_segundos // 3600)
    minutos = int((duracion_total_segundos % 3600) // 60)
    segundos = int(duracion_total_segundos % 60)

    # Imprimir el tiempo total de ejecución
    conn.close()
    print("THE CREATION OF THE FAKE GRAPH IS COMPLETED.")
    print(f"Total execution time: {horas:02d}h:{minutos:02d}m:{segundos:02d}s")