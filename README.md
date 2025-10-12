# Sistema de Biblioteca - Backend

Sistema de gestión bibliotecaria desarrollado con Java EE, que proporciona una API REST completa para la administración de libros, autores, clientes, usuarios y préstamos.

## Descripción

Backend empresarial construido con tecnologías Java EE estándar, implementando una arquitectura en capas con EJB para la lógica de negocio y JAX-RS para servicios REST. El sistema gestiona el inventario de una biblioteca, el registro de clientes, y el control de préstamos de libros con seguimiento de estados y devoluciones.

## Stack Tecnológico

### Core
- **Java EE 7+** - Plataforma empresarial
- **JAX-RS (RESTEasy)** - Servicios REST
- **EJB 3.x** - Enterprise JavaBeans para lógica de negocio
- **JPA 2.0 / Hibernate** - Persistencia de datos
- **PostgreSQL 9.x+** - Base de datos relacional

### Servidor de Aplicaciones
- **WildFly 10+** / **JBoss EAP** - Servidor compatible Java EE

### Herramientas de Desarrollo
- **Maven** (recomendado) o gestión manual de dependencias
- **Eclipse IDE** con JBoss Tools (opcional)

## Requisitos Previos

### Software Requerido
- **JDK 8+** (OpenJDK o Oracle JDK)
- **WildFly 10+** o JBoss EAP 7+
- **PostgreSQL 9.6+**
- **Maven 3.x** (opcional, para build automatizado)

### Configuración del Sistema
```bash
# Verificar versión de Java
java -version

# Verificar PostgreSQL
psql --version
```

## Estructura del Proyecto

```
biblioteca-backend/
├── BIBLIOTECA-EJB/              # Módulo EJB - Lógica de negocio
│   └── ejbModule/
│       ├── META-INF/
│       │   └── persistence.xml  # Configuración de persistencia JPA
│       └── org/biblioteca/
│           ├── entidad/         # Entidades JPA (modelos de datos)
│           │   ├── Libro.java
│           │   ├── Autor.java
│           │   ├── Cliente.java
│           │   ├── Usuario.java
│           │   ├── Ciudad.java
│           │   ├── Prestamo.java
│           │   ├── PrestamoLibro.java
│           │   ├── LibroAutor.java
│           │   └── LibrosTipos.java
│           ├── abm/             # ABM (Alta, Baja, Modificación)
│           │   └── session/     # Session Beans para operaciones CRUD
│           │       ├── LibroSession.java
│           │       ├── AutorSession.java
│           │       ├── ClienteSession.java
│           │       ├── UsuarioSession.java
│           │       └── CiudadSession.java
│           └── mov/             # Módulo de movimientos
│               └── session/     # Session Beans para operaciones transaccionales
│                   ├── PrestamoSession.java
│                   └── PrestamoLibroSession.java
│
└── PROYECTO-REST/               # Módulo REST - API HTTP
    ├── WebContent/
    │   └── WEB-INF/
    │       └── web.xml          # Descriptor de despliegue
    └── src/
        └── org/biblioteca/
            ├── rest/
            │   └── BibliotecaRestApplication.java  # Configuración JAX-RS
            ├── abm/rest/        # Endpoints REST para ABM
            │   ├── LibroRestService.java
            │   ├── AutorRestService.java
            │   ├── ClienteRestService.java
            │   ├── UsuarioRestService.java
            │   ├── CiudadRestService.java
            │   ├── LibroNormalizadoRestService.java
            │   ├── AutorNormalizadoRestService.java
            │   ├── ClienteNormalizadoRestService.java
            │   ├── UsuarioNormalizadoRestService.java
            │   └── CiudadNormalizadoRestService.java
            └── mov/rest/        # Endpoints REST para movimientos
                ├── PrestamoRestService.java
                └── PrestamoLibroRestService.java
```

## Instalación y Configuración

### 1. Configurar Base de Datos PostgreSQL

```bash
# Crear base de datos
createdb biblioteca_db

# Conectar y crear usuario (opcional)
psql -d biblioteca_db
CREATE USER biblioteca_user WITH PASSWORD 'biblioteca_pass';
GRANT ALL PRIVILEGES ON DATABASE biblioteca_db TO biblioteca_user;
```

### 2. Configurar WildFly/JBoss

**Agregar módulo de PostgreSQL JDBC Driver:**

```bash
# Descargar el driver JDBC de PostgreSQL
# Ubicar en: wildfly/modules/system/layers/base/org/postgresql/main/

# Crear module.xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.postgresql">
    <resources>
        <resource-root path="postgresql-42.x.x.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```

