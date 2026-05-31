# 🧱 Web — Spring Boot Hexagonal Architecture

Proyecto de práctica diseñado para explorar la **arquitectura hexagonal** (Ports & Adapters) con Spring Boot 4 y Java 21. Implementa un CRUD completo de productos con autenticación JWT, OAuth2 y un patrón Mediator para desacoplar casos de uso.

> **Objetivo de aprendizaje:** Interiorizar cómo estructurar aplicaciones mantenibles y escalables donde cambiar la base de datos o el framework no afecta al dominio, la lógica de negocio es fácilmente testeable y el código es modular y extensible.

---

## 📋 Tabla de contenidos

- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Estructura del proyecto](#-estructura-del-proyecto)
- [Módulos del dominio](#-módulos-del-dominio)
- [Seguridad](#-seguridad)
- [Base de datos y migraciones](#-base-de-datos-y-migraciones)
- [Docker y entornos](#-docker-y-entornos)
- [Comandos Make](#-comandos-make)
- [Variables de entorno](#-variables-de-entorno)
- [Ejecución local](#-ejecución-local)
- [Tests](#-tests)
- [Documentación API](#-documentación-api)
- [Notas de aprendizaje](#-notas-de-aprendizaje)

---

## 🛠 Tecnologías

| Categoría | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | PostgreSQL |
| Migraciones | Flyway 10+ |
| Seguridad | Spring Security + JWT (JJWT 0.13) + OAuth2 Client |
| Mappers | MapStruct 1.6.3 + Lombok 1.18 |
| Documentación | SpringDoc OpenAPI 3.0.3 (Swagger UI) |
| Caché | Spring Cache (ConcurrentHashMap en memoria) |
| Plantillas | Thymeleaf |
| Tests | JUnit 5, Mockito, AssertJ, MockMvc, H2 |
| Contenedores | Docker + Docker Compose |

---

## 🏛 Arquitectura

El proyecto sigue la **arquitectura hexagonal** (también conocida como *Ports & Adapters*), que separa el núcleo de negocio de los mecanismos externos (base de datos, API REST, etc.). El dominio no conoce ni depende de Spring, JPA ni ningún otro framework.

```
┌─────────────────────────────────────────────────────────────┐
│                       INFRAESTRUCTURA                        │
│                                                             │
│   ┌──────────────────┐          ┌──────────────────────┐   │
│   │    REST API       │          │    Base de Datos      │   │
│   │  Controllers      │          │  RepositoryImpl       │   │
│   │  DTOs / Mappers   │          │  Entities / Mappers   │   │
│   └────────┬──────────┘          └──────────┬────────────┘  │
│            │  (adaptador primario)           │ (adaptador secundario) │
└────────────┼────────────────────────────────┼───────────────┘
             │                                │
     ┌───────▼────────────────────────────────▼───────┐
     │                  APLICACIÓN                     │
     │   Commands │ Queries │ Mediator Pattern          │
     │          Handlers (casos de uso)                │
     └───────────────────────┬─────────────────────────┘
                             │
     ┌───────────────────────▼─────────────────────────┐
     │                   DOMINIO                        │
     │   Entities  │  Ports (interfaces)                │
     │   Exceptions │  Domain logic                     │
     └─────────────────────────────────────────────────┘
```

### Patrón Mediator

Se implementa un mediator propio (`Mediator`, `Request`, `RequestHandler`) que actúa como bus de comandos y consultas, desacoplando los controllers de los handlers de aplicación. Esto facilita la separación entre **Commands** (escritura) y **Queries** (lectura), siguiendo el principio CQRS de forma ligera.

---

## 📁 Estructura del proyecto

```
src/main/java/com/springboot/web/
│
├── common/                          # Infraestructura transversal
│   ├── application/mediator/        # Mediator, Request, RequestHandler
│   ├── domain/                      # PaginationQuery, PaginationResult
│   └── infrastructure/
│       ├── config/                  # SecurityConfig, JwtProperties, OpenApiConfig...
│       ├── controller/              # LoginController (Thymeleaf)
│       ├── exceptions/              # ApiExceptionHandler, ErrorMessage
│       ├── filters/                 # JwtFilter
│       ├── service/                 # JwtService, TokenBlacklistService
│       ├── security/                # CustomAccessDeniedHandler, AuthenticationEntryPoint
│       └── util/                    # FileUtils
│
├── product/                         # Módulo principal
│   ├── application/
│   │   ├── command/                 # CreateProduct, UpdateProduct, DeleteProduct
│   │   ├── query/                   # GetAllProduct, GetProductById
│   │   └── scheduling/              # FixProductsPriceSchedule
│   ├── domain/
│   │   ├── entity/                  # Product, ProductFilter
│   │   ├── exception/               # ProductNotFoundException
│   │   └── port/                    # ProductRepository (interfaz de salida)
│   └── infrastructure/
│       ├── api/                     # ProductController, ProductApi, DTOs, Mapper
│       └── database/                # ProductRepositoryImpl, ProductEntity, Specification
│
├── category/                        # Módulo de categorías
├── productdetail/                   # Módulo de detalles de producto
├── review/                          # Módulo de reseñas
│
└── user/                            # Módulo de usuarios y autenticación
    ├── application/command/         # LoginUser, RegisterUser
    ├── domain/
    │   ├── entity/                  # User, UserRole
    │   └── port/                    # UserRepository, AuthenticationPort, PasswordEncoderPort
    └── infrastructure/
        ├── api/                     # UserController, DTOs, Mapper
        ├── authentication/          # AuthenticationImpl
        ├── database/                # UserRepositoryImpl, UserEntity, AdminSeeder
        ├── password/                # PasswordEncoderImpl
        └── security/                # CustomUserDetails
```

---

## 📦 Módulos del dominio

### Product
Módulo central del proyecto. Soporta operaciones CRUD completas con filtrado dinámico (`ProductSpecification`), paginación y especificaciones JPA. Incluye un `@Scheduled` para corrección automática de precios y un seeder que carga datos desde `resources/seed/products.json`.

### Category
Gestión de categorías con relación many-to-many con productos. Seeder desde `resources/seed/categories.json`.

### ProductDetail
Entidad complementaria asociada al producto, con su propia cadena de capas hexagonales.

### Review
Módulo de reseñas vinculadas a productos. Estructura hexagonal completa preparada para ampliar.

### User
Autenticación completa con:
- Registro y login con JWT
- Blacklist de tokens para logout seguro (`TokenBlacklistService`)
- Roles (`UserRole`)
- Seeder para usuario administrador (`AdminSeeder`)

---

## 🔐 Seguridad

La seguridad está gestionada por Spring Security con tres mecanismos:

**JWT (JSON Web Tokens)**
- `JwtFilter` intercepta cada petición y valida el token antes de que llegue al controller
- `JwtService` gestiona generación, validación y extracción de claims
- `TokenBlacklistService` invalida tokens en logout (almacenado en memoria con `ConcurrentHashMap`)
- Propiedades configurables: `JWT_SECRET_KEY`, `JWT_TOKEN_EXPIRATION`, `JWT_REFRESH_TOKEN_EXPIRATION`

**OAuth2 Client**
- Integración con GitHub (configurable mediante `GITHUB_CLIENT_ID` / `GITHUB_CLIENT_SECRET`)
- Flujo estándar de autorización OAuth2

**Roles y autorización**
- `CustomAccessDeniedHandler` para respuestas 403 en formato JSON consistente
- `CustomAuthenticationEntryPoint` para respuestas 401 personalizadas
- `CustomUserDetails` implementa `UserDetails` de Spring Security

---

## 🗄 Base de datos y migraciones

Las migraciones son gestionadas por **Flyway** y se ejecutan automáticamente al arrancar la aplicación. Los scripts están en `src/main/resources/db/migration/`:

| Versión | Descripción |
|---|---|
| V1 | Tabla `categories` |
| V2 | Tabla `product_details` |
| V3 | Tabla `products` |
| V4 | Tabla `reviews` |
| V5 | Tabla join `products_categories` (many-to-many) |
| V6 | Tabla `users` |

> **Nota técnica:** El `pom.xml` configura resource filtering únicamente sobre `banner.txt` y `application.yml`. Esto garantiza que los archivos `.sql` de Flyway se copien al JAR sin ser procesados como plantillas Maven, evitando que fallen al contener caracteres como `${}`.

En entorno dev y prod existen dos bases de datos por entorno: la principal (`web_db` / `web_app_db`) y una de integración IT (`web_db_it` / `web_app_db_it`), creadas automáticamente por `docker-config/database/init.sh` en el primer arranque del volumen.

---

## 🐳 Docker y entornos

El proyecto dispone de configuraciones Docker separadas por entorno:

| Archivo | Entorno | BD | Adminer |
|---|---|---|---|
| `docker-compose.dev.yml` + `Dockerfile.dev` | Desarrollo | `web_db` + `web_db_it` | `localhost:8888` |
| `docker-compose.local.yml` | Local (solo BD, app con Maven) | `web_db` | `localhost:8888` |
| `docker-compose.prod.yml` + `Dockerfile.prod` | Producción | `web_app_db` + `web_app_db_it` | No disponible |

Los perfiles de Spring Boot correspondientes son `dev`, `local`, `prod` y `test`, con sus respectivos `application-{profile}.yml`.

Las credenciales de producción se gestionan en `.env.prod`, excluido del repositorio. Ver `.env.prod.example` como referencia.

### Requisitos

- Docker Desktop
- `make` — en Windows: `choco install make`

---

## ⚡ Comandos Make

Para la mayoría de casos, `make dev-down` + `make dev` es suficiente.

**Local (solo BD, app desde IntelliJ)**

| Comando | Descripción |
|---|---|
| `make local-db` | Levanta solo la base de datos |
| `make local-db-stop` | Para los contenedores conservando estado |
| `make local-db-clean` | Reset total — borra volúmenes y base de datos |

**Desarrollo**

| Comando | Descripción |
|---|---|
| `make dev` | Levanta dev con build |
| `make dev-up` | Levanta dev sin rebuild |
| `make dev-restart` | Fuerza recreación sin hacer down |
| `make dev-stop` | Para contenedores conservando estado |
| `make dev-down` | Elimina contenedores sin borrar datos |
| `make dev-clean` | Reset total — borra volúmenes y base de datos |

**Producción**

| Comando | Descripción |
|---|---|
| `make prod` | Levanta prod con build |
| `make prod-up` | Levanta prod sin rebuild |
| `make prod-down` | Para prod conservando datos |
| `make prod-clean` | Reset total prod — borra volúmenes y base de datos |

**Utilidades**

| Comando | Descripción |
|---|---|
| `make status` | Estado de todos los contenedores activos |
| `make logs-web` | Logs en tiempo real del contenedor web |
| `make logs-db` | Logs en tiempo real de postgres |
| `make logs-adminer` | Logs en tiempo real de adminer |
| `make shell-web` | Shell del contenedor web |
| `make shell-db` | Shell del contenedor postgres |

---

## ⚙️ Variables de entorno

Copia `.env.dev.example` o `.env.prod.example` y rellena los valores:

```env
# Base de datos
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_DB=web_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=tu_password
DB_SSL_MODE=disable

# Servidor
SERVER_PORT=8080

# JWT
JWT_SECRET_KEY=clave_secreta_minimo_32_caracteres_aqui
JWT_TOKEN_EXPIRATION=3600000         # 1 hora en ms
JWT_REFRESH_TOKEN_EXPIRATION=604800000  # 7 días en ms

# OAuth2 - GitHub
GITHUB_CLIENT_ID=tu_client_id
GITHUB_CLIENT_SECRET=tu_client_secret

# Admin por defecto (creado por AdminSeeder al arrancar)
ADMIN_EMAIL=admin@tudominio.com
ADMIN_PASSWORD_HASH=hash_bcrypt_de_tu_password
```

> ⚠️ Nunca subas archivos `.env` con valores reales al repositorio. Los archivos `.env.dev` y `.env.prod` están incluidos en `.gitignore`.

---

## 🚀 Ejecución local

### Con Make + Docker (recomendado)

```bash
# Levantar entorno de desarrollo completo (app + BD)
make dev

# Solo la base de datos (para correr la app con Maven directamente)
docker-compose -f docker-compose.local.yml up
```

### Con Maven (app sin Docker)

```bash
# Copia y rellena las variables de entorno
cp .env.dev.example .env.dev

# Arranca la aplicación con perfil dev
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Construir el JAR

```bash
./mvnw clean package -DskipTests
java -jar target/web-1.0.0.jar
```

---

## 🧪 Tests

El proyecto tiene cuatro niveles de test, todos centrados en el módulo `product`:

| Tipo | Clase | Descripción |
|---|---|---|
| Unitario | `GetAllProductHandlerTest` | Handler de consulta paginada con Mockito |
| Unitario | `GetProductByIdHandlerTest` | Handler de búsqueda por ID con Mockito |
| Slice | `ProductControllerTest` | `@WebMvcTest` del controller REST con MockMvc |
| Slice | `ProductRepositoryImplTest` | `@DataJpaTest` del repositorio contra H2 |
| Integración | `ProductIT` | Test de integración completo con contexto Spring |

```bash
# Ejecutar todos los tests
./mvnw test

# Con output detallado en consola
./mvnw test -Dsurefire.useFile=false
```

Los tests de integración utilizan H2 en memoria (perfil `test`) y `TestSecurityConfig` para desactivar restricciones de seguridad durante las pruebas.

---

## 📖 Documentación API

Con la aplicación en marcha, la documentación Swagger UI está disponible en:

```
http://localhost:{SERVER_PORT}/swagger-ui.html
```

La especificación OpenAPI en formato JSON:

```
http://localhost:{SERVER_PORT}/v3/api-docs
```

La configuración se encuentra en `OpenApiConfig.java` dentro de `common/infrastructure/config`.

---

## 📝 Notas de aprendizaje

Este proyecto fue construido como ejercicio práctico siguiendo tutoriales sobre arquitectura hexagonal en Spring Boot. Conceptos clave explorados:

- **Inversión de dependencias**: el dominio define interfaces (puertos), la infraestructura las implementa (adaptadores), nunca al revés
- **CQRS ligero**: separación de Commands (escritura) y Queries (lectura) con handlers dedicados
- **Patrón Mediator**: desacopla controllers de la lógica de aplicación sin importar dependencias cruzadas
- **Seeders**: precarga de datos de desarrollo sin interferir con las migraciones de Flyway
- **Resource filtering en Maven**: configuración precisa para que Flyway encuentre los SQL en el JAR sin corrupción
- **Scheduled tasks**: corrección automática de precios con `@Scheduled`
- **Blacklist de tokens JWT**: logout stateful sin necesidad de base de datos adicional
- **Slice tests**: `@WebMvcTest` y `@DataJpaTest` para testear capas aisladas sin levantar el contexto completo
