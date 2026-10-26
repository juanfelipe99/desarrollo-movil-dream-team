package com.equipo2.vinilos.hu01

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.equipo2.vinilos.data.network.trustManagerWithLetsEncrypt
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.security.KeyStore
import java.security.cert.X509Certificate

/** Las portadas de sitios con Let's Encrypt, como cdn.shopify.com, deben cargar también en Android 5 a 7.0. */
@RunWith(AndroidJUnit4::class)
class HU01CoverTrustTest {

    private val issuers = trustManagerWithLetsEncrypt(InstrumentationRegistry.getInstrumentation().targetContext)
        .acceptedIssuers
        .map { it.subjectX500Principal.name }

    @Test
    fun confiaEnLetsEncrypt() {
        assertTrue("Falta ISRG Root X1", issuers.any { "CN=ISRG Root X1" in it })
    }

    @Test
    fun conservaLasAutoridadesDelSistema() {
        val system = KeyStore.getInstance("AndroidCAStore").apply { load(null, null) }
        val systemIssuers = system.aliases().toList().map { (system.getCertificate(it) as X509Certificate).subjectX500Principal.name }
        assertTrue("Se perdieron las autoridades del sistema", issuers.containsAll(systemIssuers))
    }
}
