# Fibonacci API

La **Fibonacci API** es una aplicación Spring Boot que calcula el número de Fibonacci en una posición especificada. Utiliza una base de datos MySQL para almacenar los resultados y estadísticas, asegurando una mejor eficiencia en consultas repetidas. 

## Tecnologías Usadas

- **Java 17**
- **Spring Boot**
- **MySQL** como base de datos
- **Maven** para la gestión de dependencias


## Configuración de la Base de Datos

1. **Iniciar el Servidor de MySQL**:

2. **Crear la Base de Datos y el Usuario**:
   
```sql
CREATE DATABASE IF NOT EXISTS fibonacci_db;
CREATE USER IF NOT EXISTS 'laBanca'@'localhost' IDENTIFIED BY 'LaBancaAdmin1!';
GRANT ALL PRIVILEGES ON fibonacci_db.* TO 'laBanca'@'localhost';
FLUSH PRIVILEGES;
```

## **Compilar el Proyecto**: 
```markdown
mvn clean install
```

## **Ejecutar la Aplicación**: 

```markdown
mvn spring-boot:run
```

## **Uso de la API**

**Obtener el Número de Fibonacci**

**Método:** `GET`  
**Endpoint:** `/api/fibonacci/{position}`  
**Parámetro:** `position` - un entero positivo menor de 92.

**Ejemplo de solicitud y respuesta**:
```bash
curl --location 'http://localhost:8080/api/fibonacci/10'
```

**Response**
```json
{
  "position": 10,
  "result": 55,
  "error": null,
  "httpStatusCode": "OK"
}
```

## **Manejo de Errores**
La API devuelve un mensaje de error si:

- La position es menor a 0 .

```json
{
  "position": -10,
  "result": null,
  "error": "Negative position not allowed",
  "httpStatusCode": "PRECONDITION_FAILED"
}
```
- La position es mayor o igual a 92

```json
{
  "position": 92,
  "result": null,
  "error": "Positions greater than 91 are not allowed",
  "httpStatusCode": "PRECONDITION_FAILED"
}
```

## **Precondiciones de Uso**:
La posición ingresada debe cumplir con las siguientes condiciones:

**Tipo:** `Entero positivo`

**Rango:** `Menor de 92 (limite para garantizar resultados dentro de los límites de tipo long en Java).`
