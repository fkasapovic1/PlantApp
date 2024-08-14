package ba.unsa.etf.rma.projekat

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.core.content.ContextCompat
import androidx.test.espresso.DataInteraction
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.internal.ViewUtils.getBackgroundColor
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.Is
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestS2 {

    @get:Rule
    var activityRule: ActivityScenarioRule<NovaBiljkaActivity> = ActivityScenarioRule(NovaBiljkaActivity::class.java)


    @Test
    fun testPrikazSlikeBiljke() {
/*
        Napomena:
        - U zavisnosti od ordera testova ovaj test može trajati malo duže (max 40s) jer mora sačekati
        sve iskocne poruke (Toast) da se završe
        - Kamera radi ali sam dodao postDelayed od 4s pa onda finishActivity(), kako bih simulirao rad kamere
        a klikom na dugme automatski dodajem sliku u slikaIV, samim tim je proces/test automatizovan
*/
        //klik na dugme za slikanje
        onView(withId(R.id.uslikajBiljkuBtn)).perform(click())
        onView(withId(R.id.slikaIV)).check(matches(isDisplayed()))
    }

     @Test
        fun testValidacijaPolja() {

            onView(withId(R.id.nazivET)).perform(replaceText("K"))
            onView(withId(R.id.porodicaET)).perform(replaceText("P"))
            onView(withId(R.id.medicinskoUpozorenjeET)).perform(replaceText("UUUUUUUUUUUUUUUUUUUUUU21"))

            onView(withId(R.id.dodajBiljkuBtn)).perform(click())

            onView(withId(R.id.nazivET)).check(matches(hasErrorText("Naziv mora biti duži od 2 i kraći od 20 znakova")))
            onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Porodica mora biti duža od 2 i kraća od 20 znakova")))
            onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Upozorenje mora biti duže od 2 i kraće od 20 znakova")))
        }

        @Test
        fun testValidacijaPraznihPolja() {

            onView(withId(R.id.nazivET)).perform(replaceText(""))
            onView(withId(R.id.porodicaET)).perform(replaceText(""))
            onView(withId(R.id.medicinskoUpozorenjeET)).perform(replaceText(""))

            onView(withId(R.id.dodajBiljkuBtn)).perform(click())

            onView(withId(R.id.nazivET)).check(matches(hasErrorText("Naziv mora biti duži od 2 i kraći od 20 znakova")))
            onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Porodica mora biti duža od 2 i kraća od 20 znakova")))
            onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Upozorenje mora biti duže od 2 i kraće od 20 znakova")))
        }



    @Test
    fun medicinskaKoristOznaceno() {
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.medicinskaKoristLV)).check(matches(hasBackgroundColor(0, Color.RED)))
    }

    @Test
    fun klimatskiTipOznaceno() {
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.klimatskiTipLV)).check(matches(hasBackgroundColor(0, Color.RED)))
    }

    @Test
    fun zemljisniTipOznaceno() {
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.zemljisniTipLV)).check(matches(hasBackgroundColor(0, Color.RED)))
    }

    @Test
    fun dvaIdenticnaJela() {

        onView(withId(R.id.jeloET)).perform(replaceText("Test"))
        onView(withId(R.id.dodajJeloBtn)).perform(click())

        onView(withId(R.id.jeloET)).perform(replaceText("test"))
        onView(withId(R.id.dodajJeloBtn)).perform(click())

        onView(withId(R.id.dodajBiljkuBtn)).perform(click())

        onView(withId(R.id.jelaLV)).check(matches(hasBackgroundColor(0, Color.RED)))
    }

    @Test
    fun IzmijeniJeloDugme(){

        onView(withId(R.id.jeloET)).perform(replaceText("Grah"))
        onView(withId(R.id.dodajJeloBtn)).perform(click())

        onData(
            CoreMatchers.allOf(
                Is(CoreMatchers.instanceOf(String::class.java)),
                CoreMatchers.containsString("Grah")
            )
        ).inAdapterView(ViewMatchers.withId(R.id.jelaLV)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.dodajJeloBtn)).check(ViewAssertions.matches(ViewMatchers.withText("Izmijeni jelo")))

    }

    // Matcheri -> provjera boja
    private fun hasBackgroundColor(position: Int, expectedColor: Int) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("Boja nije ista")
        }

        override fun matchesSafely(view: View): Boolean {
            val listView = view as ListView
            val childView = listView.getChildAt(position)
            val actualColor = (childView as AppCompatCheckedTextView).background as ColorDrawable
            return actualColor.color == expectedColor
        }
    }
}