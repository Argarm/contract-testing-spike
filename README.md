# Contract Testing Portfolio

Spike de contract testing consumer-driven con Kotlin, Spring Boot, Gradle, JUnit 5 y Pact.

Actualmente incluye:

- `catalog-service`, proveedor REST de productos en memoria.
- `checkout-service`, cliente Pact del catalogo y operacion `POST /checkout`.
- Contrato generado por el consumidor y verificado por el proveedor.
- Docker Compose y workflow de GitHub Actions.

La operacion `POST /checkout` consulta el catalogo y calcula el total en memoria.
Todavia no se usa Pact Broker: el contrato se comparte como archivo versionado para mantener el
alcance del spike pequeno y ejecutable localmente.

## Requisitos

- JDK 21
- Docker Desktop con el daemon Linux activo para Compose
- PowerShell, macOS/Linux shell o un IDE compatible con Gradle

El proyecto usa Gradle Wrapper, por lo que no necesitas instalar Gradle globalmente.

## Arrancar catalog-service

```bash
./gradlew :catalog-service:bootRun
```

En Windows PowerShell:

```powershell
.\gradlew.bat :catalog-service:bootRun
```

El servicio escucha en `http://localhost:8081`.

## Probar catalog-service

```bash
curl http://localhost:8081/products/p-100
curl -i http://localhost:8081/products/unknown
```

## Ejecutar tests

```bash
./gradlew test
```

El test consumidor define la expectativa HTTP y genera el contrato en:

```text
contracts/pacts/checkout-service-catalog-service.json
```

El test proveedor carga ese JSON, arranca `catalog-service` y verifica la interaccion contra el
endpoint real. La demostracion de una incompatibilidad esta en:

```powershell
.\scripts\demo-incompatible-pact.ps1
```

El script cambia temporalmente `available` por `isAvailable`, exige que la verificacion falle y
restaura el contrato original.

## Docker Compose y CI

Arrancar ambos servicios localmente:

```powershell
docker compose up --build -d
```

Comprobar catalogo y salud de checkout:

```powershell
curl.exe --fail http://localhost:8081/products/p-100
curl.exe --fail http://localhost:8082/health
```

Comprobar el flujo real checkout -> catalogo:

```powershell
curl.exe --fail -H "Content-Type: application/json" -d '{"productId":"p-100","quantity":2}' http://localhost:8082/checkout
```

Parar y eliminar los contenedores:

```powershell
docker compose down --volumes
```

Los comandos locales equivalentes al workflow son:

```powershell
.\gradlew.bat :checkout-service:test --no-daemon
.\gradlew.bat :catalog-service:test --no-daemon
.\gradlew.bat test --no-daemon
docker compose up --build -d
curl.exe --fail http://localhost:8081/products/p-100
curl.exe --fail http://localhost:8082/health
curl.exe --fail -H "Content-Type: application/json" -d '{"productId":"p-100","quantity":2}' http://localhost:8082/checkout
docker compose down --volumes
```

El workflow de [GitHub Actions](.github/workflows/ci.yml) ejecuta los tests de ambos servicios, la
verificacion Pact del proveedor y un smoke test que ejercita la comunicacion entre contenedores.