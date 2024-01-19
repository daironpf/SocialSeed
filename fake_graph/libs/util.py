import string, random, os
from itertools import cycle
from shutil import get_terminal_size
from threading import Thread
from time import sleep

def lotes(filas) -> int:    
    hilos = os.cpu_count() or 2        
    filas /= hilos
    if filas < hilos:
         return 1
    while filas >= 30000:
        filas /= hilos
    return int(filas)

def lista_de_idn_begin(A_TOTAL:int):
    lote=lotes(A_TOTAL)
    lista_de_idn_a = []
    id_salto = lote*-1
    for _ in range(int(A_TOTAL/lote)-1):
        id_salto = lote+id_salto+1
        # Parche para cuando el primer elemento del salto es igual al total
        if id_salto == A_TOTAL:
            elemento = [id_salto,id_salto]
            lista_de_idn_a.append(elemento)
            return lista_de_idn_a
        
        elemento = [id_salto,lote+id_salto]
        lista_de_idn_a.append(elemento)

        # Parche a algunas situaciones para numeros pequennos
        if lote+id_salto >= A_TOTAL:
            return lista_de_idn_a
                
    elemento = [lote+id_salto+1,A_TOTAL]
    lista_de_idn_a.append(elemento)
    return lista_de_idn_a
    
def generar_direccion_email(nickname,fake):    
    proveedores_correo = ['gmail.com', 'yahoo.com', 'hotmail.com', 'outlook.com']
    proveedor = fake.random_element(proveedores_correo)
    dominio = f'{nickname}@{proveedor}'
    return dominio

def generar_codigo_unico_str(rango):
    caracteres = string.ascii_letters + string.digits
    codigo = ''.join(random.choice(caracteres) for _ in range(rango))
    return codigo

def generar_codigo_unico_long(rango):
    caracteres = string.digits
    codigo = ''.join(random.choice(caracteres) for _ in range(rango))
    return codigo

def camino_al_modulo(modulo_name='name')->str:
    __path__ = os.path.realpath('temp/').replace("\\", "/")
    if not os.path.exists(f'{__path__}/{modulo_name}'):
        os.makedirs(f'{__path__}/{modulo_name}')
    return f'{__path__}/{modulo_name}'

class Loader:
    def __init__(self, desc="Loading...", end="Done!", timeout=0.1):
        """
        A loader-like context manager

        Args:
            desc (str, optional): The loader's description. Defaults to "Loading...".
            end (str, optional): Final print. Defaults to "Done!".
            timeout (float, optional): Sleep time between prints. Defaults to 0.1.
        """
        self.desc = desc
        self.end = end
        self.timeout = timeout

        self._thread = Thread(target=self._animate, daemon=True)
        self.steps = ["⢿", "⣻", "⣽", "⣾", "⣷", "⣯", "⣟", "⡿"]
        self.done = False

    def start(self):
        self._thread.start()
        return self

    def _animate(self):
        for c in cycle(self.steps):
            if self.done:
                break
            print(f"\r{self.desc} {c}", flush=True, end="")
            sleep(self.timeout)

    def __enter__(self):
        self.start()

    def stop(self):
        self.done = True
        cols = get_terminal_size((80, 20)).columns
        print("\r" + " " * cols, end="", flush=True)
        print(f"\r{self.end}", flush=True)

    def __exit__(self, exc_type, exc_value, tb):
        # handle exceptions with those variables ^
        self.stop()