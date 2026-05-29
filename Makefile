# ===========
# LOCAL (solo DB para IntelliJ)
# ===========

# Levanta solo la base de datos para desarrollo local desde IntelliJ
local-db:
	docker compose -f docker-compose.local.yml up

# Para contenedores conservando estado — usar si vas a reanudar pronto en el mismo entorno
local-db-stop:
	docker compose -f docker-compose.local.yml down

# Reset total local — BORRA VOLÚMENES Y BASE DE DATOS
local-db-clean:
	docker compose -f docker-compose.local.yml down -v --remove-orphans

# ===========
# DEVELOPMENT
# ===========

# Levanta dev con build — usar al empezar o tras cambios en código
dev:
	docker compose -f docker-compose.yml -f docker-compose.dev.yml up --build

# Levanta dev sin rebuild — usar cuando la imagen ya está construida
dev-up:
	docker compose -f docker-compose.yml -f docker-compose.dev.yml up

# Fuerza recreación de contenedores sin hacer down — útil si Docker se queda en estado raro
dev-restart:
	docker compose -f docker-compose.yml -f docker-compose.dev.yml up -d --build --force-recreate

# Para contenedores conservando estado — usar si vas a reanudar pronto en el mismo entorno
dev-stop:
	docker compose -f docker-compose.yml -f docker-compose.dev.yml stop

# Elimina contenedores sin borrar datos — usar para cambiar de entorno o tras cambios en docker-compose
dev-down:
	docker compose -f docker-compose.yml -f docker-compose.dev.yml down

# Reset total dev — BORRA VOLÚMENES Y BASE DE DATOS
dev-clean:
	docker compose -f docker-compose.yml -f docker-compose.dev.yml down -v --remove-orphans


# ==========
# PRODUCTION
# ==========

# Levanta prod con build — usar en deploy o tras cambios en configuración
prod:
	docker compose -f docker-compose.yml -f docker-compose.prod.yml up --build

# Levanta prod sin rebuild — usar cuando la imagen ya está construida
prod-up:
	docker compose -f docker-compose.yml -f docker-compose.prod.yml up -d

# Para prod conservando datos
prod-down:
	docker compose -f docker-compose.yml -f docker-compose.prod.yml down

# Reset total prod — BORRA VOLÚMENES Y BASE DE DATOS
prod-clean:
	docker compose -f docker-compose.yml -f docker-compose.prod.yml down -v --remove-orphans


# =======
# UTILITY
# =======

# Estado de todos los contenedores activos
status:
	docker ps

# Logs en tiempo real del contenedor web
logs-web:
	docker logs -f web-api

# Logs en tiempo real de postgres
logs-db:
	docker logs -f web-db

# Logs en tiempo real de adminer
logs-adminer:
	docker logs -f web-db-adminer

# Accede al shell del contenedor web
shell-web:
	docker exec -it web-api sh

# Accede al shell del contenedor postgres
shell-db:
	docker exec -it web-db sh