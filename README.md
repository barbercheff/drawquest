# DrawQuest

[![Java CI](https://github.com/barbercheff/drawquest/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/barbercheff/drawquest/actions/workflows/maven.yml)

DrawQuest es una aplicacion de misiones de dibujo. Los usuarios se registran, inician sesion, completan quests subiendo dibujos y progresan con XP y niveles cuando sus dibujos son aprobados.

El proyecto esta en desarrollo. El trabajo actual esta centrado en el backend Java/Spring Boot; cuando esta base quede cerrada, el siguiente objetivo sera construir el frontend y completar la aplicacion.

Estado de referencia: 2026-08-19.

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
- Subida real de imagenes de dibujos con `multipart/form-data`.
- Imagenes guardadas como archivos locales y URLs relativas en base de datos.
- Usuarios solo pueden consultar su propio progreso.
- Tests unitarios de services para autenticacion, usuarios, quests, dibujos y progreso.
- Validacion con `@Valid` en payloads principales y validacion de IDs positivos en parametros de ruta.
- Swagger disponible en `/swagger-ui.html`.
- Ejemplos practicos de API en `API_EXAMPLES.md`.
- Coleccion Postman local en `postman/`.
- Migraciones de base de datos versionadas con Flyway en `src/main/resources/db/migration`.
- Quests iniciales creadas con Flyway en `V3__seed_initial_quests.sql`.
- Logging basico de eventos de negocio con SLF4J/Logback.
- Spring Boot Actuator habilitado con endpoints `health` e `info`.
- Timestamps de auditoria `createdAt` y `modifiedAt` en entidades principales.

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
- Spring Boot Actuator para health/info
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
DRAWQUEST_UPLOAD_DRAWINGS_DIR=uploads/drawings
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

La suite actual esta en:

- `src/test/java/com/drawquest/DrawquestIntegrationTest.java`
- `src/test/java/com/drawquest/JwtExpirationIntegrationTest.java`
- `src/test/java/com/drawquest/JwtUtilTest.java`
- `src/test/java/com/drawquest/ServicesUnitTest.java`

Cubre:

- Registro guarda password hasheada.
- Login devuelve JWT.
- Endpoints publicos/protegidos y tokens invalidos.
- Usuarios normales no pueden listar usuarios, aprobar dibujos ni mutar quests.
- Moderadores pueden crear/editar quests, pero no borrarlas.
- Admin puede borrar quests.
- Payloads invalidos devuelven `400 VALIDATION_ERROR`.
- Subida multipart de imagenes guarda archivo y devuelve URL publica.
- Tipos de archivo invalidos devuelven `400 VALIDATION_ERROR`.
- Usuarios solo acceden a sus propios dibujos y progreso.
- Tokens JWT caducados son rechazados.
- Aprobar un dibujo completa progreso y suma XP una sola vez.
- Services de autenticacion, usuarios, quests, dibujos y progreso con mocks.

Ultima comprobacion local observada: 25 tests, 0 fallos, 0 errores.

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
| POST   | `/drawings` JSON        | Autenticado                   | Crea dibujo con URL existente |
| POST   | `/drawings` multipart   | Autenticado                   | Sube imagen y crea dibujo |
| PUT    | `/drawings/{id}`        | Autenticado, propio           | Actualiza dibujo propio |
| PUT    | `/drawings/{id}/image`  | Autenticado, propio           | Reemplaza imagen del dibujo |
| PUT    | `/drawings/{id}/approve`| `ROLE_ADMIN`/`ROLE_MODERATOR` | Aprueba dibujo y otorga XP |
| DELETE | `/drawings/{id}`        | Autenticado, propio           | Borra dibujo propio |
| GET    | `/uploads/drawings/**`  | Publico                       | Sirve imagenes subidas |
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

## Actuator

Endpoints expuestos:

```text
http://localhost:8080/actuator/health
http://localhost:8080/actuator/info
```

Solo estan expuestos `health` e `info`. Los detalles de health no se muestran publicamente.

## Uploads

Las imagenes de dibujos se suben con `multipart/form-data` y se guardan como archivos locales. La base de datos conserva solo la URL relativa en `drawings.image_url`.

Configuracion:

```properties
DRAWQUEST_UPLOAD_DRAWINGS_DIR=uploads/drawings
```

La API sirve esos archivos desde:

```text
http://localhost:8080/uploads/drawings/<filename>
```

Tipos permitidos: JPEG, PNG, WEBP y GIF. El limite actual se controla con `spring.servlet.multipart.max-file-size=5MB`.

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

Para pruebas manuales con Postman, importar:

```text
postman/DrawQuest.postman_collection.json
postman/DrawQuest.local.postman_environment.json
```

## Proximos Pasos

La base de backend indicada en el handoff ya esta completada. La lista activa es:

1. Configurar CORS cuando empiece el frontend.
2. Construir el frontend para consumir el backend y completar la aplicacion.
3. Integrar SonarCloud y pulir documentacion de portfolio.

## Notas del Workspace

El repo Git activo esta dentro de la carpeta `drawquest`. La carpeta padre tenia un repo accidental que fue desactivado como `.git.disabled-parent-repo`.

## Colaboradores

- barbercheff - Desarrollador principal