**Configurar DataSource en standalone.xml:**

```xml
<datasources>
    <datasource jndi-name="java:/jboss/datasources/bibliotecaDS"
                pool-name="bibliotecaDS"
                enabled="true"
                use-java-context="true">
        <connection-url>jdbc:postgresql://localhost:5432/biblioteca_db</connection-url>
        <driver>postgresql</driver>
        <security>
            <user-name>biblioteca_user</user-name>
            <password>biblioteca_pass</password>
        </security>
    </datasource>
    <drivers>
        <driver name="postgresql" module="org.postgresql">
            <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
        </driver>
    </drivers>
</datasources>
```

### 3. Configurar Persistence Unit

El archivo `BIBLIOTECA-EJB/ejbModule/META-INF/persistence.xml` ya está configurado:

```xml
<persistence-unit name="BibliotecaPersistenceUnit" transaction-type="JTA">
    <provider>org.hibernate.ejb.HibernatePersistence</provider>
    <jta-data-source>java:/jboss/datasources/bibliotecaDS</jta-data-source>
    <properties>
        <property name="hibernate.show_sql" value="true" />
        <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect" />
    </properties>
</persistence-unit>
```

### 4. Compilar y Desplegar

**Opción A: Despliegue manual en WildFly**

1. Exportar `BIBLIOTECA-EJB` como EJB JAR
2. Exportar `PROYECTO-REST` como WAR
3. Copiar ambos archivos a `wildfly/standalone/deployments/`

```bash
# Copiar artefactos
cp BIBLIOTECA-EJB.jar $WILDFLY_HOME/standalone/deployments/
cp PROYECTO-REST.war $WILDFLY_HOME/standalone/deployments/

# Iniciar servidor
cd $WILDFLY_HOME/bin
./standalone.sh
```

**Opción B: Despliegue desde Eclipse con JBoss Tools**

1. Agregar servidor WildFly en Eclipse
2. Configurar ambos proyectos en el servidor
3. Click derecho → Run As → Run on Server

### 5. Verificar Despliegue

```bash
# Verificar que los módulos se desplegaron correctamente
# Buscar en logs: wildfly/standalone/log/server.log

# Mensaje esperado:
# WFLYSRV0027: Starting deployment of "BIBLIOTECA-EJB.jar"
# WFLYSRV0027: Starting deployment of "PROYECTO-REST.war"
# WFLYSRV0025: Started deployment PROYECTO-REST.war

# Probar endpoint de prueba
curl http://localhost:8080/PROYECTO-REST/rest/libro/list
```

## API REST - Endpoints

Base URL: `http://localhost:8080/PROYECTO-REST/rest`

### Libros (`/libro`)

| Método | Endpoint | Descripción | Ejemplo Body |
|--------|----------|-------------|--------------|
| GET | `/libro/list` | Lista todos los libros | - |
| GET | `/libro/find/{codigo}` | Busca libro por código | - |
| PUT | `/libro/update` | Crea o actualiza libro | `{"codigo": 0, "descripcion": "Don Quijote", "cantidad": 5, "obs": "Edición 2020", "tipos": {"codigo": 1}}` |
| DELETE | `/libro/delete/{codigo}` | Elimina libro | - |

### Autores (`/autor`)

| Método | Endpoint | Descripción | Ejemplo Body |
|--------|----------|-------------|--------------|
| GET | `/autor/list` | Lista todos los autores | - |
| GET | `/autor/find/{codigo}` | Busca autor por código | - |
| PUT | `/autor/update` | Crea o actualiza autor | `{"codigo": 0, "descripcion": "Miguel de Cervantes"}` |
| DELETE | `/autor/delete/{codigo}` | Elimina autor | - |

### Clientes (`/cliente`)

| Método | Endpoint | Descripción | Ejemplo Body |
|--------|----------|-------------|--------------|
| GET | `/cliente/list` | Lista todos los clientes | - |
| GET | `/cliente/find/{codigo}` | Busca cliente por código | - |
| PUT | `/cliente/update` | Crea o actualiza cliente | `{"codigo": 0, "nombre": "Juan Pérez", "direccion": "Av. Principal 123", "telefono": "021-123456"}` |
| DELETE | `/cliente/delete/{codigo}` | Elimina cliente | - |

### Usuarios (`/usuario`)

