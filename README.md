## Proyecto de aprendizaje: Arquitectura Hexagonal

Este proyecto ha sido desarrollado con el objetivo principal de **aprender y aplicar la arquitectura hexagonal (Ports & Adapters)** en un entorno real utilizando Spring Boot.

La idea no ha sido solo construir una API funcional, sino entender cómo **desacoplar la lógica de negocio del resto de capas**, permitiendo que el dominio sea independiente de frameworks, bases de datos o detalles de infraestructura.

### Qué se ha trabajado

- Separación clara entre:
  - **Dominio** (entidades y reglas de negocio)
  - **Aplicación** (casos de uso / handlers)
  - **Infraestructura** (controladores, persistencia, utilidades como gestión de ficheros)
- Uso de **puertos (interfaces)** para desacoplar el dominio de las implementaciones concretas
- Implementación de **adaptadores** para conectar con el exterior (API REST, base de datos, sistema de archivos)
- Gestión de operaciones típicas (CRUD) respetando esta separación
- Manejo de archivos (imágenes) como parte de la infraestructura
- Tests de integración para validar el comportamiento real del sistema

### Objetivo

El objetivo ha sido interiorizar cómo estructurar aplicaciones mantenibles y escalables, donde:

- Cambiar la base de datos o el framework no afecta al dominio
- La lógica de negocio es fácilmente testeable
- El código es más limpio, modular y extensible

---

## Infraestructura y despliegue

El proyecto incluye una configuración Docker completa con soporte para entornos dev y prod, Dockerfiles separados por entorno y gestión de volúmenes independientes.

### Requisitos

- Docker Desktop
- `make` — en Windows: `choco install make`

### Entornos

| Entorno | Dockerfile | Base de datos | Adminer |
|---|---|---|---|
| Dev | `Dockerfile.dev` | `web_db` + `web_db_it` | `localhost:8888` |
| Prod | `Dockerfile.prod` | `web_app_db` + `web_app_db_it` | No disponible |

Las bases de datos IT se crean automáticamente al inicializar el volumen por primera vez via `docker-config/database/init.sh`.

Las credenciales de prod se gestionan en `.env.prod`, excluido del repositorio. Ver `.env.example` como referencia.

### Comandos

Para la mayoría de casos `make dev-down` + `make dev` es suficiente.

| Comando | Descripción |
|---|---|
| `make dev` | Levanta dev con build |
| `make dev-up` | Levanta dev sin rebuild |
| `make dev-restart` | Fuerza recreación sin hacer down |
| `make dev-stop` | Pausa dev conservando estado |
| `make dev-down` | Elimina contenedores sin borrar datos |
| `make dev-clean` | Reset total — borra volúmenes y base de datos |
| `make prod` | Levanta prod con build |
| `make prod-up` | Levanta prod sin rebuild |
| `make prod-down` | Para prod conservando datos |
| `make prod-clean` | Reset total prod — borra volúmenes y base de datos |
| `make status` | Estado de los contenedores activos |
| `make logs-web` | Logs en tiempo real del contenedor web |
| `make logs-db` | Logs en tiempo real de postgres |
| `make shell-web` | Shell del contenedor web |
| `make shell-db` | Shell del contenedor postgres |
