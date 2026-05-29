# Demo Postman, Newman y Rest Assured

Proyecto Spring Boot con PostgreSQL, validaciones Jakarta Validator, pruebas con Rest Assured y una coleccion de Postman lista para ejecutar con Newman.

## Requisitos

- Java 21
- Maven 3.9 o superior
- Node.js 16 o superior
- PostgreSQL corriendo localmente
- Base de datos creada: `Pruebas_db`

Todos los comandos de este documento se ejecutan desde:

```powershell
cd "C:\Users\jonns\OneDrive\Escritorio\Pruebas Postman\demo\demo"
```

## Variables De Entorno

El proyecto no guarda credenciales reales en `application.properties`. Spring Boot lee la conexion desde variables de entorno:

```properties
spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:Pruebas_db}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Crea tu archivo local a partir del ejemplo:

```powershell
Copy-Item .env.example .env
```

Edita `.env` con tus credenciales reales. Ese archivo esta ignorado por Git, asi que no se versiona.

Para cargar variables en PowerShell durante la sesion actual:

```powershell
$env:DB_HOST="localhost"
$env:DB_PORT="5432"
$env:DB_NAME="Pruebas_db"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="tu_password_local"
$env:DB_DRIVER="org.postgresql.Driver"
```

Tambien puedes cargar el `.env` local con:

```powershell
Get-Content .env | ForEach-Object {
  if ($_ -match '^\s*([^#][^=]+)=(.*)$') {
    [Environment]::SetEnvironmentVariable($matches[1].Trim(), $matches[2].Trim(), "Process")
  }
}
```

## Dependencias Java

Rest Assured, Validator, JPA, PostgreSQL y WebMVC se descargan con Maven porque estan declarados en `pom.xml`.

Instalar/descargar dependencias:

```powershell
mvn dependency:resolve
```

Compilar:

```powershell
mvn clean compile
```

Ejecutar pruebas con Rest Assured:

```powershell
mvn test
```

Levantar la API:

```powershell
mvn spring-boot:run
```

Endpoint base:

```text
http://localhost:8080/api/personas
```

## Newman

Newman ejecuta colecciones de Postman desde terminal. La coleccion de ejemplo esta en:

```text
postman/personas.postman_collection.json
```

### Instalacion Global

Instala Newman globalmente para usar el comando `newman` desde cualquier carpeta:

```powershell
npm install -g newman
```

Reportes HTML basicos:

```powershell
npm install -g newman-reporter-html
```

Reportes HTML mas completos:

```powershell
npm install -g newman-reporter-htmlextra
```

### Instalacion Local Al Proyecto

Si prefieres que todo quede instalado en este proyecto, ejecuta estos comandos desde `demo/demo`:

```powershell
npm init -y
npm install --save-dev newman newman-reporter-html newman-reporter-htmlextra
```

Con instalacion local, ejecuta Newman usando:

```powershell
npx newman run postman/personas.postman_collection.json --env-var baseUrl=http://localhost:8080
```

## Comandos Newman Utiles

Ejecutar la coleccion contra la API local:

```powershell
newman run postman/personas.postman_collection.json --env-var baseUrl=http://localhost:8080
```

Ejecutar usando la variable definida en PowerShell:

```powershell
newman run postman/personas.postman_collection.json --env-var baseUrl=$env:NEWMAN_BASE_URL
```

Generar reporte JSON:

```powershell
newman run postman/personas.postman_collection.json -r cli,json --reporter-json-export newman/resultados.json --env-var baseUrl=http://localhost:8080
```

Generar reporte JUnit XML para CI/CD:

```powershell
newman run postman/personas.postman_collection.json -r cli,junit --reporter-junit-export newman/resultados.xml --env-var baseUrl=http://localhost:8080
```

Generar reporte HTML:

```powershell
newman run postman/personas.postman_collection.json -r cli,html --reporter-html-export newman/reporte.html --env-var baseUrl=http://localhost:8080
```

Generar reporte HTML extra:

```powershell
newman run postman/personas.postman_collection.json -r cli,htmlextra --reporter-htmlextra-export newman/reporte-extra.html --env-var baseUrl=http://localhost:8080
```

Detener la ejecucion al primer fallo:

```powershell
newman run postman/personas.postman_collection.json --bail --env-var baseUrl=http://localhost:8080
```

Definir timeout por request:

```powershell
newman run postman/personas.postman_collection.json --timeout-request 5000 --env-var baseUrl=http://localhost:8080
```

Ejecutar una carpeta especifica de la coleccion:

```powershell
newman run postman/personas.postman_collection.json --folder "Personas" --env-var baseUrl=http://localhost:8080
```

Ejecutar pruebas data driven con archivo CSV o JSON:

```powershell
newman run postman/personas.postman_collection.json -d data/personas.csv --env-var baseUrl=http://localhost:8080
```

## Flujo Recomendado

1. Crea la base de datos `Pruebas_db` en PostgreSQL.
2. Copia `.env.example` a `.env`.
3. Llena `.env` con tus credenciales locales.
4. Carga las variables de entorno en PowerShell.
5. Ejecuta `mvn spring-boot:run`.
6. Importa `postman/personas.postman_collection.json` en Postman.
7. Automatiza con `newman run`.

## Nota Sobre Secretos

No subas `.env`, passwords reales, tokens ni API keys al repositorio. Solo debe quedar versionado `.env.example`, con valores de ejemplo.
