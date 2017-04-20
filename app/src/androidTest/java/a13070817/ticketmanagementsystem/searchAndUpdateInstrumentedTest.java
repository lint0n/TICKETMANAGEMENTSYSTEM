package a13070817.ticketmanagementsystem;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigInteger;
import java.security.SecureRandom;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Samuel Linton SRN 13070817 on 20/04/2017.
 */

public class searchAndUpdateInstrumentedTest {
    @Rule
    public final IntentsTestRule<Main> searchIntentsTestRule = new IntentsTestRule<>(Main.class);
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void validateTicketSearchAndUpdate() {
        //generates random string
        //inspired from http://stackoverflow.com/a/41156
        SecureRandom secureRandom = new SecureRandom();
        String randomString = new BigInteger(130, secureRandom).toString(32);

        onView((withId(R.id.search)))
                .perform(click());
        onView((withId(R.id.searchLookup)))
                .perform(typeText("1"));
        onView((withId(R.id.lookupEditText)))
                .perform(click());
        onView(withId(R.id.JobDescription))
                .perform(replaceText(randomString));
        closeSoftKeyboard();
        onView(withId(R.id.checkBox))
                .perform(click());
        onView(withId(R.id.fabUpdate))
                .perform(click());
        ViewInteraction appCompatButton = onView(allOf(withId(R.id.snackbar_action), withText("Home"), isDisplayed()));
        appCompatButton.perform(click());
    }
}
