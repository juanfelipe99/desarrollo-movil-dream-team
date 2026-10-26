package com.equipo2.vinilos.hu03

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.equipo2.vinilos.R
import com.equipo2.vinilos.support.VinilosRule
import com.equipo2.vinilos.support.toolbarTitle
import com.equipo2.vinilos.support.useArtistRepository
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** UI de HU03 con un repositorio en memoria: carga, filtros, contador, vacío, error con reintento y navegación. */
@RunWith(AndroidJUnit4::class)
class HU03ArtistStatesTest {

    @get:Rule
    val app = VinilosRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val errorTitle = context.getString(R.string.error_load_title, context.getString(R.string.artist_list_object))

    private val ruben = testMusician(100, "Rubén Blades Bellido de Luna", 1948)
    private val freddie = testMusician(4, "Freddie Mercury", 1946)
    private val queen = testBand(101, "Queen", 1970)

    @Test
    fun mientrasCarga_muestraElMensajeDeCarga_sinFiltrosNiListado() {
        useArtistRepository(InMemoryArtistRepository().apply { neverAnswer = true })
        app.launch()

        openArtists()

        onView(withText(R.string.artist_list_loading)).check(matches(isDisplayed()))
        onView(withId(R.id.artistHeader)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.artistList)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun conArtistas_muestraCadaUnoConSuTipoYAnio_elContador_yTodosElegido() {
        useArtistRepository(InMemoryArtistRepository(Result.success(listOf(ruben, queen, freddie))))
        app.launch()

        openArtists()

        onView(toolbarTitle(R.string.artist_list_title)).check(matches(isDisplayed()))
        onView(withId(R.id.filterAll)).check(matches(isChecked()))
        onView(withText("3 artistas")).check(matches(isDisplayed()))
        onView(artistRowName("Rubén Blades Bellido de Luna")).check(matches(isDisplayed()))
        onView(withText("Nació en 1948")).check(matches(isDisplayed()))
        onView(artistRowName("Queen")).check(matches(isDisplayed()))
        onView(withText("Creada en 1970")).check(matches(isDisplayed()))
        onView(withText(R.string.artist_type_band)).check(matches(isDisplayed()))
    }

    @Test
    fun filtroMusicos_muestraSoloMusicos_yActualizaElContador() {
        useArtistRepository(InMemoryArtistRepository(Result.success(listOf(ruben, queen, freddie))))
        app.launch()
        openArtists()

        onView(withId(R.id.filterMusicians)).perform(click())

        onView(withId(R.id.filterMusicians)).check(matches(isChecked()))
        onView(withText("2 artistas")).check(matches(isDisplayed()))
        onView(artistRowName("Freddie Mercury")).check(matches(isDisplayed()))
        onView(artistRowName("Queen")).check(doesNotExist())
    }

    @Test
    fun filtroBandas_muestraSoloBandas_yTodosLasVuelveAMostrarTodas() {
        useArtistRepository(InMemoryArtistRepository(Result.success(listOf(ruben, queen, freddie))))
        app.launch()
        openArtists()

        onView(withId(R.id.filterBands)).perform(click())
        onView(withText("1 artista")).check(matches(isDisplayed()))
        onView(artistRowName("Queen")).check(matches(isDisplayed()))
        onView(artistRowName("Freddie Mercury")).check(doesNotExist())

        onView(withId(R.id.filterAll)).perform(click())
        onView(withText("3 artistas")).check(matches(isDisplayed()))
    }

    @Test
    fun filtroSinArtistasDeEseTipo_muestraElVacio_conElContadorEnCero() {
        useArtistRepository(InMemoryArtistRepository(Result.success(listOf(ruben, freddie))))
        app.launch()
        openArtists()

        onView(withId(R.id.filterBands)).perform(click())

        onView(withText(R.string.artist_list_empty_title)).check(matches(isDisplayed()))
        onView(withText("0 artistas")).check(matches(isDisplayed()))
        onView(withId(R.id.filterAll)).check(matches(isDisplayed()))
    }

    @Test
    fun sinArtistas_muestraElMensajeDeVacio_conElContadorEnCero_sinReintentar() {
        useArtistRepository(InMemoryArtistRepository(Result.success(emptyList())))
        app.launch()

        openArtists()

        onView(withText(R.string.artist_list_empty_title)).check(matches(isDisplayed()))
        onView(withText(R.string.artist_list_empty_body)).check(matches(isDisplayed()))
        onView(withText("0 artistas")).check(matches(isDisplayed()))
        onView(withId(R.id.stateRetry)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun siFallaLaCarga_muestraElError_yReintentarMuestraLosArtistas() {
        useArtistRepository(InMemoryArtistRepository(listOf(noConnection(), Result.success(listOf(queen)))))
        app.launch()
        openArtists()

        onView(withText(errorTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.artistHeader)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.stateRetry)).perform(click())

        onView(artistRowName("Queen")).check(matches(isDisplayed()))
        onView(withText(errorTitle)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun siElReintentoTambienFalla_sigueMostrandoElError() {
        useArtistRepository(InMemoryArtistRepository(noConnection()))
        app.launch()
        openArtists()

        onView(withId(R.id.stateRetry)).perform(click())

        onView(withText(errorTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.stateRetry)).check(matches(isDisplayed()))
    }

    @Test
    fun tocarUnArtista_abreSuDetalle_yAtrasVuelveAlListadoConElMismoFiltro() {
        useArtistRepository(InMemoryArtistRepository(Result.success(listOf(ruben, queen))))
        app.launch()
        openArtists()
        onView(withId(R.id.filterBands)).perform(click())

        onView(artistRowName("Queen")).perform(click())

        onView(toolbarTitle(R.string.artist_detail_title)).check(matches(isDisplayed()))

        pressBack()

        onView(toolbarTitle(R.string.artist_list_title)).check(matches(isDisplayed()))
        onView(withId(R.id.filterBands)).check(matches(isChecked()))
        onView(artistRowName("Queen")).check(matches(isDisplayed()))
    }
}
