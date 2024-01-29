import time

def begin():
    print("STARTING THE CREATION OF THE FAKE GRAPH")    
    return time.time()

def end(start_time):
    # Tomar el tiempo de finalización
    tiempo_fin = time.time()

    # Calcular la duración total en segundos
    duracion_total_segundos = tiempo_fin - start_time

    # Convertir la duración total a horas, minutos y segundos
    horas = int(duracion_total_segundos // 3600)
    minutos = int((duracion_total_segundos % 3600) // 60)
    segundos = int(duracion_total_segundos % 60)

    # Imprimir el tiempo total de ejecución
    print("THE CREATION OF THE FAKE GRAPH IS COMPLETED.")
    print(f"Total execution time: {horas:02d}h:{minutos:02d}m:{segundos:02d}s")