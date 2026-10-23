package com.equipo2.vinilos.support

import com.equipo2.vinilos.BuildConfig
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

/** Llamadas directas a BackVynils para preparar y limpiar datos de prueba. */
object BackVynils {

    private const val TIMEOUT_MS = 90_000

    /** Despierta el servidor de Render, que puede tardar casi un minuto. */
    fun wakeUp() {
        request("albums", "GET")
    }

    fun albumNames(): List<String> {
        val albums = JSONArray(request("albums", "GET"))
        return List(albums.length()) { albums.getJSONObject(it).getString("name") }
    }

    /** Borra los álbumes con este prefijo para no dejar datos de prueba. */
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
