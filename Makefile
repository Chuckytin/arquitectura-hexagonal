dev:
	docker compose -f docker-compose.yml -f docker-compose.dev.yml up --build

dev-up:
	docker compose -f docker-compose.yml -f docker-compose.dev.yml up

prod:
	docker compose -f docker-compose.yml -f docker-compose.prod.yml up --build

prod-up:
	docker compose -f docker-compose.yml -f docker-compose.prod.yml up

down-dev:
	docker compose -f docker-compose.yml -f docker-compose.dev.yml down

down-prod:
	docker compose -f docker-compose.yml -f docker-compose.prod.yml down

down-clean:
	docker compose down -v --remove-orphans