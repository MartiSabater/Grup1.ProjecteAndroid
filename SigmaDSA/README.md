# SigmaDSA - Implementación Android para unión a grupos

## Alcance implementado

- Añadida nueva funcionalidad en la app Android para mostrar un listado de grupos.
- Se creó un modelo `Group` en `app/src/main/java/com/example/sigmadsa/models/Group.java`.
- Añadidas nuevas rutas Retrofit en `app/src/main/java/com/example/sigmadsa/api/ApiService.java`:
  - `getGrupos()`
  - `joinGrupo(groupId, userId)`
- Extendida la interfaz de usuario de `ShopActivity` para incluir un nuevo tab `GRUPOS`.
- Implementada la carga dinámica de grupos en `ShopActivity` y un botón `UNIRSE` para unirse a un grupo.
- La nueva funcionalidad usa Retrofit para invocar la API REST.

## Qué funciona

- Visualizar la pantalla principal de la tienda y pasar al tab `GRUPOS`.
- Cargar la lista de grupos desde la API REST.
- Enviar la petición de unión a grupo con Retrofit.
- Respuesta de éxito/fracaso mostrada en pantalla.

## Qué queda pendiente

- Implementación del backend dummy con las rutas:
  - `GET /auth/grupos`
  - `POST /auth/grupos/{groupId}/join/{userId}`
- Capturas de pantalla de la funcionalidad en ejecución.
- Validación adicional de estados (por ejemplo, usuario ya miembro del grupo).
- Mejorar los mensajes y diseño de la sección de grupos.

## Archivos clave modificados

- `app/src/main/java/com/example/sigmadsa/api/ApiService.java`
- `app/src/main/java/com/example/sigmadsa/models/Group.java`
- `app/src/main/java/com/example/sigmadsa/viewmodel/ShopActivity.java`
- `app/src/main/res/layout/activity_shop.xml`
- `app/src/main/res/values/strings.xml`
