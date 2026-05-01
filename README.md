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

## Infraestructura y despliegue

El proyecto incluye una configuración completa de Docker con soporte para múltiples entornos.

### Entornos
- **Dev**: credenciales visibles en docker-compose.dev.yml, Adminer disponible en `localhost:8888`
- **Prod**: credenciales gestionadas via `.env.prod` (excluido del repositorio)

### Requisitos
- Docker Desktop
- make (`choco install make` en Windows)

### Comandos disponibles

| Comando | Descripción |
|---|---|
| `make dev` | Levanta entorno dev con build |
| `make dev-up` | Levanta entorno dev sin rebuild |
| `make prod` | Levanta entorno prod con build |
| `make prod-up` | Levanta entorno prod sin rebuild |
| `make down-dev` | Para entorno dev conservando datos |
| `make down-prod` | Para entorno prod conservando datos |
| `make down-clean` | Para todo y elimina volúmenes |

### Bases de datos
Cada entorno inicializa automáticamente dos bases de datos al crear el volumen por primera vez:
- **Dev**: `web_db` y `web_db_it`
- **Prod**: `web_app_db` y `web_app_db_it`

### Objetivo

El objetivo ha sido interiorizar cómo estructurar aplicaciones mantenibles y escalables, donde:

- Cambiar la base de datos o el framework no afecta al dominio
- La lógica de negocio es fácilmente testeable
- El código es más limpio, modular y extensible
