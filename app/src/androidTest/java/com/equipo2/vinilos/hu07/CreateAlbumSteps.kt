package com.equipo2.vinilos.hu07

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.equipo2.vinilos.R
import com.equipo2.vinilos.support.enterAs
import com.equipo2.vinilos.support.pickOption

/** Portada que existe y carga en todas las versiones de Android. */
const val TEST_COVER_URL = "https://www.gstatic.com/webp/gallery/1.jpg"

/** Prefijo de los álbumes que crean las pruebas, para borrarlos al terminar. */
const val TEST_ALBUM_PREFIX = "E2E HU07"

/** Entra como coleccionista y abre Crear álbum con el botón «+». */
fun openCreateAlbum() {
    enterAs(collector = true)
    onView(withId(R.id.createAlbumButton)).perform(click())
}

/** Llena todos los campos del formulario con datos válidos. */
fun fillAlbumForm(name: String) {
    onView(withId(R.id.nameInput)).perform(replaceText(name), closeSoftKeyboard())
    onView(withId(R.id.coverInput)).perform(replaceText(TEST_COVER_URL), closeSoftKeyboard())
    pickTodayAsReleaseDate()
    pickOption(R.id.genreInput, "Rock")
    pickOption(R.id.recordLabelInput, "EMI")
    onView(withId(R.id.descriptionInput)).perform(scrollTo(), replaceText("Álbum creado por la prueba E2E de HU07."), closeSoftKeyboard())
}

/** Abre el selector de fecha y acepta la fecha de hoy, que viene marcada. */
fun pickTodayAsReleaseDate() {
    onView(withId(R.id.releaseDateInput)).perform(scrollTo(), click())
    onView(withText(R.string.action_accept)).inRoot(isDialog()).perform(click())
}
