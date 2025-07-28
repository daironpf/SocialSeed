# Utilizar una imagen Python oficial y establecer una versión específica
FROM python:3.11.2-slim AS python-base

# Primero, construir una imagen temporal llamada "python-base" que incluya todas las dependencias de Python
# Esto facilitará la copia de estas dependencias en la imagen final

FROM python-base AS builder

# Establecer el directorio de trabajo en /app
WORKDIR /app

# Copiar el script Python y hacerlo ejecutable
COPY . /app

# Copiar el archivo requirements.txt
COPY requirements.txt requirements.txt

# Instalar las dependencias en la imagen temporal
RUN pip install --no-cache-dir -r requirements.txt

# Ahora, construir la imagen final sin la necesidad de incluir las herramientas de compilación y dependencias
FROM python:3.11.2-slim

# Establecer el directorio de trabajo en /app
WORKDIR /app

# Copiar solo las dependencias de Python desde la imagen temporal "builder"
COPY --from=builder /usr/local /usr/local

# Copiar el script Python y el archivo requirements.txt
COPY . /app
COPY requirements.txt requirements.txt

# # Comando para ejecutar la aplicación cuando el contenedor se inicie
# CMD ["python", "generate.py"]
