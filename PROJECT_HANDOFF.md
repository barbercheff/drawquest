# DrawQuest - resumen para continuar trabajo

Fecha de referencia: 2026-07-26.

## Contexto general

Proyecto ubicado en:

`C:\Users\victor\Desktop\PROGRAMACION\APPS\drawquest app`

La app principal esta dentro de:

`drawquest`

DrawQuest es una aplicacion de misiones de dibujo. El trabajo actual esta centrado en el backend Java/Spring Boot; despues vendra el frontend para completar la aplicacion.

La idea es que usuarios participen en misiones de dibujo, suban dibujos, reciban aprobacion y progresen con XP/niveles.

## Estado actual

Backend base completado y verificado:

- Autenticacion con registro/login y JWT.
- Passwords codificadas con BCrypt en `UserServiceImpl.createUser`.
- Registro publico con rol por defecto `ROLE_USER`.
- `POST /users` restringido a `ROLE_ADMIN`.
- Usuarios solo pueden consultar/borrar su propio usuario por ID.
- CRUD de quests protegido por rol:
  - `GET /quests/**`: autenticado.
  - `POST /quests`: `ROLE_ADMIN` o `ROLE_MODERATOR`.
  - `PUT /quests/*`: `ROLE_ADMIN` o `ROLE_MODERATOR`.
  - `DELETE /quests/*`: `ROLE_ADMIN`.
- Usuarios solo pueden listar/ver/editar/borrar sus propios dibujos.
- Aprobacion de dibujos restringida a `ROLE_ADMIN` o `ROLE_MODERATOR`.
- Usuarios solo pueden listar/ver su propio progreso.
- Validacion con `@Valid` en payloads principales.
- Validacion de IDs positivos en parametros de ruta con `@Validated` y `@Positive`.
- Validacion de `DrawingCreateDTO.questId` como positivo.
- Handler de errores de validacion devuelve `400 VALIDATION_ERROR`.
- Swagger disponible en `/swagger-ui.html`.
- Actuator disponible en `/actuator/health` y `/actuator/info`.
- Logging basico de eventos de negocio con SLF4J/Logback.
- Coleccion Postman local en `postman/`.
- Ejemplos practicos de API en `API_EXAMPLES.md`.
- Migraciones Flyway en `src/main/resources/db/migration`.

## Stack

- Java 17
- Spring Boot 3.5.16
- Maven wrapper incluido (`mvnw.cmd`)
- Spring Web
- Spring Security
- Spring Data JPA
- Spring Boot Actuator
- JWT con `jjwt` 0.13.0
- MySQL para desarrollo local
- H2 para tests
- Flyway
- Lombok
- Swagger/OpenAPI con springdoc

## Estructura

- `controllers`: controladores REST
- `services` y `services/impl`: logica de negocio
- `repositories`: repositorios JPA
- `models`: entidades
- `dtos`: DTOs de entrada/salida
- `mappers`: conversion entidad/DTO
- `security`: JWT y filtro de autenticacion
- `config/SecurityConfig.java`: reglas de seguridad
- `resources/db/migration`: migraciones Flyway para tablas y roles base
- `postman`: coleccion y environment local de Postman

El repo Git activo esta dentro de `drawquest`. La carpeta padre tiene el antiguo repo accidental desactivado como `.git.disabled-parent-repo`.

## Funcionalidad implementada

### Autenticacion

- `POST /auth/register`
- `POST /auth/login`
- JWT en header `Authorization: Bearer <token>`

### Usuarios

- `GET /users`
- `GET /users/me`
- `GET /users/{id}`
- `POST /users`
- `DELETE /users/{id}`

### Quests

- `GET /quests`
- `GET /quests/{id}`
- `POST /quests`
- `PUT /quests/{id}`
- `DELETE /quests/{id}`

### Dibujos

- `GET /drawings`
- `GET /drawings/{id}`
- `POST /drawings`
- `PUT /drawings/{id}`
- `PUT /drawings/{id}/approve`
- `DELETE /drawings/{id}`

### Progreso

- `GET /progress`
- `GET /progress/{id}`

### Actuator

- `GET /actuator/health`
- `GET /actuator/info`

## Logica de progreso

- Al crear un dibujo se crea o recupera el progreso de usuario+quest y se incrementan los intentos.
- Al aprobar un dibujo:
  - el dibujo queda aprobado.
  - el progreso se marca como completado si no lo estaba.
  - se suma XP al usuario.
  - se recalcula nivel con `xp / 100`.
  - no se vuelve a sumar XP si esa quest ya estaba completada.

## Historial reciente

### 2026-07-11

