#!/bin/bash

# Automatic run Zookeeper and Kafka server by docker
# Makesure to start Docker desktop first

echo "Checking if Docker is running..."
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

echo "Starting Docker compose services..."
docker-compose up -d
echo "⏳ Waiting for services to start..."
sleep 20

echo "Checking if Kafka container is running..."
if [ "$(docker ps -q -f name=kafka)" ]; then
    echo "✅ Kafka container is running!"
else
    echo "❌ Kafka container failed to start"
    exit 1
fi

echo "Checking if Zookeeper container is running..."
if [ "$(docker ps -q -f name=zookeeper)" ]; then
    echo "✅ Zookeeper container is running!"
else
    echo "❌ Zookeeper container failed to start"
    exit 1
fi

echo ""
echo "🎉 Kafka setup is complete!"
echo ""
echo "📌 Services running:"
echo "   - Kafka: localhost:9092"
echo "   - Zookeeper: localhost:2181"
echo ""
echo "📋 Your topics will be auto-created:"
echo "   - user-message-topic"
echo "   - ai-response-topic"
echo ""
echo "🔧 Useful commands:"
echo "   Check containers: docker ps"
echo "   View logs: docker logs kafka"
echo "   Stop services: docker-compose down"
echo ""
echo "🚀 You can now start your Spring Boot application!"