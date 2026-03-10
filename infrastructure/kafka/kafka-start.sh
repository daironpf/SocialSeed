#!/bin/bash
set -e

# Formatear el storage si es la primera vez
if [ ! -f /var/lib/kafka/data/meta.properties ]; then
  echo "Formatting Kafka storage..."
  /opt/kafka/bin/kafka-storage.sh format \
    -t $(/opt/kafka/bin/kafka-storage.sh random-uuid) \
    -c /opt/kafka/config/kraft/server.properties
fi

echo "Starting Kafka..."
exec /opt/kafka/bin/kafka-server-start.sh /opt/kafka/config/kraft/server.properties
