# Leer y Seguir los pasos para no tener problemas en medio de la importación
1. Copiar todo lo que se encuentra en esta carpeta para la carpeta **import**

2. Ejecutar los siguientes comandos en la consola:
* `python --version` verifica que tengas la version 10.0.10 o mayor
   
* `python -m pip install --upgrade pip` actualiza el gestor de paquetes de python
* `pip install neo4j pandas tqdm faker` instala todas las dependencias a utilizar

3. Abrir el fichero **configuration.conf** y ajustar los valores a lo deseado y luego guardar los cambios.
4. Ejecutar el fichero generador con el siguiente comando:
    `python generate.py`

> Ahora solo te queda esperar con calma en dependencia de la cantidad y las proporciones que especificaste

crear el fichero: apoc.conf
y colocarle estos datos:
apoc.import.file.enabled=true
apoc.import.file.use_neo4j_config=false

5. Ejecutar el script de preparacion para tener todas las constraints y index en la base de datos