# Sistema de Control de Inventarios - Grupo Castores

## Descripción

Aplicación web desarrollada como prueba técnica para la gestión de inventarios.

---

## Tecnologías Utilizadas

### Backend
- Java 17
- Spring Boot 4.1.0
- Spring Data JPA
- Hibernate 7.4.1.Final
- Maven

### Frontend
- Thymeleaf
- Bootstrap 5.3

### Base de Datos
- MySQL 9.6

### Control de Versiones
- Git
- GitHub

---

## IDE Utilizado

- IntelliJ IDEA Community Edition 2026

---

## Versión del Lenguaje

- Java 17

## Ejecucion

- Agregar estas propiedades al application.properties

spring.application.name=inventario
spring.datasource.url=jdbc:mysql://localhost:3306/name_data_base
spring.datasource.username=user
spring.datasource.password=password
spring.jpa.show-sql=true
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

## Compilar el proyecto

- mvn clean install
  
## Ejecutar 
- Ejecutar la clase principal o
- mvn spring-boot:run

## URL 
- http://localhost:8080/login
