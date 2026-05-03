#!/bin/bash
# Script de inicialización ejecutado por PostgreSQL al crear el volumen por primera vez.
# Crea automáticamente una base de datos IT a partir del nombre de la base principal
# añadiendo el sufijo _it. Ejemplo: web_db -> web_db_it.
# La variable POSTGRES_DB es inyectada por docker-compose en tiempo de ejecución.
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "postgres" <<-EOSQL
    SELECT 'CREATE DATABASE "${POSTGRES_DB}_it"'
    WHERE NOT EXISTS (
        SELECT FROM pg_database WHERE datname = '${POSTGRES_DB}_it'
    )\gexec
EOSQL