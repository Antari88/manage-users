# Manage Users -- Spring Boot REST API

Applicazione Spring Boot per la gestione degli utenti con autenticazione
JWT tramite Keycloak.\
Espone API REST protette per operazioni CRUD sugli utenti e un endpoint
per ottenere il token di accesso.

------------------------------------------------------------------------

## 🚀 Tecnologie utilizzate

-   Java 17+
-   Spring Boot
-   Spring Web
-   Spring Data JPA
-   Spring Security (OAuth2 Resource Server -- JWT)
-   Keycloak (Identity Provider)
-   H2 Database (in-memory)
-   WebClient
-   Maven

------------------------------------------------------------------------

## 📁 Struttura del progetto

    src/
     ├── config/
     ├── controller/
     ├── entity/
     ├── repository/
     ├── service/
     ├── mapper/
     ├── model/
     ├── listener/
     └── utils/

------------------------------------------------------------------------

## ⚙️ Configurazione

### Database

    spring.datasource.url=jdbc:h2:mem:testdb
    spring.jpa.hibernate.ddl-auto=none
    spring.sql.init.mode=always

Console H2 disponibile su:

http://localhost:8080/h2-console

------------------------------------------------------------------------

## 🔐 Sicurezza

Configurato come OAuth2 Resource Server con validazione JWT:

    spring.security.oauth2.resourceserver.jwt.issuer-uri=https://idpgw.test4mind.com/realms/demo-interview


------------------------------------------------------------------------

## ▶️ Avvio del progetto

Build:

    mvn clean install

Run:

    mvn spring-boot:run

Applicazione disponibile su:

http://localhost:8080

------------------------------------------------------------------------

## 📌 API REST -- Gestione Utenti

Per testare le chiamate: http://localhost:8080/swagger-ui/index.html

Endpoint login:

    POST /auth/login --> per ottenere Bearer

Header richiesto:

    Authorization: Bearer <access_token>

### Endpoints principali
-   GET /api/users
-   GET /api/users/{id}
-   POST /api/users
-   PUT /api/users/{id}
-   DELETE /api/users/{id}



