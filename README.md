# DrawQuest - Proyecto Backend

**DrawQuest** es una aplicación web que permite a los usuarios participar en misiones de dibujo con una progresión a través de diferentes niveles. Los usuarios pueden completar misiones, enviar sus dibujos y seguir su avance en el juego.

---

## Características Principales

- **Usuarios**: Los usuarios pueden registrarse, iniciar sesión y ver su progreso.
- **Misiones**: El sistema ofrece misiones de dibujo con diferentes niveles de dificultad.
- **Progreso**: Los usuarios pueden ver su progreso y completar misiones.
- **Dibujos**: Los usuarios pueden subir sus dibujos como parte de las misiones completadas.
- **Seguridad**: Implementación de seguridad con JWT (JSON Web Tokens) para el manejo de autenticación.

---

## Tecnologías Utilizadas

- **Spring Boot**: Framework para el backend.
- **Spring Security**: Gestión de seguridad y autenticación de usuarios.
- **JWT (JSON Web Tokens)**: Autenticación basada en tokens.
- **JPA (Java Persistence API)**: Persistencia y acceso a base de datos.
- **MySQL**: Base de datos configurada por defecto para desarrollo local.
- **H2**: Dependencia disponible para desarrollo o pruebas en memoria.
- **Maven**: Herramienta de gestión y construcción de proyectos.

---

## Requisitos

- **Java 17** o superior.
- **Maven** o el wrapper incluido (`mvnw` / `mvnw.cmd`).
- **MySQL** con una base de datos local para DrawQuest.

---

## Configuración del Proyecto

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu_usuario/drawquest.git
cd drawquest
```

### 2. Configurar Variables de Entorno

La aplicación lee la configuración sensible desde variables de entorno:

```properties
DRAWQUEST_DB_URL=jdbc:mysql://localhost:3306/drawquest_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DRAWQUEST_DB_USERNAME=drawquest_admin
DRAWQUEST_DB_PASSWORD=your_password
DRAWQUEST_JWT_SECRET=base64_secret_with_at_least_256_bits
DRAWQUEST_JWT_EXPIRATION_MS=3600000
```

`DRAWQUEST_JWT_SECRET` debe estar en Base64 y tener al menos 256 bits para HS256.

### 3. Compilar y Ejecutar el Proyecto

En Windows:

```bash
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

En macOS/Linux:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

La aplicación se levantará en el puerto **8080** por defecto.

---

## Estructura del Proyecto

```text
src/main/java/com/drawquest/
├── controllers/      # Controladores REST
├── services/         # Lógica de negocio
├── repositories/     # Repositorios JPA
├── models/           # Entidades del modelo
├── dtos/             # Objetos de entrada y salida
├── mappers/          # Conversión entre entidades y DTOs
├── security/         # Configuración JWT y filtros de seguridad
└── DrawquestApplication.java
```

---

## Autenticación y Seguridad

La aplicación utiliza **JWT (JSON Web Tokens)** para manejar la autenticación de los usuarios.

1. **Registro de usuario**: Los usuarios se registran enviando un `POST` a `/auth/register`.
2. **Login**: Los usuarios inician sesión enviando un `POST` a `/auth/login` con su usuario y contraseña. Si la autenticación es exitosa, reciben un JWT.
3. **Autenticación con JWT**: Las solicitudes protegidas requieren el token JWT en el encabezado `Authorization` con el formato `Bearer token`.

---

## Rutas API

| Método | Ruta              | Descripción                     |
|--------|-------------------|---------------------------------|
| POST   | `/auth/register`  | Registro de nuevo usuario       |
| POST   | `/auth/login`     | Login y obtención del JWT       |
| GET    | `/quests`         | Listar todas las misiones       |
| GET    | `/quests/{id}`    | Ver detalles de una misión      |
| POST   | `/drawings`       | Subir un dibujo                 |
| GET    | `/progress`       | Ver el progreso de las misiones |
| POST   | `/progress`       | Crear progreso                  |

---

## Notas de Desarrollo

- La autenticación se maneja con **Spring Security** y **JWT**.
- La documentación Swagger está disponible en `/swagger-ui.html`.
- La inicialización SQL usa `src/main/resources/schema.sql`.
- La aplicación está diseñada para ampliarse con nuevas misiones, progreso y roles.

---

## Próximos Pasos

1. Corregir la validación de contraseña en el login.
2. Asignar un rol por defecto al registrar usuarios.
3. Tomar el usuario autenticado desde el JWT en endpoints protegidos.
4. Añadir tests de autenticación, validaciones y endpoints protegidos.

---

## Colaboradores

- **barbercheff** - Desarrollador principal
