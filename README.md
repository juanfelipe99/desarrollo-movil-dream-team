# Vinilos - Dream Team

Proyecto del curso de Desarrollo de Aplicaciones Móviles (MISO, Universidad de los Andes).

Aplicación Android desarrollada en **Kotlin** para consultar y gestionar álbumes, artistas y coleccionistas de música.

## Backend

La app consume el API REST de [BackVynils](https://github.com/TheSoftwareDesignLab/BackVynils).

## Pruebas E2E

Las pruebas E2E están hechas con Espresso, en `app/src/androidTest/java/com/equipo2/vinilos`, y usan la API real de BackVynils:

- `support/`: lo que comparten las pruebas de todas las historias, como la regla que abre la app, la espera a la API y las llamadas directas a BackVynils.
- Una carpeta por historia de usuario, por ejemplo `hu07/`, con sus clases de prueba y las funciones que solo usa esa historia.

Los datos que crean las pruebas en la API se borran al terminar.

### Preparación

1. Abrir un emulador o conectar un teléfono con Android 5.0 (API 21) o superior, con la depuración USB activa, y revisar que aparezca con `adb devices`.
2. Desactivar las animaciones, porque pueden hacer fallar las pruebas:

   ```
   adb shell settings put global window_animation_scale 0
   adb shell settings put global transition_animation_scale 0
   adb shell settings put global animator_duration_scale 0
   ```

   Al terminar se pueden volver a activar con los mismos comandos y el valor `1`.

### Desde Android Studio

En el panel *Project*, clic derecho sobre `app/src/androidTest/java/com.equipo2.vinilos` → *Run 'Tests in com.equipo2.vinilos'*.

Los resultados aparecen en el panel *Run*, abajo. Android Studio ejecuta las pruebas con Gradle.

### Por línea de comandos (todas las versiones de Android)

Desde la carpeta del proyecto se instalan la app y la app de pruebas, y luego se ejecutan las pruebas en el dispositivo:

```
./gradlew installDebug installDebugAndroidTest
adb shell am instrument -w com.equipo2.vinilos.test/androidx.test.runner.AndroidJUnitRunner
```

En Windows se usa `gradlew.bat` en lugar de `./gradlew`.

## Integrantes

- Juan Pablo Vargas
- Juan David Zea
- Juan Felipe Quiñonez
- Jesús Gómez
