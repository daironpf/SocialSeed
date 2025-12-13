#!/bin/bash
# topics-init.sh
BOOTSTRAP_SERVER=$1

echo "Waiting for Kafka broker to be ready..."
until /opt/kafka/bin/kafka-broker-api-versions.sh --bootstrap-server "$BOOTSTRAP_SERVER" >/dev/null 2>&1; do
  sleep 2
done

echo "Kafka broker is ready. Creating topics..."

topics=(
  "socialuser.user.created"
  "socialuser.user.updated"
  "socialuser.user.deleted"
  "socialuser.user.status.changed"
  "auth.user.registered"
  "auth.user.login"
  "auth.user.logout"
  "auth.user.credentials.updated"
  "relationships.follow.created"
  "relationships.follow.deleted"
  "relationships.friendship.requested"
  "relationships.friendship.accepted"
  "relationships.friendship.rejected"
  "relationships.friendship.removed"
)

for topic in "${topics[@]}"; do
  echo "Creating topic: $topic"
  /opt/kafka/bin/kafka-topics.sh --create \
    --bootstrap-server "$BOOTSTRAP_SERVER" \
    --if-not-exists \
    --topic "$topic" \
    --partitions 3 \
    --replication-factor 1
done

echo "All topics created."
echo "Topic initialization completed."