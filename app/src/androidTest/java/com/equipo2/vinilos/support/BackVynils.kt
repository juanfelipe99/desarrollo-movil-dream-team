package com.equipo2.vinilos.support

import com.equipo2.vinilos.BuildConfig
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

/** Llamadas directas a BackVynils para preparar y limpiar los datos de las pruebas. */
object BackVynils {

    private const val TIMEOUT_MS = 90_000

    /** Despierta el servidor, que en Render puede tardar casi un minuto en responder la primera vez. */
    fun wakeUp() {
        request("albums", "GET")
    }

    /** Nombres de todos los álbumes guardados. */
    fun albumNames(): List<String> {
        val albums = JSONArray(request("albums", "GET"))
        return List(albums.length()) { albums.getJSONObject(it).getString("name") }
    }

    /** Borra los álbumes cuyo nombre empieza así, para no dejar datos de prueba en la API del equipo. */
    fun deleteAlbumsStartingWith(prefix: String) {
        val albums = JSONArray(request("albums", "GET"))
        for (i in 0 until albums.length()) {
            val album = albums.getJSONObject(i)
            if (album.getString("name").startsWith(prefix)) request("albums/${album.getInt("id")}", "DELETE")
        }
    }

    private fun request(path: String, method: String): String {
        val connection = URL(BuildConfig.API_BASE_URL + path).openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.connectTimeout = TIMEOUT_MS
        connection.readTimeout = TIMEOUT_MS
        return try {
            connection.inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }
}
