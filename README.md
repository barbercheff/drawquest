# DrawQuest

DrawQuest es una aplicacion de misiones de dibujo. Los usuarios se registran, inician sesion, completan quests subiendo dibujos y progresan con XP y niveles cuando sus dibujos son aprobados.

El proyecto esta en desarrollo. El trabajo actual esta centrado en el backend Java/Spring Boot; cuando esta base quede cerrada, el siguiente objetivo sera construir el frontend y completar la aplicacion.

Estado de referencia: 2026-07-23.

## Estado Actual

Backend base implementado y con tests de integracion:

- Registro y login con JWT.
- Passwords guardadas con BCrypt desde `UserServiceImpl.createUser`.
- Registro publico con rol por defecto `ROLE_USER`.
- Endpoints protegidos con Spring Security y filtro JWT.
- `GET /users` y `POST /users` restringidos a `ROLE_ADMIN`.
- CRUD de quests restringido por rol:
  - `GET /quests/**`: usuario autenticado.
  - `POST /quests`: `ROLE_ADMIN` o `ROLE_MODERATOR`.
  - `PUT /quests/{id}`: `ROLE_ADMIN` o `ROLE_MODERATOR`.
  - `DELETE /quests/{id}`: `ROLE_ADMIN`.
- Aprobacion de dibujos restringida a `ROLE_ADMIN` o `ROLE_MODERATOR`.
- Usuarios solo pueden consultar/modificar sus propios dibujos.
- Usuarios solo pueden consultar su propio progreso.
- Validacion con `@Valid` en payloads principales.
- Swagger disponible en `/swagger-ui.html`.
- Ejemplos practicos de API en `API_EXAMPLES.md`.
- Migraciones de base de datos versionadas con Flyway en `src/main/resources/db/migration`.
- Logging basico de eventos de negocio con SLF4J/Logback.

## Stack

- Java 17
- Spring Boot 3.5.16
- Maven wrapper incluido (`mvnw` / `mvnw.cmd`)
- Spring Web
- Spring Security
- Spring Data JPA
- JWT con `jjwt`
- MySQL para desarrollo local
- H2 para tests
- Flyway para migraciones de base de datos
- Lombok
- Swagger/OpenAPI con springdoc

## Requisitos

- Java 17 o superior.
- Maven o el wrapper incluido.
- MySQL local para ejecutar la app contra base de datos real.

## Configuracion

La aplicacion lee configuracion sensible desde variables de entorno:

```properties
DRAWQUEST_DB_URL=jdbc:mysql://localhost:3306/drawquest_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DRAWQUEST_DB_USERNAME=drawquest_admin
DRAWQUEST_DB_PASSWORD=your_password
DRAWQUEST_JWT_SECRET=base64_secret_with_at_least_256_bits
DRAWQUEST_JWT_EXPIRATION_MS=3600000
```

`DRAWQUEST_JWT_SECRET` debe estar en Base64 y tener al menos 256 bits para HS256.

## Ejecutar

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

En macOS/Linux:

```bash
./mvnw spring-boot:run
```

La aplicacion se levanta en el puerto `8080` por defecto.

Swagger:

```text
http://localhost:8080/swagger-ui.html
```

## Tests

Ejecutar suite:

```powershell
.\mvnw.cmd test
```

La suite actual esta en `src/test/java/com/drawquest/DrawquestIntegrationTest.java` y cubre:

- Registro guarda password hasheada.
- Login devuelve JWT.
- Usuarios normales no pueden listar usuarios, aprobar dibujos ni mutar quests.
- Moderadores pueden crear/editar quests, pero no borrarlas.
- Admin puede borrar quests.
- Payloads invalidos devuelven `400 VALIDATION_ERROR`.
- Usuarios solo acceden a sus propios dibujos y progreso.
- Aprobar un dibujo completa progreso y suma XP una sola vez.

Ultima comprobacion local observada: 7 tests, 0 fallos, 0 errores.

## Rutas API

| Metodo | Ruta                    | Permiso                       | Descripcion |
|--------|-------------------------|-------------------------------|-------------|
| POST   | `/auth/register`        | Publico                       | Registra usuario con `ROLE_USER` |
| POST   | `/auth/login`           | Publico                       | Devuelve JWT |
| GET    | `/users`                | `ROLE_ADMIN`                  | Lista usuarios |
| GET    | `/users/me`             | Autenticado                   | Devuelve usuario actual |
| GET    | `/users/{id}`           | Propio usuario                | Devuelve usuario por ID |
| POST   | `/users`                | `ROLE_ADMIN`                  | Crea usuario |
| DELETE | `/users/{id}`           | Propio usuario                | Borra usuario |
| GET    | `/quests`               | Autenticado                   | Lista quests |
| GET    | `/quests/{id}`          | Autenticado                   | Devuelve quest |
| POST   | `/quests`               | `ROLE_ADMIN`/`ROLE_MODERATOR` | Crea quest |
| PUT    | `/quests/{id}`          | `ROLE_ADMIN`/`ROLE_MODERATOR` | Actualiza quest |
| DELETE | `/quests/{id}`          | `ROLE_ADMIN`                  | Borra quest |
| GET    | `/drawings`             | Autenticado, propios          | Lista dibujos propios |
| GET    | `/drawings/{id}`        | Autenticado, propio           | Devuelve dibujo propio |
| POST   | `/drawings`             | Autenticado                   | Crea dibujo para una quest |
| PUT    | `/drawings/{id}`        | Autenticado, propio           | Actualiza dibujo propio |
| PUT    | `/drawings/{id}/approve`| `ROLE_ADMIN`/`ROLE_MODERATOR` | Aprueba dibujo y otorga XP |
| DELETE | `/drawings/{id}`        | Autenticado, propio           | Borra dibujo propio |
| GET    | `/progress`             | Autenticado, propio           | Lista progreso propio |
| GET    | `/progress/{id}`        | Autenticado, propio           | Devuelve progreso propio |

Las llamadas protegidas requieren:

```text
Authorization: Bearer <token>
```

## Logging

La aplicacion registra eventos de negocio relevantes sin incluir passwords, JWT ni payloads completos:

- login correcto o fallido;
- creacion y borrado de usuarios;
- creacion, actualizacion y borrado de quests;
- creacion, actualizacion, borrado y aprobacion de dibujos;
- errores inesperados.

Nivel configurable:

```properties
DRAWQUEST_LOG_LEVEL=INFO
```

## Estructura

```text
src/main/java/com/drawquest/
|-- controllers/      # Controladores REST
|-- services/         # Logica de negocio
|-- repositories/     # Repositorios JPA
|-- models/           # Entidades
|-- dtos/             # DTOs de entrada/salida
|-- mappers/          # Conversion entidad/DTO
|-- security/         # JWT y filtro de autenticacion
|-- config/           # Configuracion Spring/Security
`-- DrawquestApplication.java
```

## Documentacion Practica

Para ejemplos PowerShell de registro, login, JWT, quests, drawings, progress y asignacion local de roles, ver:

```text
API_EXAMPLES.md
```

## Proximos Pasos

La base de backend indicada en el handoff ya esta completada. La lista activa es:

1. Decidir e implementar subida real de imagenes para dibujos.
2. Construir el frontend para consumir el backend y completar la aplicacion.

## Notas del Workspace

El repo Git activo esta dentro de la carpeta `drawquest`. La carpeta padre tenia un repo accidental que fue desactivado como `.git.disabled-parent-repo`.

## Colaboradores

- barbercheff - Desarrollador principal