| Método | Endpoint | Descripción | Ejemplo Body |
|--------|----------|-------------|--------------|
| GET | `/usuario/list` | Lista todos los usuarios | - |
| GET | `/usuario/find/{codigo}` | Busca usuario por código | - |
| PUT | `/usuario/update` | Crea o actualiza usuario | `{"codigo": 0, "nombre": "admin", "clave": "admin123"}` |
| DELETE | `/usuario/delete/{codigo}` | Elimina usuario | - |

### Ciudades (`/ciudad`)

| Método | Endpoint | Descripción | Ejemplo Body |
|--------|----------|-------------|--------------|
| GET | `/ciudad/list` | Lista todas las ciudades | - |
| GET | `/ciudad/find/{codigo}` | Busca ciudad por código | - |
| PUT | `/ciudad/update` | Crea o actualiza ciudad | `{"codigo": 0, "descripcion": "Asunción"}` |
| DELETE | `/ciudad/delete/{codigo}` | Elimina ciudad | - |

### Préstamos (`/prestamo`)

| Método | Endpoint | Descripción | Ejemplo Body |
|--------|----------|-------------|--------------|
| GET | `/prestamo/list` | Lista todos los préstamos | - |
| GET | `/prestamo/find/{numero}` | Busca préstamo por número | - |
| PUT | `/prestamo/update` | Crea o actualiza préstamo con detalle | Ver ejemplo complejo abajo |
| DELETE | `/prestamo/delete/{numero}` | Anula préstamo | - |

**Ejemplo de Préstamo con Detalle:**

```json
{
  "numero": 0,
  "fecha": "2024-01-15T10:00:00",
  "cliente": {"codigo": 1},
  "usuario": {"codigo": 1},
  "situacion": 1,
  "total": 50000.0,
  "observacion": "Préstamo regular",
  "lista": [
    {
      "libro": {"codigo": 1},
      "dias": 7,
      "valor": 25000.0,
      "estado": 1,
      "fechaDevolucion": "2024-01-22T10:00:00"
    },
    {
      "libro": {"codigo": 2},
      "dias": 7,
      "valor": 25000.0,
      "estado": 1,
      "fechaDevolucion": "2024-01-22T10:00:00"
    }
  ]
}
```

### Detalle de Préstamo Libros (`/prestamo-libro`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/prestamo-libro/find-by-prestamo/{prestamoId}` | Lista libros de un préstamo |
| DELETE | `/prestamo-libro/delete/{prestamoId}` | Elimina detalle de préstamo |

### Endpoints Normalizados (Formato alternativo de respuesta)

Los siguientes endpoints devuelven datos en formato normalizado para compatibilidad con clientes específicos:

- `/autor_no_usar/*` - Versión normalizada de autores
- `/libro_no_usar/*` - Versión normalizada de libros
- `/cliente_no_usar/*` - Versión normalizada de clientes
- `/usuario_no_usar/*` - Versión normalizada de usuarios
- `/ciudad_no_usar/*` - Versión normalizada de ciudades

## Modelo de Datos

### Entidades Principales

**Libro**
- `lib_codigo` (PK, SERIAL)
- `lib_descripcion` (VARCHAR)
- `lib_cantidad` (INTEGER)
- `lib_obs` (TEXT)
- `lib_tipo` (FK → LibrosTipos)

**Autor**
- `aut_codigo` (PK, SERIAL)
- `aut_descripcion` (VARCHAR)

**Cliente**
- `cli_codigo` (PK, SERIAL)
- `cli_nombre` (VARCHAR)
- `cli_direccion` (VARCHAR)
- `cli_telefono` (VARCHAR)

**Usuario**
- `usu_codigo` (PK, SERIAL)
- `usu_nombre` (VARCHAR)
- `usu_clave` (VARCHAR)

**Prestamo**
- `pre_numero` (PK, SERIAL)
- `pre_fecha` (TIMESTAMP)
- `pre_cliente` (FK → Cliente)
- `pre_usuario` (FK → Usuario)
- `pre_situacion` (INTEGER)
- `pre_total` (DECIMAL)
- `pre_observacion` (TEXT)

**PrestamoLibro** (Detalle)
- `prl_prestamo` (FK → Prestamo)
- `prl_libro` (FK → Libro)
- `prl_dias` (INTEGER)
- `prl_valor` (DECIMAL)
- `prl_estado` (INTEGER)
- `prl_fecha_devolucion` (TIMESTAMP)

