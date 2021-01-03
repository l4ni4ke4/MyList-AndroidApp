package com.egeuzma.proje.Controller

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.egeuzma.proje.R
import kotlinx.android.synthetic.main.activity_main.view.*

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    // Uygulama açılınca Ana Ekran yükleniyor mu?
    @Test
    fun isMainActivityInView(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.MainActivityLayout)).check(matches(isDisplayed()))
    }

    // Ana Sayfadaki yeni liste ekleme butonu gözüküyor mu?
    @Test
    fun isCreateListButtonInView(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button5)).check(matches(isDisplayed()))
    }

    // Kalori Hesaplıyıcı butonuna basınca kalori hesaplıyıcı açılıyor mu?
    @Test
    fun navCalorie(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button4)).perform(click())
        onView(withId(R.id.KaloriHesaplıyıcıLayout)).check(matches(isDisplayed()))
    }



}