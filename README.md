# 🏨 Hotel Reservation API — Full-Stack Integration

Microservicio de gestión de reservas de hotel construido con **Spring Boot**, **PostgreSQL**, **Docker** y documentado con **OpenAPI/Swagger**. Implementado bajo los principios de **Arquitectura Limpia (Clean Architecture)** y **Diseño Guiado por el Dominio (DDD)**.

---

## 🛠 Stack Tecnológico

| Componente | Tecnología |
|------------|------------|
| Framework | Spring Boot 3.2.x |
| Lenguaje | Java 21 |
| Base de datos | PostgreSQL 16 |
| ORM | Spring Data JPA |
| Containerización | Docker Compose |
| Documentación | Springdoc OpenAPI (Swagger) |
| Pruebas | JUnit 5 + Mockito |
| Cobertura | JaCoCo |
| Build Tool | Maven |
| Frontend | TypeScript Vanilla, Vite |

---

## 🔗 Repositorios de Referencia

- **Core de Dominio / Hito 1**: Este repositorio
- **Frontend Vite + TS / Hito 2**: [hito2/Hotel](../Hito%202/Hotel/)

---

## 🚀 Guía de Puesta en Marcha Local

### 1. Levantar la Base de Datos Relacional

```bash
docker compose up -d
```

### 2. Ejecutar Pruebas Automatizadas

```bash
mvn clean test
```

### 3. Iniciar el Microservicio Backend

```bash
mvn spring-boot:run
```

- **API REST**: http://localhost:8080/api/v1
- **Swagger UI** (Perfil Dev): http://localhost:8080/swagger-ui.html

### 4. Iniciar la Interfaz Web Frontend

```bash
cd ../Hito\ 2/Hotel/
npm install
npm run dev
```

- **App Web**: http://localhost:5173

---

## 🏛️ Arquitectura

El proyecto sigue estrictamente **Arquitecturaura Limpia** con separación de responsabilidades:

```text
com.hotel
│
├── domain                          ← Capa de Dominio (Cero frameworks, Java Puro)
│   ├── entity                      ← Entidades con identidad única y lógica rica
│   ├── valueobject                 ← Objetos de Valor inmutables auto-validantes
│   ├── exception                   ← Excepciones de negocio personalizadas
│   └── repository                  ← Contratos puros de interfaces
│
├── application                     ← Capa de Aplicación (Casos de Uso)
│   └── usecase                     ← Casos de uso con inyección por constructor
│
└── infrastructure                  ← Capa de Infraestructura (Tecnología)
    ├── persistence                 ← Implementaciones de persistencia
    └── web                         ← Capa Web (API REST)
        ├── controller              ← Controladores REST
        ├── dto                     ← Data Transfer Objects
        └── config                  ← Configuraciones (CORS, OpenAPI)
```

### Reglas de Dependencia

- **Flujo estricto**: `infrastructure → application → domain`
- **Dominio puro**: Cero anotaciones de Spring, JPA o frameworks externos
- **Inversión de dependencias**: El dominio define interfaces, la infraestructura las implementa

---

## 📚 Documentación de la API

### Swagger-UI (Interactivo)

```
http://localhost:8080/swagger-ui.html
```

### Endpoints Disponibles

| Método | Ruta | Descripción | Código Éxito |
|--------|------|-------------|--------------|
| `GET` | `/api/v1/rooms` | Listar todas las habitaciones | 200 OK |
| `GET` | `/api/v1/rooms/{roomNumber}` | Obtener habitación por número | 200 OK |
| `POST` | `/api/v1/rooms` | Crear nueva habitación | 201 Created |
| `GET` | `/api/v1/reservations` | Listar todas las reservas | 200 OK |
| `GET` | `/api/v1/reservations/{id}` | Obtener reserva por ID | 200 OK |
| `POST` | `/api/v1/reservations` | Crear nueva reserva | 201 Created |
| `DELETE` | `/api/v1/reservations/{id}` | Cancelar reserva | 200 OK |
| `PATCH` | `/api/v1/reservations/{id}` | Actualizar estado de reserva | 200 OK |

### Códigos de Error

| Código HTTP | Significado | Ejemplo |
|-------------|-------------|---------|
| 400 | Solicitud inválida | Datos faltantes o formato incorrecto |
| 404 | Recurso no encontrado | Habitación o reserva inexistente |
| 422 | Regla de negocio violada | Habitación no disponible |
| 500 | Error interno | Error no controlado (sin stacktrace) |

---

## 🧪 Ejecución de Pruebas

```bash
# Todas las pruebas
mvn clean test

# Pruebas con cobertura
mvn test jacoco:report

# Ver reporte
open target/site/jacoco/index.html
```

### Cobertura Mínima Requerida

| Tipo | Mínimo |
|------|--------|
| Líneas | 80% |
| Ramas | 80% |

---

## 🛡️ Seguridad

- **Swagger bloqueado en producción**: No se expone documentación técnica fuera de desarrollo
- **Sin stacktraces**: Los errores 500 nunca muestran detalles internos
- **Validación de entrada**: Todos los endpoints validan datos con Bean Validation
- **Manejo centralizado**: Todas las excepciones pasan por `GlobalExceptionHandler`
- **CORS configurado**: Permite peticiones desde el frontend Vite (localhost:5173)

---

## 🔧 Configuración por Perfiles

### Perfil `dev` (Desarrollo)

- PostgreSQL local en `localhost:5432`
- Swagger habilitado en `/swagger-ui.html`
- SQL logging habilitado
- `ddl-auto: update` (auto-actualización de esquema)

### Perfil `prod` (Producción)

- Variables de entorno para BD (`DB_URL`, `DB_USER`, `DB_PASSWORD`)
- Swagger **DESHABILITADO** (404 en intentos de acceso)
- `ddl-auto: validate` (validación sin modificaciones)
- Logging reducido

---

## ✅ Checklist de Autorrevisión

| # | Validación | Comando / Verificación |
|---|-----------|------------------------|
| 1 | Pruebas Unitarias 100% pasando | `mvn clean test` |
| 2 | Cero Frameworks en Dominio | Ningún @Entity/@Table en `domain/` |
| 3 | Persistencia Real | `docker compose up -d` → BD activa en :5432 |
| 4 | CORS Resuelto | `@CrossOrigin` + `CorsConfig` en backend |
| 5 | Cero `any` en Frontend | `pnpm build` sin errores TS |
| 6 | Ciclo Completo | Formulario → PostgreSQL → UI |
| 7 | Exclusión de Secretos | Cero contraseñas de producción en Git |
| 8 | Swagger Aislado | Swagger bloqueado en prod, activo en dev |

---

## 👥 Equipo de Desarrollo

Proyecto desarrollado como parte del bootcamp de **Desafío Latam**.
