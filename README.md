# Vinilos - Dream Team

Proyecto del curso de Desarrollo de Aplicaciones Móviles (MISO, Universidad de los Andes).

Aplicación Android desarrollada en **Kotlin** para consultar y gestionar álbumes, artistas y coleccionistas de música.

## Backend

La app consume el API REST de [BackVynils](https://github.com/TheSoftwareDesignLab/BackVynils).

## Pruebas E2E

Las pruebas E2E están hechas con Espresso, en `app/src/androidTest`, y usan la API real de BackVynils. Para correrlas:

1. Abre un emulador o conecta un teléfono con Android 5.0 (API 21) o superior.
2. En *Opciones de desarrollador*, pon en 0 las tres escalas de animación.
3. En la carpeta del proyecto, ejecuta `./gradlew connectedDebugAndroidTest` (en Windows, `gradlew.bat connectedDebugAndroidTest`).

El reporte queda en `app/build/reports/androidTests/connected/debug/index.html`. Los álbumes que crean las pruebas se borran de la API al terminar.

En Android 5.0 (API 21), la tarea de Gradle se queda esperando al final por un error del plugin de Android al copiar resultados adicionales. En esa versión se instalan las dos apps y se corren las pruebas con `adb`:

```
./gradlew installDebug installDebugAndroidTest
adb shell am instrument -w com.equipo2.vinilos.test/androidx.test.runner.AndroidJUnitRunner
```

## Integrantes

- Juan Pablo Vargas
- Juan David Zea
- Juan Felipe Quiñonez
- Jesús Gómez
