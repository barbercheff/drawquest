# DrawQuest - resumen para continuar trabajo

Fecha de referencia: 2026-07-11.

## Avance aplicado el 2026-07-11

Primer bloque de backend base completado:

- Corregido `DrawingMapper`: `createdAt` y `modifiedAt` ya no salen invertidos en `DrawingResponseDTO`.
- Movida la codificacion BCrypt de passwords a `UserServiceImpl.createUser`.
- Eliminada la codificacion directa en `AuthController.register`.
- Restringido `POST /users` a `ROLE_ADMIN`.
- Restringido CRUD de quests:
  - `GET /quests/**`: autenticado
  - `POST /quests`: `ROLE_ADMIN` o `ROLE_MODERATOR`
  - `PUT /quests/*`: `ROLE_ADMIN` o `ROLE_MODERATOR`
  - `DELETE /quests/*`: `ROLE_ADMIN`
- Anadido `@Valid` en create/update de `QuestController`.
- Anadido `src/test/java/com/drawquest/DrawquestIntegrationTest.java` con tests de integracion para registro/login, autorizacion y aprobacion idempotente de dibujos.

Verificacion ejecutada:

```powershell
cd "C:\Users\victor\Desktop\PROGRAMACIÓN\APPS\drawquest app\drawquest"
.\mvnw.cmd test
```

Resultado observado: `BUILD SUCCESS`, 3 tests ejecutados.

Handoff movido al repo real (`drawquest/PROJECT_HANDOFF.md`) y commiteado. El repo Git accidental de la carpeta padre `drawquest app` se desactivo renombrando su `.git` a `.git.disabled-parent-repo`; el repo activo debe seguir siendo siempre `drawquest`.

## Contexto general

Proyecto ubicado en:

`C:\Users\victor\Desktop\PROGRAMACION\APPS\drawquest app`

La app principal esta dentro de:

`drawquest`

Es un backend Java/Spring Boot para una app llamada DrawQuest. La idea es que usuarios participen en misiones de dibujo, suban dibujos, reciban aprobacion y progresen con XP/niveles.

## Stack detectado

- Java 17
- Spring Boot 3.4.2
- Maven wrapper incluido (`mvnw.cmd`)
- Spring Web
- Spring Security
- Spring Data JPA
- JWT con `jjwt`
- MySQL por defecto
- H2 disponible como dependencia
- Lombok
- Swagger/OpenAPI con springdoc

## Estado actual del backend

La estructura esta bastante completa:

- `controllers`: controladores REST
- `services` y `services/impl`: logica de negocio
- `repositories`: repositorios JPA
- `models`: entidades
- `dtos`: DTOs de entrada/salida
- `mappers`: conversion entidad/DTO
- `security`: JWT y filtro de autenticacion
- `config/SecurityConfig.java`: reglas de seguridad
- `resources/schema.sql`: creacion de tablas y roles base

La build pasa con:

```powershell
cd "C:\Users\victor\Desktop\PROGRAMACION\APPS\drawquest app\drawquest"
.\mvnw.cmd test
```

Resultado observado: `BUILD SUCCESS`.

Importante: no hay tests reales en `src/test`; Maven pasa porque no tiene pruebas que ejecutar.

## Funcionalidad implementada

### Autenticacion

- `POST /auth/register`
- `POST /auth/login`
- JWT en header `Authorization: Bearer <token>`
- Passwords en `/auth/register` se codifican con BCrypt.

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

### Logica de progreso

- Al crear un dibujo se crea o recupera el progreso de usuario+quest y se incrementan los intentos.
- Al aprobar un dibujo:
  - el dibujo queda aprobado
  - el progreso se marca como completado si no lo estaba
  - se suma XP al usuario
  - se recalcula nivel con `xp / 100`
  - no se vuelve a sumar XP si esa quest ya estaba completada

## Riesgos y cosas a medias

1. Bug en fechas de `DrawingMapper`

Archivo:

`drawquest/src/main/java/com/drawquest/mappers/DrawingMapper.java`

