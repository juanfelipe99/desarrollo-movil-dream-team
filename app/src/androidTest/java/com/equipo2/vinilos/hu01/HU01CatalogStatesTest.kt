package com.equipo2.vinilos.hu01

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.equipo2.vinilos.R
import com.equipo2.vinilos.data.repository.DataError
import com.equipo2.vinilos.support.VinilosRule
import com.equipo2.vinilos.support.enterAs
import com.equipo2.vinilos.support.toolbarTitle
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** UI de HU01 con un repositorio en memoria: estados del catálogo, reintento y navegación al detalle. */
@RunWith(AndroidJUnit4::class)
class HU01CatalogStatesTest {

    @get:Rule
    val app = VinilosRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val buscandoAmerica = testAlbum(100, "Buscando América", 1984, "Salsa")
    private val aDayAtTheRaces = testAlbum(101, "A Day at the Races", 1976, "Rock")

    @Test
    fun mientrasCarga_muestraElMensajeDeCarga() {
        useAlbumRepository(CatalogAlbumRepository().apply { neverAnswer = true })
        app.launch()

        enterAs(collector = false)

        onView(withText(R.string.album_list_loading)).check(matches(isDisplayed()))
        onView(withId(R.id.albumGrid)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun conAlbumes_muestraCadaUnoConSuAnioYGenero_sinElDistintivoNuevo() {
        useAlbumRepository(CatalogAlbumRepository(Result.success(listOf(buscandoAmerica, aDayAtTheRaces))))
        app.launch()

        enterAs(collector = false)

        onView(toolbarTitle(R.string.album_list_title)).check(matches(isDisplayed()))
        onView(albumCardName("Buscando América")).check(matches(isDisplayed()))
        onView(withText("1984 · Salsa")).check(matches(isDisplayed()))
        onView(albumCardName("A Day at the Races")).check(matches(isDisplayed()))
        onView(withText("1976 · Rock")).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.albumNewBadge), isDisplayed())).check(doesNotExist())
    }

    @Test
    fun catalogoVacio_muestraElMensajeDeVacio_sinReintentar() {
        useAlbumRepository(CatalogAlbumRepository(Result.success(emptyList())))
        app.launch()

        enterAs(collector = false)

        onView(withText(R.string.album_list_empty_title)).check(matches(isDisplayed()))
        onView(withText(R.string.album_list_empty_body)).check(matches(isDisplayed()))
        onView(withId(R.id.stateRetry)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun siFallaLaCarga_muestraElError_yReintentarMuestraElCatalogo() {
        useAlbumRepository(
            CatalogAlbumRepository(
                listOf(
                    Result.failure(DataError.Network()),
                    Result.success(listOf(buscandoAmerica)),
                ),
            ),
        )
        app.launch()
        enterAs(collector = false)

        val errorTitle = context.getString(R.string.error_load_title, context.getString(R.string.album_list_object))
        onView(withText(errorTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.albumGrid)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.stateRetry)).perform(click())

        onView(albumCardName("Buscando América")).check(matches(isDisplayed()))
        onView(withText(errorTitle)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun siElReintentoTambienFalla_sigueMostrandoElError() {
        useAlbumRepository(CatalogAlbumRepository(Result.failure(DataError.Server(500))))
        app.launch()
        enterAs(collector = false)

        onView(withId(R.id.stateRetry)).perform(click())

        val errorTitle = context.getString(R.string.error_load_title, context.getString(R.string.album_list_object))
        onView(withText(errorTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.stateRetry)).check(matches(isDisplayed()))
    }

    @Test
    fun tocarUnAlbum_abreSuDetalle_yAtrasVuelveAlCatalogo() {
        useAlbumRepository(CatalogAlbumRepository(Result.success(listOf(buscandoAmerica, aDayAtTheRaces))))
        app.launch()
        enterAs(collector = false)

        openAlbum("A Day at the Races")

        onView(toolbarTitle(R.string.album_detail_title)).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.albumName), withText("A Day at the Races"))).check(matches(isDisplayed()))

        pressBack()

        onView(toolbarTitle(R.string.album_list_title)).check(matches(isDisplayed()))
        onView(albumCardName("Buscando América")).check(matches(isDisplayed()))
    }
}
