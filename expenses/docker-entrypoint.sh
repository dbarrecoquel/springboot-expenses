#!/bin/bash

set -e

echo "======================================"
echo "🚀 Expenses Application Starting"
echo "======================================"

wait_for_postgres() {
    echo "⏳ Waiting for PostgreSQL to be ready..."
    max_attempts=30
    attempt=0

    while [ $attempt -lt $max_attempts ]; do
        if timeout 1 bash -c "cat < /dev/null > /dev/tcp/postgres/5432" 2>/dev/null; then
            echo "✅ PostgreSQL is ready!"
            return 0
        fi
        echo "   PostgreSQL is unavailable - sleeping (attempt $((attempt+1))/$max_attempts)"
        sleep 2
        attempt=$((attempt+1))
    done

    echo "❌ PostgreSQL did not become ready in time"
    exit 1
}

wait_for_postgres

echo "======================================"
echo "🌐 Starting FrontRest API (Profile: ${SPRING_PROFILES_ACTIVE:-default})"
echo "======================================"

java -jar frontrest.jar \
    --server.port=8084 \
    --spring.profiles.active=${SPRING_PROFILES_ACTIVE:-dev} \
    --spring.datasource.url=${SPRING_DATASOURCE_URL} \
    --spring.datasource.username=${SPRING_DATASOURCE_USERNAME} \
    --spring.datasource.password=${SPRING_DATASOURCE_PASSWORD} &

FRONTREST_PID=$!
echo "✅ FrontRest started with PID: $FRONTREST_PID"

cleanup() {
    echo "🛑 Shutting down application..."
    kill $FRONTREST_PID 2>/dev/null
    wait $FRONTREST_PID 2>/dev/null
    echo "✅ Shutdown complete"
    exit 0
}

trap cleanup SIGTERM SIGINT

wait $FRONTREST_PID