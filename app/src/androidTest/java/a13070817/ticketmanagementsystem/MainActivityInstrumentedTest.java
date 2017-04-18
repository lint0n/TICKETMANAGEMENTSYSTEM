package a13070817.ticketmanagementsystem;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Sam on 17/04/2017.
 */

public class MainActivityInstrumentedTest {

    @Rule
    public IntentsTestRule<Main> activityIntentsTestRule = new IntentsTestRule<>(Main.class);

    @Test
    public void validateIntentSentToCreate() {
        onView(withId(R.id.fabCreate)).perform(click());
        intended(hasComponent(hasClassName(Create.class.getName())));
    }

    @Test
    public void validateIntentSentToSearch() {
        onView((withId(R.id.search))).perform(click());
        intended(hasComponent(hasClassName(Search.class.getName())));
    }

    @Test
    public void validateIntentSentToStats() {
        onView((withId(R.id.about))).perform(click());
        intended(hasComponent(hasClassName(Statistics.class.getName())));
    }

    @Test
    public void validateTicketCreate() {

        for (int i = 0; i < 25; i++) {
            onView(withId(R.id.fabCreate)).perform(click());
            onView(withId(R.id.job_title)).perform(typeText("Test " + i));
            onView(withId(R.id.job_title)).perform(closeSoftKeyboard());
            onView(withId(R.id.job_customer)).perform(typeText("Customer" + i));
            onView(withId(R.id.job_customer)).perform(closeSoftKeyboard());
            onView(withId(R.id.job_asset)).perform(typeText("PC" + i));
            onView(withId(R.id.job_asset)).perform(closeSoftKeyboard());
            onView(withId(R.id.job_engineer)).perform(typeText("S.C. Linton"));
            onView(withId(R.id.job_engineer)).perform(closeSoftKeyboard());
            onView(withId(R.id.job_description)).perform(typeText("Espresso test " + i));
            onView(withId(R.id.job_description)).perform(closeSoftKeyboard());
            onView(withId(R.id.createspinner)).perform(click());
            if (i % 3 == 0) {
                onData(allOf(is(instanceOf(String.class)), is("3-Medium"))).perform(click());
                onView(withId(R.id.createspinner)).check(matches(withSpinnerText("3-Medium")));
            } else if (i % 10 == 0) {
                onData(allOf(is(instanceOf(String.class)), is("2-High"))).perform(click());
                onView(withId(R.id.createspinner)).check(matches(withSpinnerText("2-High")));
            } else if (i % 25 == 0) {
                onData(allOf(is(instanceOf(String.class)), is("1-Critical"))).perform(click());
                onView(withId(R.id.createspinner)).check(matches(withSpinnerText("1-Critical")));
            } else {
                onData(allOf(is(instanceOf(String.class)), is("4-Low"))).perform(click());
                onView(withId(R.id.createspinner)).check(matches(withSpinnerText("4-Low")));
            }

            ViewInteraction actionMenuItemView1 = onView(
                    allOf(withId(R.id.menu_create_create), withContentDescription("Create"), isDisplayed()));
            actionMenuItemView1.perform(click());

            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.snackbar_action), withText("Home"), isDisplayed()));
            appCompatButton.perform(click());
        }
    }
}

