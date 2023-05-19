package com.febrian.storyapp.ui.story

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.febrian.storyapp.R
import com.febrian.storyapp.ui.auth.LoginActivity
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddNewStoryActivityTest {

    @Test
    fun logInTest() {
        val activityScenario = ActivityScenario.launch(
            LoginActivity::class.java
        )

        onView(withId(R.id.edt_email)).perform(typeText("f26@gmail.com"))
        onView(withId(R.id.edt_password)).perform(typeText("12345678"))
        onView(withId(R.id.btn_login)).perform(click())

        activityScenario.close()
    }
}