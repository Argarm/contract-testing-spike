# Contract Testing Portfolio

Scaffold inicial de un portfolio de contract testing con Kotlin, Spring Boot,
Gradle y JUnit 5.

Actualmente solo está implementado `catalog-service`. Pact, Docker Compose,
`checkout-service` y GitHub Actions se añadirán en pasos posteriores.

## Requisitos

- JDK 21
- PowerShell, macOS/Linux shell o un IDE compatible con Gradle

El proyecto usa Gradle Wrapper, por lo que no necesitas instalar Gradle
globalmente.

## Arrancar el servicio

Desde la raíz del proyecto:

```bash
./gradlew :catalog-service:bootRun
```

En Windows PowerShell:

```powershell
.\gradlew.bat :catalog-service:bootRun
```

El servicio escucha en `http://localhost:8081`.

## Probar el endpoint

```bash
curl http://localhost:8081/products/p-100
```

Respuesta esperada:

```json
{
  "id": "p-100",
  "name": "Mechanical Keyboard",
  "price": 89.99,
  "available": true
}
```

Producto no existente:

```bash
curl -i http://localhost:8081/products/unknown
```

Devuelve `404 Not Found`.

## Ejecutar tests

```bash
./gradlew test
```

Los tests cubren el repositorio en memoria y el contrato HTTP del controlador
con `MockMvc`. Todavía no prueban compatibilidad entre servicios: eso será el
objetivo de los tests Pact.

## Docker Compose y CI

Arrancar ambos servicios localmente:

```powershell
docker compose up --build
```

Comprobar los endpoints:

```powershell
curl.exe http://localhost:8081/products/p-100
curl.exe http://localhost:8082/health
```

Parar y eliminar los contenedores:

```powershell
docker compose down --volumes
```

Los comandos locales equivalentes al workflow son:

```powershell
$env:JAVA_HOME="C:\tmp\temurin21\jdk-21.0.11+10"
.\gradlew.bat :checkout-service:test --no-daemon
.\gradlew.bat :catalog-service:test --no-daemon
.\gradlew.bat test --no-daemon
docker compose up --build -d
curl.exe --fail http://localhost:8081/products/p-100
curl.exe --fail http://localhost:8082/health
docker compose down --volumes
```

El test de `catalog-service` incluye la verificación del contrato Pact generado por
`checkout-service`. El workflow ejecuta primero los tests de contrato y después un
smoke test de los dos contenedores.