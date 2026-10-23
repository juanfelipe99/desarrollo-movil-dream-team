package com.equipo2.vinilos.support

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.equipo2.vinilos.R
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

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

/** Elige una opción del menú de rol, que se abre como ventana aparte. */
fun chooseInRoleMenu(@IdRes optionId: Int) {
    onView(withId(R.id.roleChip)).perform(click())
    onView(withId(optionId)).inRoot(isPlatformPopup()).perform(click())
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
