# Ejecutar el sistema con Oracle Cloud

El sistema de NetBeans y la API Android deben conectarse a la misma base de Oracle Cloud. Los datos, incluidos CLI007 y sus puntos, ya estan guardados en esa base; no es necesario volver a importarlos.

## Preparar la laptop

1. Actualiza este repositorio con `git pull --ff-only` o clonalo desde GitHub.
2. Instala un JDK 25 y abre el proyecto Maven en NetBeans.
3. Copia de forma privada la carpeta completa del wallet ELGORDODB y el archivo config-elgordo.properties desde tu PC a tu laptop. No los subas a GitHub.
4. Dentro del archivo privado, conserva el usuario y la contrasena actuales. Ajusta TNS_ADMIN en db.url a la ruta real del wallet en la laptop. Usa barras / en las rutas.
5. Copia config.example.properties como config.properties en la raiz del proyecto. Ajusta db.config.external a la ruta real del archivo privado en la laptop.
6. Ejecuta Clean and Build y luego Run desde NetBeans, con conexion a internet.
7. Busca CLI007: debe aparecer Monica Aldana con 98 puntos.

El archivo privado debe contener db.url, db.user y db.password, igual que el utilizado por la API. La configuracion de ejemplo no contiene credenciales y por si sola no permite conectarse.

## Android y la API durante la presentacion

Actualizar este repositorio actualiza el sistema de escritorio. La API es un proyecto distinto, disponible en https://github.com/EdwinBeni/APISandwichesElGordo.

La app Android actual usa https://samuel.tailf25130.ts.net. Si la API y Funnel siguen en la PC samuel, esa PC debe permanecer encendida, conectada a internet y sin suspension, con la API en el puerto 8081 y Funnel activo.

Si tambien vas a ejecutar la API en la laptop, configura alli su archivo privado y el wallet. Verifica la direccion que muestra Funnel: puede ser distinta en otro equipo y tendras que actualizar la direccion de la app Android. No asumas que la direccion samuel se transfiere automaticamente al cambiar de computadora.

## Respaldo local

En la PC original se conservo config.local-respaldo.properties con la conexion anterior. La base local no se elimino. Ese archivo y config.properties estan excluidos de Git.
