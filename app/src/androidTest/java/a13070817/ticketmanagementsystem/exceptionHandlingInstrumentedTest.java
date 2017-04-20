package a13070817.ticketmanagementsystem;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Sam on 20/04/2017.
 */

public class exceptionHandlingInstrumentedTest {
    @Rule
    public final IntentsTestRule<Main> searchIntentsTestRule = new IntentsTestRule<>(Main.class);
    @Test
    public void validateTicketSearchErrorHandling(){
        onView((withId(R.id.search)))
                .perform(click());
            //looks up ticket number 18446744073709551616 (SQLite max row)
            onView((withId(R.id.searchLookup)))
                    .perform(typeText("18446744073709551616"));
            onView((withId(R.id.lookupEditText)))
                    .perform(click());
    }
}
