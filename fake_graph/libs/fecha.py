from faker import Faker
from datetime import datetime, timedelta
import random

fake = Faker('es_ES')

def generar_fecha_nacimiento():
    start_date = datetime(1970, 1, 1)
    end_date = datetime(2003, 12, 31)

    # Calcula la diferencia en días entre las fechas de inicio y fin
    days_diff = (end_date - start_date).days

    # Genera un número aleatorio de días dentro del rango establecido
    random_days = random.randint(0, days_diff)

    # Crea una fecha de nacimiento aleatoria sumando los días aleatorios al inicio
    fecha_nacimiento = start_date + timedelta(days=random_days)

    # Formatea la fecha de nacimiento en el formato deseado
    formatted_fecha_nacimiento = fecha_nacimiento.strftime("%Y-%m-%d")

    return formatted_fecha_nacimiento


def generar_fecha_registro(fecha_nacimiento):
    fecha_nacimiento_dt = datetime.strptime(fecha_nacimiento, "%Y-%m-%d")

    # Suma 16 años a la fecha de nacimiento
    fecha_inicial = fecha_nacimiento_dt + timedelta(days=16*365)

    # Resta un año a la fecha actual
    fecha_actual = datetime.now()
    fecha_final = fecha_actual - timedelta(days=2*368)

    # Genera una fecha de registro aleatoria dentro del rango
    diferencia = fecha_final - fecha_inicial
    segundos_totales = diferencia.total_seconds()
    segundos_aleatorios = random.random() * segundos_totales
    fecha_registro = fecha_inicial + timedelta(seconds=segundos_aleatorios)

    return fecha_registro.strftime("%Y-%m-%dT%H:%M:%S.%f")

def generar_fecha_base(annobase,annoend):
    start_date = datetime(annobase, 1, 1)
    end_date = datetime(annoend, 12, 31)

    # Calcula la diferencia en días entre las fechas de inicio y fin
    days_diff = (end_date - start_date).days

    # Genera un número aleatorio de días dentro del rango establecido
    random_days = random.randint(0, days_diff)

    # Crea una fecha aleatoria sumando los días aleatorios al inicio
    fecha = start_date + timedelta(days=random_days)

    # Formatea la fecha a "2023-06-08T17:18:41.873096984" LocalDateTime de Java
    return fecha.strftime("%Y-%m-%dT%H:%M:%S.%f")