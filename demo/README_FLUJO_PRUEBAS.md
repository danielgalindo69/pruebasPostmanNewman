# Flujo De Pruebas Con Postman, Newman Y CSV

Este documento explica el flujo completo para probar la API de Personas usando Postman de forma manual y Newman de forma automatizada.

## 1. Ubicacion Del Proyecto

Todos los comandos se ejecutan desde la raiz del backend:

```powershell
cd "C:\Users\jonns\OneDrive\Escritorio\Pruebas Postman\demo\demo"
```

Esta carpeta contiene:

```text
pom.xml
package.json
postman/
data/
newman/
src/
```

## 2. Levantar El Backend

Antes de ejecutar Newman, la API debe estar corriendo en `http://localhost:8080`.

```powershell
mvn spring-boot:run
```

Endpoint principal:

```text
http://localhost:8080/api/personas
```

## 3. Que Son Las Colecciones De Postman

Una coleccion de Postman es un archivo JSON que guarda:

- Requests HTTP.
- URLs.
- Bodies.
- Variables.
- Scripts Pre-request.
- Tests o validaciones.

En este proyecto las colecciones estan en:

```text
postman/
```

Colecciones actuales:

```text
postman/coleccion postman.postman_collection.json
postman/personas.postman_collection.json
postman/personas-data-driven.postman_collection.json
```

La coleccion principal del ejercicio es:

```text
postman/coleccion postman.postman_collection.json
```

## 4. Que Es El Archivo CSV

Un archivo CSV es una tabla de datos en texto plano. Newman puede leer cada fila del CSV y ejecutar la misma request varias veces.

Archivo usado:

```text
data/personas.csv
```

Contenido:

```csv
nombre,apellido,numero
Juan,Perez,3001234567
Ana,Gomez,3112223344
Carlos,Ramirez,3205556677
Laura,Torres,3159876543
```

Cada columna se convierte en una variable:

```text
{{nombre}}
{{apellido}}
{{numero}}
```

En tu coleccion principal, el request `Crear una persona` copia esos valores hacia:

```text
{{tempNombre}}
{{tempApellido}}
{{tempNumero}}
```

Eso permite que el mismo request funcione de dos formas:

- En Postman manual: genera datos aleatorios si no hay CSV.
- En Newman: usa cada fila de `data/personas.csv`.

## 5. Que Es Newman

Newman es el ejecutor de colecciones de Postman desde consola.

Sirve para:

- Ejecutar colecciones sin abrir Postman.
- Automatizar pruebas.
- Ejecutar pruebas en pipelines CI/CD.
- Usar archivos CSV o JSON como datos de entrada.
- Generar reportes HTML, JSON y JUnit XML.

## 6. Instalar Newman Localmente

La instalacion local ya esta configurada en este proyecto con `package.json`.

Instalar dependencias:

```powershell
npm install
```

Ejecutar con scripts del proyecto:

```powershell
npm run newman:coleccion:csv
```

Ventaja: no necesitas instalar Newman globalmente y todos usan la version declarada en el proyecto.

## 7. Instalar Newman Globalmente

Si quieres usar el comando `newman` desde cualquier carpeta de Windows, instala Newman global:

```powershell
npm install -g newman
```

Verificar instalacion:

```powershell
newman -v
```

Instalar reporter HTML basico:

```powershell
npm install -g newman-reporter-html
```

Instalar reporter HTML avanzado:

```powershell
npm install -g newman-reporter-htmlextra
```

Si PowerShell no reconoce `newman` despues de instalarlo, cierra y abre una nueva terminal. Si sigue fallando, revisa que la carpeta global de npm este en el `PATH`:

```powershell
npm config get prefix
```

## 8. Comandos Basicos De Newman

Ejecutar tu coleccion completa:

```powershell
newman run "postman/coleccion postman.postman_collection.json" --env-var baseUrl=http://localhost:8080
```

Ejecutar solo el request `Crear una persona`:

```powershell
newman run "postman/coleccion postman.postman_collection.json" --folder "Crear una persona" --env-var baseUrl=http://localhost:8080
```

Ejecutar solo el request `Crear una persona` con CSV:

```powershell
newman run "postman/coleccion postman.postman_collection.json" --folder "Crear una persona" -d data/personas.csv --env-var baseUrl=http://localhost:8080
```

Ejecutar usando `npx`, sin instalar Newman global:

```powershell
npx newman run "postman/coleccion postman.postman_collection.json" --folder "Crear una persona" -d data/personas.csv --env-var baseUrl=http://localhost:8080
```

Ejecutar usando el script del proyecto:

```powershell
npm run newman:coleccion:csv
```

## 9. Comandos Con Reportes

Reporte por consola:

```powershell
newman run "postman/coleccion postman.postman_collection.json" -r cli --env-var baseUrl=http://localhost:8080
```

Reporte JSON:

```powershell
newman run "postman/coleccion postman.postman_collection.json" -r cli,json --reporter-json-export newman/resultados.json --env-var baseUrl=http://localhost:8080
```

Reporte JUnit XML:

```powershell
newman run "postman/coleccion postman.postman_collection.json" -r cli,junit --reporter-junit-export newman/resultados.xml --env-var baseUrl=http://localhost:8080
```

Reporte HTML avanzado:

```powershell
newman run "postman/coleccion postman.postman_collection.json" -r cli,htmlextra --reporter-htmlextra-export newman/reporte.html --env-var baseUrl=http://localhost:8080
```

## 10. Scripts Disponibles En El Proyecto

Ver scripts:

```powershell
npm run
```

Ejecutar coleccion base:

```powershell
npm run newman:personas
```

Ejecutar tu coleccion:

```powershell
npm run newman:coleccion
```

Ejecutar tu coleccion con CSV:

```powershell
npm run newman:coleccion:csv
```

Ejecutar coleccion data-driven de ejemplo:

```powershell
npm run newman:csv
```

Generar reportes:

```powershell
npm run newman:report
```

## 11. Como Leer El Resultado

Cuando Newman termina, muestra una tabla similar a esta:

```text
iterations   executed 4   failed 0
requests     executed 4   failed 0
assertions   executed 8   failed 0
```

Significado:

- `iterations`: cantidad de veces que se ejecuto la coleccion o request.
- `requests`: cantidad de requests HTTP ejecutados.
- `assertions`: cantidad de validaciones ejecutadas.
- `failed`: cantidad de errores.

Para el CSV actual hay 4 filas, por eso ves:

```text
iterations 4
```

## 12. Flujo Recomendado Del Ejercicio

1. Levanta PostgreSQL.
2. Levanta Spring Boot con `mvn spring-boot:run`.
3. Prueba manualmente en Postman.
4. Exporta la coleccion a `postman/`.
5. Ejecuta `npm run newman:coleccion`.
6. Ejecuta `npm run newman:coleccion:csv`.
7. Revisa que `failed` sea `0`.
8. Genera reportes cuando necesites evidencia.

## 13. Notas Importantes

- Si usas Newman local, ejecuta con `npm run ...` o `npx newman ...`.
- Si quieres escribir `newman` directamente, instala Newman global con `npm install -g newman`.
- Si modificas la coleccion en Postman, exportala de nuevo y reemplaza el archivo dentro de `postman/`.
- No subas contrasenas ni archivos `.env` reales al repositorio.
