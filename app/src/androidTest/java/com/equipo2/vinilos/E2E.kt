package com.equipo2.vinilos

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

/** Portada que existe y carga en todas las versiones de Android. */
const val TEST_COVER_URL = "https://www.gstatic.com/webp/gallery/1.jpg"

/** Prefijo de los álbumes que crean las pruebas, para borrarlos al terminar. */
const val TEST_ALBUM_PREFIX = "E2E HU07"

/** Título de la barra superior, para no confundirlo con otros textos iguales de la pantalla. */
fun toolbarTitle(@StringRes title: Int): Matcher<View> = allOf(withText(title), isDescendantOfA(withId(R.id.toolbar)))

/** Campo del formulario que muestra este mensaje de error, o ninguno si es null. */
fun hasFieldError(@StringRes message: Int?): Matcher<View> = object : TypeSafeMatcher<View>() {
    private val expected = message?.let { InstrumentationRegistry.getInstrumentation().targetContext.getString(it) }

    override fun describeTo(description: Description) {
        description.appendText("campo con el error ").appendValue(expected)
    }

    override fun matchesSafely(view: View): Boolean = view is TextInputLayout && view.error?.toString() == expected
}

/** Entra con el rol elegido desde la selección de rol. */
fun enterAs(collector: Boolean) {
    onView(withId(if (collector) R.id.collectorButton else R.id.visitorButton)).perform(click())
}

/** Entra como coleccionista y abre Crear álbum con el botón «+». */
fun openCreateAlbum() {
    enterAs(collector = true)
    onView(withId(R.id.createAlbumButton)).perform(click())
}

/** Elige una opción del menú de rol, que se abre como ventana aparte. */
fun chooseInRoleMenu(@IdRes optionId: Int) {
    onView(withId(R.id.roleChip)).perform(click())
    onView(withId(optionId)).inRoot(isPlatformPopup()).perform(click())
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

/** Abre el desplegable y elige la opción, que aparece en una ventana aparte. */
fun pickOption(@IdRes inputId: Int, option: String) {
    onView(withId(inputId)).perform(scrollTo(), click())
    onView(withText(option)).inRoot(isPlatformPopup()).perform(click())
}

/** Espera a que se cumpla algo que pasa fuera de lo que Espresso vigila, como guardar el tema en DataStore. */
fun waitUntil(timeoutMs: Long = 10_000, condition: () -> Boolean) {
    val deadline = System.currentTimeMillis() + timeoutMs
    while (!condition()) {
        check(System.currentTimeMillis() < deadline) { "La condición no se cumplió en ${timeoutMs / 1000} s" }
        Thread.sleep(100)
    }
}

/** Espera a que la app aplique este modo de tema. */
fun waitForNightMode(mode: Int) = waitUntil { AppCompatDelegate.getDefaultNightMode() == mode }