- Corregido `DrawingMapper`: `createdAt` y `modifiedAt` ya no salen invertidos en `DrawingResponseDTO`.
- Movida la codificacion BCrypt de passwords a `UserServiceImpl.createUser`.
- Eliminada la codificacion directa en `AuthController.register`.
- Restringido `POST /users` a `ROLE_ADMIN`.
- Restringido CRUD de quests por rol.
- Anadido `@Valid` en create/update de `QuestController`.
- Anadidos tests de integracion para registro/login, autorizacion y aprobacion idempotente de dibujos.
- Arranque real con MySQL validado.
- Swagger y `/api-docs` validados.
- `API_EXAMPLES.md` anadido.
- Handoff movido al repo real.
- Repo Git accidental de la carpeta padre desactivado.

### 2026-07-23

- Flyway incorporado:
  - dependencias `flyway-core` y `flyway-mysql`.
  - migracion inicial `V1__init_schema.sql`.
  - `spring.sql.init.mode=never`.
  - `spring.flyway.baseline-on-migrate=true`.
  - `spring.jpa.hibernate.ddl-auto=validate`.
  - Flyway desactivado en tests H2 con `ddl-auto=create-drop`.
- Dependencias actualizadas:
  - Spring Boot `3.4.2` a `3.5.16`.
  - Springdoc `2.8.0` a `2.8.17`.
  - JJWT `0.12.6` a `0.13.0`.
  - Lombok `1.18.34` a `1.18.46`.
  - `mysql-connector-j` pasa a version gestionada por el BOM de Spring Boot.

### 2026-07-24

- Logging basico anadido en autenticacion, usuarios, quests, dibujos y errores inesperados.
- Evitados passwords, JWT y payloads completos en logs.
- Configuracion `logging.level.com.drawquest=${DRAWQUEST_LOG_LEVEL:INFO}`.
- `JwtUtil` actualizado a la API actual de JJWT:
  - `Jwts.SIG.HS256`.
  - `verifyWith`.
  - `getPayload`.
  - `SecretKey`.

### 2026-07-25

- Coleccion Postman local anadida:
  - `postman/DrawQuest.postman_collection.json`.
  - `postman/DrawQuest.local.postman_environment.json`.
- Spring Boot Actuator anadido:
  - dependencia `spring-boot-starter-actuator`.
  - expuestos solo `/actuator/health` y `/actuator/info`.
  - endpoints permitidos publicamente en `SecurityConfig`.
  - `management.endpoint.health.show-details=never`.

### 2026-07-26

- Validacion de parametros anadida:
  - `@Validated` en controladores con parametros de ruta.
  - `@Positive` a IDs de ruta (`users`, `quests`, `drawings`, `progress`).
  - `@Positive` a `DrawingCreateDTO.questId`.
  - handler para `ConstraintViolationException` con `400 VALIDATION_ERROR`.
- `Drawing.createdAt` y `Drawing.modifiedAt` pasan a gestionarse en la entidad con `@PrePersist` y `@PreUpdate`.
- Eliminadas asignaciones manuales de timestamps en `DrawingMapper` y `DrawingServiceImpl`.
- Suite verificada con `.\mvnw.cmd test`: `BUILD SUCCESS`, 9 tests ejecutados.

## Lista activa

1. Quitar password hardcodeada de MySQL en `application.properties` y usar `DRAWQUEST_DB_PASSWORD`.
2. Decidir e implementar subida real de imagenes para dibujos.
3. Configurar CORS cuando empiece el frontend.
4. Decidir seeds de quests iniciales.
5. Construir el frontend para consumir el backend y completar la aplicacion.

Docker queda descartado por ahora; para pruebas locales se usa MySQL instalado en el ordenador.

## Comandos utiles

Compilar/testear:

```powershell
cd "C:\Users\victor\Desktop\PROGRAMACION\APPS\drawquest app\drawquest"
.\mvnw.cmd test
```

Arrancar backend:

```powershell
cd "C:\Users\victor\Desktop\PROGRAMACION\APPS\drawquest app\drawquest"
.\mvnw.cmd spring-boot:run
```

Variables esperadas:

```properties
DRAWQUEST_DB_URL=jdbc:mysql://localhost:3306/drawquest_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DRAWQUEST_DB_USERNAME=drawquest_admin
DRAWQUEST_DB_PASSWORD=your_password
DRAWQUEST_JWT_SECRET=base64_secret_with_at_least_256_bits
DRAWQUEST_JWT_EXPIRATION_MS=3600000
DRAWQUEST_LOG_LEVEL=INFO
```

Swagger:

```text
http://localhost:8080/swagger-ui.html
```

Actuator:

```text
http://localhost:8080/actuator/health
http://localhost:8080/actuator/info
```

## Instruccion para una conversacion nueva

Pegar esto:

```text
Estoy trabajando en el proyecto DrawQuest. Lee el archivo PROJECT_HANDOFF.md en la raiz del repo drawquest y continua desde ahi. La base backend ya esta completada. La lista activa empieza por quitar la password hardcodeada, luego implementar subida real de imagenes, CORS, seeds de quests iniciales y frontend.
```
