# Newman Paso A Paso

## Donde Instalar Newman

Instalaremos Newman en la raiz del backend:

```powershell
cd "C:\Users\jonns\OneDrive\Escritorio\Pruebas Postman\demo\demo"
```

La instalacion local es mejor para este ejercicio porque queda amarrada al proyecto. Asi cualquier persona puede ejecutar los mismos comandos con `npm run ...` sin depender de una instalacion global.

## Que Vamos A Usar De Newman

- `newman run`: ejecuta colecciones de Postman desde terminal.
- `--env-var`: envia variables como `baseUrl` sin modificar la coleccion.
- `-d`: ejecuta la coleccion usando datos desde CSV o JSON.
- `-r`: activa reportes como `cli`, `json`, `junit` y `htmlextra`.
- `--reporter-...-export`: guarda reportes en archivos.
- `--bail`: corta la ejecucion cuando aparece el primer fallo.
- `--timeout-request`: controla el tiempo maximo de cada request.

## Instalacion

Desde la raiz del backend:

```powershell
npm install
```

Esto descarga:

- `newman`: motor para ejecutar colecciones.
- `newman-reporter-htmlextra`: reporte HTML visual para revisar resultados.

## Ejecutar La Coleccion Base

Primero levanta el backend:

```powershell
mvn spring-boot:run
```

En otra terminal, desde `demo/demo`, ejecuta:

```powershell
npm run newman:personas
```

Ese comando corre:

```powershell
newman run postman/personas.postman_collection.json --env-var baseUrl=http://localhost:8080
```

## Ejecutar Tu Coleccion Exportada De Postman

Tu coleccion esta en:

```text
postman/coleccion postman.postman_collection.json
```

Se ejecuta con:

```powershell
npm run newman:coleccion
```

Importante: el archivo actual de esa coleccion todavia aparece sin URLs, bodies ni scripts de test. En Postman debes guardar cada request y exportar de nuevo la coleccion para que Newman tenga algo real que ejecutar.

## Ejecutar Pruebas Con CSV

El archivo de datos esta en:

```text
data/personas.csv
```

Cada fila del CSV representa una iteracion. Newman reemplaza `{{nombre}}`, `{{apellido}}` y `{{numero}}` con los valores de cada fila.

Ejecuta:

```powershell
npm run newman:csv
```

Ese comando corre:

```powershell
newman run postman/personas-data-driven.postman_collection.json -d data/personas.csv --env-var baseUrl=http://localhost:8080
```

Si el CSV tiene 4 filas, Newman ejecuta 4 veces el request `Crear persona desde CSV`.

## Ejecutar Tu Coleccion Con CSV

Tambien adapte tu coleccion `coleccion postman` para que el request `Crear una persona` lea el CSV cuando se ejecuta con Newman.

El archivo sigue siendo:

```text
data/personas.csv
```

Columnas esperadas:

```csv
nombre,apellido,numero
Juan,Perez,3001234567
```

Tu request usa `{{tempNombre}}`, `{{tempApellido}}` y `{{tempNumero}}`. El Pre-request copia los datos del CSV hacia esas variables. Si corres la request manualmente en Postman sin CSV, genera datos aleatorios.

Ejecuta solo el request de creacion con todas las filas del CSV:

```powershell
npm run newman:coleccion:csv
```

Comando real:

```powershell
newman run "postman/coleccion postman.postman_collection.json" --folder "Crear una persona" -d data/personas.csv --env-var baseUrl=http://localhost:8080
```

## Generar Reportes

Ejecuta:

```powershell
npm run newman:report
```

Se generan estos archivos:

```text
newman/reporte.html
newman/resultados.json
newman/resultados.xml
```

- `reporte.html`: reporte visual para revisar manualmente.
- `resultados.json`: resultado completo en formato JSON.
- `resultados.xml`: formato JUnit para pipelines CI/CD.

## Flujo Mental Del Ejercicio

1. Postman sirve para disenar y probar manualmente.
2. La coleccion exportada guarda requests, variables y tests.
3. Newman ejecuta esa coleccion fuera de Postman.
4. CSV permite repetir el mismo request con muchos datos.
5. Los reportes permiten revisar evidencia o conectar con CI/CD.
