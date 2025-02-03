
# DrawQuest - Proyecto Backend

**DrawQuest** es una aplicación web que permite a los usuarios participar en misiones de dibujo con una progresión a través de diferentes niveles. Los usuarios pueden completar misiones, enviar sus dibujos y seguir su avance en el juego.

---

## 🚀 **Características Principales**

- **Usuarios**: Los usuarios pueden registrarse, iniciar sesión y ver su progreso.
- **Misiones**: El sistema ofrece misiones de dibujo con diferentes niveles de dificultad.
- **Progreso**: Los usuarios pueden ver su progreso y completar misiones.
- **Dibujos**: Los usuarios pueden subir sus dibujos como parte de las misiones completadas.
- **Seguridad**: Implementación de seguridad con JWT (JSON Web Tokens) para el manejo de autenticación.

---

## 📦 **Tecnologías Utilizadas**

- **Spring Boot**: Framework para el backend.
- **Spring Security**: Para la gestión de seguridad y autenticación de usuarios.
- **JWT (JSON Web Tokens)**: Para la autenticación basada en tokens.
- **JPA (Java Persistence API)**: Para interactuar con la base de datos.
- **H2**: Base de datos en memoria para el desarrollo.
- **PostgreSQL**: Base de datos real (configurada para la producción).
- **Maven**: Herramienta de gestión y construcción de proyectos.

---

## 📥 **Requisitos**

- **Java 17** o superior
- **Maven** (Para la construcción del proyecto)
- **PostgreSQL** (Configurado para producción)

---

## 🛠 **Configuración del Proyecto**

### 1. **Clonar el Repositorio**

```bash
git clone https://github.com/tu_usuario/drawquest.git
cd drawquest
```

### 2. **Compilar y Ejecutar el Proyecto**

Si usas Maven, puedes compilar y ejecutar el proyecto de la siguiente forma:

```bash
mvn clean install
mvn spring-boot:run
```

Esto levantará la aplicación en el puerto **8080** por defecto.

---

## 🏗️ **Estructura del Proyecto**

```
src/main/java/com/drawquest/
│── controller/       # Controladores REST
│── service/          # Lógica de negocio
│── repository/       # Repositorios JPA
│── model/            # Entidades del modelo
│── DrawquestApplication.java  # Clase principal de la aplicación
```

---

## 🔒 **Autenticación y Seguridad**

La aplicación utiliza **JWT (JSON Web Tokens)** para manejar la autenticación de los usuarios.

1. **Registro de usuario**: Los usuarios se registran enviando un POST a `/auth/register`.
2. **Login**: Los usuarios inician sesión enviando un POST a `/auth/login` con su correo y contraseña. Si la autenticación es exitosa, reciben un JWT que debe ser utilizado para las siguientes peticiones.
3. **Autenticación con JWT**: Todas las solicitudes protegidas requieren el token JWT en el encabezado **Authorization** como `Bearer token`.

---

## 🗄 **Configuración de la Base de Datos**

**PostgreSQL** está configurado para producción. Asegúrate de tener una instancia corriendo en tu entorno local o en un servidor.

Para conectar la base de datos, edita las propiedades en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/drawquest
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

---

## 📚 **Rutas API**

| Método | Ruta                      | Descripción                     |
|--------|---------------------------|---------------------------------|
| POST   | /auth/register             | Registro de nuevo usuario       |
| POST   | /auth/login                | Login y obtención del JWT       |
| GET    | /quests                    | Listar todas las misiones       |
| GET    | /quests/{id}               | Ver detalles de una misión      |
| POST   | /drawings                  | Subir un dibujo                 |
| GET    | /progress                  | Ver el progreso de las misiones |
| POST   | /progress/{questId}        | Completar una misión            |

---

## 📝 **Notas de Desarrollo**

- En desarrollo, estamos utilizando **H2** como base de datos en memoria. Asegúrate de cambiar la configuración de la base de datos a **PostgreSQL** en el entorno de producción.
- La autenticación se maneja con **Spring Security** y **JWT**.
- La aplicación está diseñada para escalar y puede ampliarse con nuevas características fácilmente.

---

## 📅 **Próximos Pasos**

1. Implementar más misiones y características del juego.
2. Mejorar la interfaz de usuario (UI).
3. Optimizar la seguridad y agregar roles de usuario (admin, usuario, etc.).

---

## 👥 **Colaboradores**

- **barbercheff** - Desarrollador principal

---
