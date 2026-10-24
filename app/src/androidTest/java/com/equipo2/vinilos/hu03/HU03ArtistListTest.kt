package com.equipo2.vinilos.hu03

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.equipo2.vinilos.R
import com.equipo2.vinilos.support.BackVynils
import com.equipo2.vinilos.support.VinilosRule
import com.equipo2.vinilos.support.toolbarTitle
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** E2E de HU03 con la API real: el listado trae todos los músicos y bandas, y los filtros los separan. */
@RunWith(AndroidJUnit4::class)
class HU03ArtistListTest {

    @get:Rule
    val app = VinilosRule()

    private val resources = InstrumentationRegistry.getInstrumentation().targetContext.resources

    @Test
    fun todos_muestraMusicosYBandas_conElContadorDeLaApi() {
        val musicians = BackVynils.musicianNames()
        val bands = BackVynils.bandNames()
        app.launch()

        openArtists()

        onView(toolbarTitle(R.string.artist_list_title)).check(matches(isDisplayed()))
        onView(withText(countText(musicians.size + bands.size))).check(matches(isDisplayed()))
        firstUniqueName(musicians + bands)?.let { onView(artistRowName(it)).check(matches(isDisplayed())) }
    }

    @Test
    fun musicos_muestraSoloLosMusicosDeLaApi() {
        val musicians = BackVynils.musicianNames()
        val bands = BackVynils.bandNames()
        app.launch()
        openArtists()

        onView(withId(R.id.filterMusicians)).perform(click())

        onView(withText(countText(musicians.size))).check(matches(isDisplayed()))
        firstUniqueName(musicians)?.let { onView(artistRowName(it)).check(matches(isDisplayed())) }
        bands.filter { it !in musicians }.forEach { onView(artistRowName(it)).check(doesNotExist()) }
    }

    @Test
    fun bandas_muestraSoloLasBandasDeLaApi() {
        val musicians = BackVynils.musicianNames()
        val bands = BackVynils.bandNames()
        app.launch()
        openArtists()

        onView(withId(R.id.filterBands)).perform(click())

        onView(withText(countText(bands.size))).check(matches(isDisplayed()))
        firstUniqueName(bands)?.let { onView(artistRowName(it)).check(matches(isDisplayed())) }
        musicians.filter { it !in bands }.forEach { onView(artistRowName(it)).check(doesNotExist()) }
    }

    private fun countText(count: Int) = resources.getQuantityString(R.plurals.artist_list_count, count, count)

    // El primero del listado que no se repite, para que Espresso no encuentre dos filas iguales
    private fun firstUniqueName(names: List<String>): String? =
        sortedLikeTheApp(names).take(VISIBLE_ROWS).firstOrNull { name -> names.count { it == name } == 1 }

    private companion object {
        /** Filas que caben sin desplazar incluso en un teléfono pequeño. */
        const val VISIBLE_ROWS = 3
    }
}
