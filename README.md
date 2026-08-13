# Sistema de Reservas de Hotel

Este proyecto corresponde a la reestructuración arquitectónica del backend del sistema de reservas de hotel, adaptando un diseño monolítico anterior a un esquema robusto basado en **Arquitectura Limpia (Clean Architecture)** y **Diseño Guiado por el Dominio (DDD)** en Java puro.

---

## 🏛️ Estructura y Mapa de Paquetes

El proyecto se encuentra estrictamente segmentado en tres capas base para garantizar que el núcleo del negocio permanezca 100% inmune a acoplamientos tecnológicos externos:

```text
com.hotel
│
├── domain                  <-- Capa de Dominio (Cero frameworks, Java Puro)
│   ├── entity              <-- Entidades con identidad única y lógica rica (Room, Reservation)
│   ├── valueobject         <-- Objetos de Valor inmutables auto-validantes (RoomNumber, Price, Nights, GuestName)
│   ├── exception           <-- Excepciones de negocio personalizadas
│   └── repository          <-- Contratos puros de interfaces (RoomRepository, ReservationRepository, EmailNotificationService)
│
├── application             <-- Capa de Aplicación (Casos de Uso)
│   └── usecase             <-- Clases enfocadas en flujos de negocio únicos e inyección de constructor
│                               (MakeReservationUseCase, CancelReservationUseCase, GetReservationDetailsUseCase, HotelService)
│
└── infrastructure          <-- Capa de Infraestructura (Persistencia, adaptadores técnicos)
    └── persistence         <-- Implementaciones en memoria y adaptadores de consola
                                (InMemoryRoomRepository, InMemoryReservationRepository, ConsoleEmailNotificationAdapter)
```

### Reglas de Dependencia Cumplidas
- **Flujo de Dependencia Estricto:** Las capas externas (`infrastructure` -> `application` -> `domain`) conocen e importan a las internas, pero las internas jamás tienen conocimiento ni referencias directas sobre las capas externas.
- **Sin Contaminación de Frameworks:** No existen importaciones de Spring (@Service, @Repository), Jackson o JPA (@Entity) en el dominio ni en la aplicación. Todo es Java puro.
- **Inyección por Constructor:** Los Casos de Uso dependen exclusivamente de las abstracciones (interfaces contrato) inyectadas en su constructor, cumpliendo el principio de inversión de dependencias.

---

## ⚙️ Instrucciones de Compilación y Pruebas

El proyecto gestiona la calidad y cobertura mediante **Maven** y **JaCoCo**, exigiendo un **100% de cobertura de líneas y ramas** para asegurar la total robustez del código.

### 1. Compilar y Verificar el Proyecto
Para limpiar los directorios temporales, descargar dependencias y compilar todo el código:
```bash
mvn clean compile
```

### 2. Ejecutar la Suite de Pruebas Unitarias
Para correr la suite de pruebas unitarias completa y verificar que la cobertura cumpla con el estándar del 100%:
```bash
mvn test
```

### 3. Ejecutar la Aplicación en Consola (Manual)
Para ejecutar la simulación en consola y ver el flujo completo de reserva, excepciones y cancelación:
```bash
mvn exec:java -Dexec.mainClass="com.hotel.Main"
```
