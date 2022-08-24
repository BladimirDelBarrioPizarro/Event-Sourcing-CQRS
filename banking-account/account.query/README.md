application.yml

spring:
   jpa:

Configuración de la plataforma

    database-platform: org.hibernate.dialect.MySQL8Dialect
Mostramos el SQL
 ``` 
    show-sql: true
```
Actualiza el módelo de la bbdd contra el existente esquema de nuestras
clases Java entidad, ante cualquier cambio nos genera un archivo para actualizar al base de datos  
  ```
    hibernate:
       ddl-auto: update
  ```       
Definición de la cádena de conexión a la bbdd.
Si no existe la autogenera
```
datasource:
      url: jdbc:mysql://localhost:3306/bankingAccount?createDatabaseIfNotExist=true
      username: root
      password: Bladi;:_1985
```  

CONFIGURACIÓN KAFKA
Middleware entre command y query.
Command: Produce mensajes
Query: Consume los mensajes (escucha).

Le decimos a kafka que tome en este micro el modo listener.
En ack-mode indicamos si queremos remover el mensaje o que este disponible para otros consumers.
```
kafka:
  listener:
    ack-mode: MANUAL_IMMEDIATE
```  
Definimos el consumidor a nivel de listener con su group-id
```
 consumer:
    bootstrap-servers: localhost:9092
    group-id: bankaccConsumer
```  
Cuando se crea el grupo por primera vez, antes de que se consuman los mensajes, la posición se establece de acuerdo con una política de restablecimiento de compensación configurable.
```
auto-offset-reset: earliest
``` 
Indicamos la key y value de los mensajes
``` 
key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
``` 
Indicamos que todos nuestros objetos de nuestro proyecto son confiables de ser serializados.
``` 
properties:
    spring:
      json:
        trusted:
         packages: '*'
``` 