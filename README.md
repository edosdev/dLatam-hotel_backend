# 🏨 Hotel Reservation API

Microservicio de gestión de reservas de hotel construido con **Spring Boot**, **PostgreSQL**, **Docker** y documentado con **OpenAPI/Swagger**. Implementado bajo los principios de **Arquitectura Limpia (Clean Architecture)** y **Diseño Guiado por el Dominio (DDD)**.

---

## 📋 Descripción del Sistema

El **Hotel Reservation API** es un backend robusto que permite gestionar habitaciones y reservas de hotel a través de una API REST semántica. El sistema garantiza:

- **Persistencia real** en base de datos relacional PostgreSQL
- **Contratos seguros** documentados con OpenAPI/Swagger-UI
- **Manejo centralizado de errores** con códigos HTTP semánticos
- **Aislamiento por perfiles** (desarrollo/producción)
- **Cobertura de código** al 100% con JaCoCo

### 🛠️ Stack Tecnológica

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

---

## 🏛️ Arquitectura

El proyecto sigue estrictamente **Arquitectura Limpia** con separación de responsabilidades en tres capas principales:

```text
com.hotel
│
├── domain                          ← Capa de Dominio (Cero frameworks, Java Puro)
│   ├── entity                      ← Entidades con identidad única y lógica rica
│   │   ├── Room                    ← Entidad de habitación con comportamiento
│   │   └── Reservation             ← Entidad de reserva con comportamiento
│   ├── valueobject                 ← Objetos de Valor inmutables auto-validantes
│   │   ├── RoomNumber              ← Número de habitación
│   │   ├── RoomType                ← Tipo de habitación (SINGLE, DOUBLE, SUITE)
│   │   ├── GuestName               ← Nombre del huésped
│   │   ├── Nights                  ← Número de noches
│   │   ├── Price                   ← Precio por noche
│   │   └── ReservationStatus       ← Estado (CONFIRMED, CANCELLED)
│   ├── exception                   ← Excepciones de negocio personalizadas
│   │   ├── RoomNotFoundException
│   │   ├── RoomNotAvailableException
│   │   ├── ReservationNotFoundException
│   │   └── InvalidReservationException
│   └── repository                  ← Contratos puros de interfaces
│       ├── RoomRepository          ← Interfaz de persistencia de habitaciones
│       ├── ReservationRepository   ← Interfaz de persistencia de reservas
│       └── EmailNotificationService ← Interfaz de notificaciones
│
├── application                     ← Capa de Aplicación (Casos de Uso)
│   └── usecase                     ← Casos de uso con inyección por constructor
│       ├── MakeReservationUseCase  ← Crear reserva
│       ├── CancelReservationUseCase ← Cancelar reserva
│       ├── GetReservationDetailsUseCase ← Consultar reserva
│       └── HotelService            ← Fachada que orquesta los casos de uso
│
└── infrastructure                  ← Capa de Infraestructura (Tecnología)
    ├── persistence                 ← Implementaciones de persistencia
    │   ├── entity                  ← Entidades JPA (@Entity)
    │   │   ├── RoomEntity
    │   │   └── ReservationEntity
    │   ├── repository              ← Repositorios Spring Data JPA
    │   │   ├── RoomJpaRepository
    │   │   └── ReservationJpaRepository
    │   ├── JpaRoomRepository       ← Adaptador JPA → Dominio
    │   ├── JpaReservationRepository ← Adaptador JPA → Dominio
    │   ├── InMemoryRoomRepository  ← Implementación en memoria (testing)
    │   ├── InMemoryReservationRepository ← Implementación en memoria (testing)
    │   └── ConsoleEmailNotificationAdapter ← Adaptador de consola
    └── web                         ← Capa Web (API REST)
        ├── controller              ← Controladores REST
        │   ├── RoomController      ← Endpoints de habitaciones
        │   ├── ReservationController ← Endpoints de reservas
        │   └── GlobalExceptionHandler ← Manejador global de errores
        ├── dto                     ← Data Transfer Objects
        │   ├── RoomRequest
        │   ├── RoomResponse
        │   ├── ReservationRequest
        │   ├── ReservationResponse
        │   └── ErrorResponse
        └── config                  ← Configuraciones
            └── SwaggerConfig       ← Configuración de OpenAPI
```

### Reglas de Dependencia

- **Flujo estricto**: `infrastructure → application → domain`
- **Dominio puro**: Cero anotaciones de Spring, JPA o frameworks externos
- **Inversión de dependencias**: El dominio define interfaces, la infraestructura las implementa

---

## 🚀 Instrucciones de Levantamiento

### 1. Levantar la Base de Datos (Docker)

```bash
# Levantar PostgreSQL en Docker
docker compose up -d

# Verificar que el contenedor está corriendo
docker compose ps
```

**Resultado esperado:**
```
NAME                IMAGE               STATUS              PORTS
hotel-postgres-db   postgres:16-alpine  Up X minutes        0.0.0.0:5432->5432/tcp
```