### Relaciones
- **Libro ↔ Autor**: Relación N:N mediante `LibroAutor`
- **Prestamo → Cliente**: N:1
- **Prestamo → Usuario**: N:1
- **Prestamo ↔ Libro**: 1:N mediante `PrestamoLibro`

## Configuración CORS

El backend está configurado para aceptar peticiones de cualquier origen:

```java
CorsFilter corsFilter = new CorsFilter();
corsFilter.getAllowedOrigins().add("*");
corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");
```

Para producción, se recomienda restringir los orígenes permitidos:

```java
corsFilter.getAllowedOrigins().add("http://localhost:4200");
corsFilter.getAllowedOrigins().add("https://tu-dominio.com");
```

## Pruebas de API

### Usando cURL

```bash
# Listar libros
curl -X GET http://localhost:8080/PROYECTO-REST/rest/libro/list

# Buscar libro específico
curl -X GET http://localhost:8080/PROYECTO-REST/rest/libro/find/1

# Crear nuevo libro
curl -X PUT http://localhost:8080/PROYECTO-REST/rest/libro/update \
  -H "Content-Type: application/json" \
  -d '{"codigo":0,"descripcion":"Cien Años de Soledad","cantidad":10,"obs":"Edición especial","tipos":{"codigo":1}}'

# Eliminar libro
curl -X DELETE http://localhost:8080/PROYECTO-REST/rest/libro/delete/5
```

### Usando Postman

1. Importar colección con base URL: `http://localhost:8080/PROYECTO-REST/rest`
2. Configurar Headers: `Content-Type: application/json`
3. Probar endpoints según la tabla anterior

## Troubleshooting

### Error: DataSource no encontrado

```
Caused by: javax.naming.NameNotFoundException: jboss/datasources/bibliotecaDS
```

**Solución**: Verificar que el DataSource esté configurado en `standalone.xml` y que WildFly haya reiniciado correctamente.

### Error: Driver PostgreSQL no encontrado

```
WFLYJCA0041: Failed to load module for driver [org.postgresql]
```

**Solución**: Verificar que el módulo PostgreSQL esté instalado en `wildfly/modules/system/layers/base/org/postgresql/main/` con el driver JAR y el `module.xml`.

### Error: Entidad no puede ser persistida

```
javax.persistence.TransactionRequiredException: no transaction is in progress
```

**Solución**: Verificar que los EJB Session Beans tengan la anotación `@Stateless` y que estén siendo inyectados correctamente con `@EJB`.

### Puerto 8080 ya en uso

```bash
# Cambiar puerto en standalone.xml
<socket-binding name="http" port="${jboss.http.port:8180}"/>

# O detener proceso que usa el puerto
lsof -ti:8080 | xargs kill -9
```

### Logs del servidor

```bash
# Ubicación de logs
tail -f $WILDFLY_HOME/standalone/log/server.log

# Habilitar SQL logging en persistence.xml
<property name="hibernate.show_sql" value="true" />
<property name="hibernate.format_sql" value="true" />
```

## Mejores Prácticas de Desarrollo

1. **Transacciones**: Los Session Beans gestionan automáticamente las transacciones con `@TransactionAttribute`
2. **Validación**: Implementar validaciones en Session Beans antes de persistir
3. **Logging**: Usar `java.util.logging.Logger` para logs estructurados
4. **Seguridad**: Implementar autenticación JWT o sesiones para endpoints protegidos
5. **Versionado**: Considerar versionado de API (`/rest/v1/...`)

## Tecnologías Complementarias (Opcionales)

- **Swagger/OpenAPI**: Documentación interactiva de API
- **JWT**: Autenticación y autorización con tokens
- **Flyway/Liquibase**: Control de versiones de base de datos
- **SLF4J + Logback**: Sistema de logging robusto
- **JUnit + Arquillian**: Testing de componentes EJB

## Contribución

Para contribuir al proyecto:

1. Seguir convenciones de nombres Java (camelCase para métodos, PascalCase para clases)
2. Documentar métodos públicos con Javadoc
3. Mantener separación de capas (Entity → Session → REST)
4. Probar endpoints después de cambios

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## Contacto y Soporte

Para reportar bugs o solicitar funcionalidades, crear un issue en el repositorio del proyecto.

## Recursos Adicionales

- [Java EE 7 Tutorial](https://docs.oracle.com/javaee/7/tutorial/)
- [WildFly Documentation](https://docs.wildfly.org/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [JAX-RS Specification](https://jakarta.ee/specifications/restful-ws/)
- [JPA 2.0 Specification](https://jakarta.ee/specifications/persistence/)
