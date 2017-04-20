package a13070817.ticketmanagementsystem;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Sam on 20/04/2017.
 */

public class intentInstrumentedTest {
    @Rule
    public final IntentsTestRule<Main> testRule = new IntentsTestRule<>(Main.class);

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
}
