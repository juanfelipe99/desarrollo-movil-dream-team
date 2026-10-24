
package com.equipo2.vinilos.hu02

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.equipo2.vinilos.R

fun verifyAlbumDescriptionIsDisplayed() {
    onView(withId(R.id.albumDescription))
        .perform(scrollTo())
        .check(matches(isDisplayed()))
}

fun expandAlbumDescription() {
    onView(withId(R.id.albumDescriptionToggle))
        .perform(scrollTo())
        .check(matches(isDisplayed()))
        .perform(click())
}

fun verifyTracksAndCommentsSections() {

    fun verifyTracksAndCommentsSections() {
        onView(withId(R.id.tracksCount))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withId(R.id.tracksList))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withId(R.id.commentsCount))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withId(R.id.commentsList))
            .perform(scrollTo())
            .check(matches(isDisplayed()))
    }

}
