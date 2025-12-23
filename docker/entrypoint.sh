#!/usr/bin/env bash
set -e

# Wait for a TCP host:port to become available using bash /dev/tcp
wait_for() {
  local host=$1
  local port=$2
  local retries=60
  local wait=2

  for i in $(seq 1 $retries); do
    if (echo > /dev/tcp/${host}/${port}) >/dev/null 2>&1; then
      echo "[entrypoint] $host:$port is available"
      return 0
    fi
    echo "[entrypoint] waiting for $host:$port... ($i/$retries)"
    sleep $wait
  done

  echo "[entrypoint] timeout waiting for $host:$port"
  return 1
}


DB_HOST=${DB_HOST:-db}
DB_PORT=${DB_PORT:-3306}

echo "[entrypoint] Using DB at ${DB_HOST}:${DB_PORT}"

# If DB wait is desired and host reachable, wait for it
if [ -n "$DB_HOST" ]; then
  wait_for "$DB_HOST" "$DB_PORT" || echo "[entrypoint] continuing despite DB wait failure"
fi

echo "[entrypoint] starting application"
exec java $JAVA_OPTS -jar /app/app.jar