### 2. Ejecutar la Aplicación en Modo Desarrollo

```bash
# Compilar el proyecto
./mvnw clean compile

# Ejecutar con perfil de desarrollo
./mvnw spring-boot:run
```

**O usando Maven directamente:**
```bash
mvn clean compile
mvn spring-boot:run
```

### 3. Verificar que Funciona

```bash
# Probar endpoint de habitaciones
curl http://localhost:8080/api/v1/rooms/101

# Probar creación de reserva
curl -X POST http://localhost:8080/api/v1/reservations \
  -H "Content-Type: application/json" \
  -d '{
    "reservationId": "RES-001",
    "guestName": "María Gómez",
    "roomNumber": "101",
    "nights": 3
  }'
```

---

## 📚 Documentación de la API

### Swagger-UI (Interactivo)

Accede a la consola interactiva de Swagger para probar los endpoints:

```
http://localhost:8080/swagger-ui.html
```

**Características:**
- ✅ Botón "Try it out" habilitado para pruebas interactivas
- ✅ Documentación completa de todos los endpoints
- ✅ Esquemas de DTOs con ejemplos
- ✅ Códigos de respuesta HTTP

### OpenAPI JSON (Especificación Técnica)

Descarga la especificación OpenAPI en formato JSON:

```
http://localhost:8080/api-docs
```

### Endpoints Disponibles

| Método | Ruta | Descripción | Código Éxito |
|--------|------|-------------|--------------|
| `GET` | `/api/v1/rooms/{roomNumber}` | Obtener habitación por número | 200 OK |
| `POST` | `/api/v1/rooms` | Crear nueva habitación | 201 Created |
| `GET` | `/api/v1/reservations/{id}` | Obtener reserva por ID | 200 OK |
| `POST` | `/api/v1/reservations` | Crear nueva reserva | 201 Created |
| `DELETE` | `/api/v1/reservations/{id}` | Cancelar reserva | 200 OK |

### Códigos de Error

| Código HTTP | Significado | Ejemplo |
|-------------|-------------|---------|
| 400 | Solicitud inválida | Datos faltantes o formato incorrecto |
| 404 | Recurso no encontrado | Habitación o reserva inexistente |
| 422 | Regla de negocio violada | Habitación no disponible |
| 500 | Error interno | Error no controlado (sin stacktrace) |

---

## 🧪 Ejecución de Pruebas

### Ejecutar todas las pruebas

```bash
./mvnw test
```

### Ejecutar pruebas con cobertura

```bash
./mvnw test jacoco:report
```

### Ver reporte de cobertura

```bash
# Abrir reporte HTML
open target/site/jacoco/index.html
```

### Cobertura Mínima Requerida

| Tipo | Mínimo |
|------|--------|
| Líneas | 80% |
| Ramas | 80% |

**Nota**: Las clases de infraestructura (controllers, JPA entities, repositories) están excluidas del chequeo de cobertura ya que son código generado por frameworks.

---

## 📁 Estructura de Archivos

```text
hotel-reservation-api/
├── docker-compose.yml              ← Orquestación Docker
├── pom.xml                         ← Configuración Maven
├── README.md                       ← Este archivo
├── src/
│   ├── main/
│   │   ├── java/com/hotel/
│   │   │   ├── Main.java           ← Punto de entrada Spring Boot
│   │   │   ├── domain/             ← Dominio puro (sin frameworks)
│   │   │   ├── application/        ← Casos de uso
│   │   │   └── infrastructure/     ← Implementaciones técnicas
│   │   └── resources/
│   │       └── application.yml     ← Configuración por perfiles
│   └── test/
│       └── java/com/hotel/         ← Pruebas unitarias e integración
└── target/                         ← Compilación Maven
```

---

## 🔧 Configuración por Perfiles

### Perfil `dev` (Desarrollo)

- PostgreSQL local en `localhost:5432`
- Swagger habilitado en `/swagger-ui.html`
- SQL logging habilitado
- `ddl-auto: update` (auto-actualización de esquema)

### Perfil `prod` (Producción)

- Variables de entorno para BD (`DB_USERNAME`, `DB_PASSWORD`)
- Swagger **DESHABILITADO** (404 en intentos de acceso)
- `ddl-auto: validate` (validación sin modificaciones)
- Logging reducido

---

## 🛡️ Seguridad

- **Swagger bloqueado en producción**: No se expone documentación técnica fuera de desarrollo
- **Sin stacktraces**: Los errores 500 nunca muestran detalles internos
- **Validación de entrada**: Todos los endpoints validan datos con Bean Validation
- **Manejo centralizado**: Todas las excepciones pasan por `GlobalExceptionHandler`

---

## 👥 Equipo de Desarrollo

Proyecto desarrollado como parte del-bootcamp de **Latam Digital House**.

---

## 📄 Licencia

Este proyecto está bajo la licencia Apache 2.0. Ver [LICENSE](LICENSE) para más detalles.
