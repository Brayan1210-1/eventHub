# README CON INSTRUCCIONES DE USO

#  EventHub - APP de Gestión de Eventos


EventHub es un programa para la gestión de lugares, eventos y zonas ,  Implementa seguridad con **JWT**, base de datos con **PostgreSQL** y tests con **Testcontainers**.

---

##  Tecnologías Utilizadas

* **Backend:** Java 21, Spring Boot 4.0.2
* **Seguridad:** Spring Security, JWT 
* **Base de Datos:** PostgreSQL 16
* **Testing:** JUnit 5, Mockito, Testcontainers (PostgreSQL)
* **Despliegue:** Docker 

---

##  Requisitos Previos

Para ejecutar el proyecto debe tener instalado:
* Docker Desktop o docker para gestionar los contenedores
* Java 21 JDK

---

* link de documentación de endpoints con swagger: http://localhost:8081/swagger-ui/index.html

* Realiza los tests con: ./gradlew clean test y puedes mirar la cobertura en jacoco: build>jacoco>test>html> abre el index.html

* levantarlo con docker: una vez que ya tengas docker y el .env listo, ejecuta en la terminal (apuntado a la raíz deproyecto) el siguiente comando:
 docker compose up -d --build
 con esto la app se levanta y está lista para usarse

### . Configuración del Entorno
Crea un archivo .env en la raíz del proyecto basándote en el .env.example:

* EJEMPLO:

```properties
SERVER_PORT=8081

DB_NAME=basedatos
DB_URL=jdbc:postgresql://postgres:5432/basedatos
DB_USERNAME=postgres
DB_PASSWORD=laContraseñaaaaaaaaaaaaa

JWT_SECRET=contraseñadealmenos32caracteres
JWT_ACCESS=900000
JWT_REFRESH=604800000