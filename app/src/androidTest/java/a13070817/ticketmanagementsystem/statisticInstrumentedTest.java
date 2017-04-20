package a13070817.ticketmanagementsystem;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Sam on 20/04/2017.
 */

public class statisticInstrumentedTest {
    @Rule
    public final IntentsTestRule<Main> searchIntentsTestRule = new IntentsTestRule<>(Main.class);
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void validateStatistic() {
        onView((withId(R.id.about)))
                .perform(click());
        onView(withId(R.id.statistic_download))
                .perform(click());
    }
}
