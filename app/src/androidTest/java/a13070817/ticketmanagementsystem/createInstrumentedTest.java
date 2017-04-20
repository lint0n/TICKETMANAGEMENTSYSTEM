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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Samuel Linton SRN 13070817 on 17/04/2017.
 */

public class createInstrumentedTest {
    @Rule
    public final IntentsTestRule<Main> searchIntentsTestRule = new IntentsTestRule<>(Main.class);

    @Test
    public void validateTicketCreate() {
        onView(withId(R.id.fabCreate))
                .perform(click());
        onView(withId(R.id.job_title))
                .perform(typeText("Test Ticket"));
        onView(withId(R.id.job_title))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.job_customer))
                .perform(typeText("J. Bloggs"));
        onView(withId(R.id.job_customer))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.job_asset))
                .perform(typeText("PC001"));
        onView(withId(R.id.job_asset))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.job_engineer))
                .perform(typeText("S.Linton"));
        onView(withId(R.id.job_engineer))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.job_description))
                .perform(typeText("Test that ensures the ability to create a ticket is successfull"));
        onView(withId(R.id.job_description))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.createspinner))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("3-Medium")))
                .perform(click());
        onView(withId(R.id.createspinner))
                .check(matches(withSpinnerText("3-Medium")));
        onView(withId(R.id.menu_create_create))
                .perform(click());
        ViewInteraction appCompatButton = onView(allOf(withId(R.id.snackbar_action), withText("Home"), isDisplayed()));
        appCompatButton.perform(click());
    }
}

