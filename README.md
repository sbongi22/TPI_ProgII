# Programación II - Trabajo Integrador

## índice 
- [Proyecto: estructura](#estructura-del-proyecto)
- [Descripción del dominio](#descripción-del-dominio)
- [Requisitos](#requisitos-del-sistema)
- [Archivos SQL](#archivos-sql-provistos)
- [Crear Base de Datos](#pasos-para-crear-la-base-de-datos)
- [Capa de persistencia y acceso a datos](#capa-de-persistencia-y-acceso-a-datos)
- [Capa de presentacion](#capa-de-presentación)

---------------

## Sistema de Gestión de Mascotas y Microchips

Aplicación de consola en **Java** para administrar mascotas y sus microchips asociados.  
El sistema implementa un CRUD completo para ambas entidades utilizando JDBC, DAOs genéricos, servicios y modelos basados en una clase común (`Base`).

## Estructura del proyecto

```sh

/src
├── config/
│    └── databaseConnection.java
├── dao/
│    ├── GenericDao.java 
│    └── *.java (DAOs concretos)
├── service/
│    ├── GenericService.java 
│    └── *.java (Servicios concretos) 
├── model/
│    ├── Base.java 
│    ├── Mascota.java 
│    └── Microchip.java 
├── main/
│    ├── Main.java 
│    └── AppMenu.java └── Main.java
 
 ``` 

## Descripción del dominio

El dominio del proyecto corresponde a un **sistema de registro de mascotas** y **gestión de microchips**.  
Permite:

### **Mascotas**

-   Crear, listar, actualizar y eliminar (lógicamente) mascotas
    
-   Buscar mascotas por ID
    
-   Buscar mascotas por dueño
    
-   Asignar un microchip a una mascota
    
-   Ver datos completos (raza, especie, fecha de nacimiento, etc.)
    

### **Microchips**

-   Crear microchips
    
-   Listar microchips
    
-   Buscar por ID
    
-   Eliminar (lógicamente)
    
-   Asignarlos a mascotas
    

Las entidades comparten atributos comunes heredados de `Base`, como:

-   `id`
    
-   `eliminado` (para eliminación lógica)
    

La aplicación funciona mediante un menú textual (`AppMenu`) con opciones interactivas, ejecutados desde la consola.


## Requisitos del sistema

###  Java

-   **JDK 17** o superior
    
-   Compatible con cualquier IDE (IntelliJ, Eclipse, NetBeans) o consola
    

### Base de datos

-   **MySQL** (recomendado)
    
-   Driver JDBC: `mysql-connector-j-8.x.x.jar`
    

Asegúrese de configurar correctamente:

-   URL: `jdbc:mysql://localhost:3306/mascotas_db`
    
-   Usuario y contraseña
    
-   Inclusión del driver en el classpath
    

## Archivos SQL provistos

El proyecto incluye **dos archivos SQL** en el orden exacto en que deben ser ejecutados:

### **1. 01-esquema.sql**

Contiene:

-   Creación de la base de datos
    
-   Creación de tablas
    
-   Relaciones y llaves primarias/foráneas
    
-   Estructura completa del modelo
    

### **2. 02-catalogo.sql**

Incluye:

-   Inserción de datos de prueba
    
-   Datos iniciales para las tablas
    
-   Registros de ejemplo tanto para Mascota como para Microchip
    

----------

## Pasos para crear la base de datos

1.  Abrir MySQL Workbench o Xampp.
    
2.  Ejecutar el script de estructura:
    
`../01-esquema.sql;` 
    
3.  Luego ejecutar el catálogo:
    
`../02-catalogo.sql;` 
    
4.  Verificar tablas y datos:
    
```sql

	SHOW TABLES; 
	SELECT  *  FROM mascotas; 
	SELECT  *  FROM microchips;

``` 



## Capa de persistencia y acceso a datos

### **DAO: Data Access Object<T>**

Define las operaciones CRUD básicas usando JDBC:

-   crear (insert)
    
-   leer (select)
    
-   leerTodos (select)
    
-   actualizar (update)
    
-   eliminar (lógico o físico según implementación) (update)
    

### **Capa de lógica de negocio<T>**

#### GenericService
Realiza las operaciones correspondientes al DAO validando las reglas de negocio antes de persistir los datos en la base de datos.

-   insertar
    
-   actualizar
    
-   eliminar (lógico)
    
-   getById
    
-   getAll

-----------

## Capa de presentación

La capa de presentación corresponde al archivo ``Main.java`` que ejecuta el menú de ``AppMenu.java`` con todos los comandos necesarios para realizar todas las operaciones. La interfaz es una vista báscia que opera con comandos desde consola.

### **Ejecutar**

Ejecutar la app desde el archivo ``Main.java``

----------
