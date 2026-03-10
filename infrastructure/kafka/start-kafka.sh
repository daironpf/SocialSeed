#!/bin/bash
set -e

# Formatear storage si no existe
if [ ! -f "/var/lib/kafka/data/meta.properties" ]; then
  echo "Formatting Kafka storage..."
  /opt/kafka/bin/kafka-storage.sh format \
    -t $(/opt/kafka/bin/kafka-storage.sh random-uuid) \
    -c /opt/kafka/config/kraft/server.properties
fi

# Iniciar Kafka en segundo plano
echo "Starting Kafka in background..."
/opt/kafka/bin/kafka-server-start.sh /opt/kafka/config/kraft/server.properties &
KAFKA_PID=$!

# Esperar a que Kafka esté listo
echo "Waiting for Kafka broker..."
until /opt/kafka/bin/kafka-broker-api-versions.sh --bootstrap-server kafka:9092 >/dev/null 2>&1; do
  sleep 2
done

# Crear topics si no existen
echo "Checking existing topics..."
EXISTING_TOPICS=$(/opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --list)

if [ -z "$EXISTING_TOPICS" ]; then
  echo "No topics found. Creating SocialSeed topics..."
  chmod +x /scripts/topics-init.sh
  /scripts/topics-init.sh kafka:9092
  echo "Topics created."
else
  echo "Topics already exist. Skipping creation."
fi

# Mantener Kafka corriendo
wait $KAFKA_PID
echo "Kafka process has exited."