El constructor de `DrawingResponseDTO` espera:

```java
id, userId, questId, imageUrl, approved, createdAt, modifiedAt
```

Pero el mapper pasa:

```java
drawing.getModifiedAt(),
drawing.getCreatedAt()
```

Estan invertidas.

2. `POST /users` puede guardar passwords sin hash

Archivo:

`drawquest/src/main/java/com/drawquest/controllers/UserController.java`

`/auth/register` codifica la password antes de llamar a `userService.createUser`, pero `POST /users` llama directamente a `userService.createUser` sin codificar.

Solucion recomendada: mover el hash de password al `UserService`, no al controlador. Asi cualquier creacion de usuario queda protegida.

3. Seguridad incompleta en quests

Archivo:

`drawquest/src/main/java/com/drawquest/config/SecurityConfig.java`

Actualmente solo hay reglas explicitas para:

- `GET /users`: admin
- `PUT /drawings/*/approve`: admin o moderator

El resto queda como `authenticated`.

Eso significa que cualquier usuario autenticado puede crear, editar o borrar quests. Recomendado:

- `GET /quests/**`: usuario autenticado
- `POST /quests`: admin/moderator
- `PUT /quests/{id}`: admin/moderator
- `DELETE /quests/{id}`: admin

4. Falta `@Valid` en `QuestController`

Archivo:

`drawquest/src/main/java/com/drawquest/controllers/QuestController.java`

Los DTOs de quest tienen validaciones, pero create/update no usan `@Valid`, asi que esas validaciones no se aplican.

5. No hay tests

Crear tests minimos para:

- registro guarda password hasheada
- login devuelve JWT
- usuario normal no puede listar usuarios
- usuario normal no puede aprobar dibujos
- usuario normal no puede crear/editar/borrar quests si se restringe
- aprobar dibujo completa progreso y suma XP solo una vez

6. Estado Git peculiar

En el repo raiz, `git status --short` mostraba:

```text
 M drawquest
?? drawquest.zip
```

Dentro de `drawquest`, `git status --short` no mostraba cambios.

El repo raiz parece tratar `drawquest` como submodulo o repo anidado, pero `git submodule status` falla porque no hay `.gitmodules`.

Pendiente decidir:

- si `drawquest` debe ser submodulo real
- si debe ser carpeta normal
- que hacer con `drawquest.zip`

## Lista priorizada para continuar

1. Corregir `DrawingMapper` para no invertir `createdAt` y `modifiedAt`.
2. Mover codificacion BCrypt de password a `UserServiceImpl.createUser`.
3. Eliminar, restringir o corregir `POST /users`.
4. Proteger CRUD de quests por rol en `SecurityConfig`.
5. Añadir `@Valid` en `QuestController`.
6. Añadir tests de autenticacion/autorizacion/progreso.
7. Probar arranque real con MySQL y variables de entorno.
8. Revisar estructura Git del repo raiz y `drawquest.zip`.
9. Documentar ejemplos practicos de API.
10. Decidir siguiente bloque funcional: subida real de imagenes o frontend.

## Comandos utiles

Nota 2026-07-11: los puntos 1 a 6 de la lista anterior ya estan completados. Tambien se movio y commiteo este handoff dentro del repo real, y se desactivo el repo Git accidental de la carpeta padre. La lista activa es:

1. Ampliar tests de autorizacion/validacion para casos positivos y negativos restantes.
2. Probar arranque real con MySQL y variables de entorno.
3. Documentar ejemplos practicos de API.
4. Decidir siguiente bloque funcional: subida real de imagenes o frontend.

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
```

Swagger:

```text
http://localhost:8080/swagger-ui.html
```

## Instruccion para una conversacion nueva

Pegar esto:

```text
Estoy trabajando en el proyecto DrawQuest. Lee el archivo PROJECT_HANDOFF.md en la raiz del workspace y continua desde ahi. Quiero seguir con la lista priorizada: primero corregir backend base, luego tests, luego decidir frontend/subida real de imagenes.
```
