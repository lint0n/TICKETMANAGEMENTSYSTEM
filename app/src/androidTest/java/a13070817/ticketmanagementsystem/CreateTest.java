package a13070817.ticketmanagementsystem;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateTest {

    @Rule
    public ActivityTestRule<Main> mActivityTestRule = new ActivityTestRule<>(Main.class);

    @Test
    public void createTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fabCreate),
                        withParent(allOf(withId(R.id.activity_main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.job_title), isDisplayed()));
        textInputEditText.perform(replaceText("Test Title"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.job_customer), isDisplayed()));
        textInputEditText2.perform(replaceText("J. Bloggs"), closeSoftKeyboard());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.job_asset), isDisplayed()));
        textInputEditText3.perform(replaceText("LAPTOP1"), closeSoftKeyboard());

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.job_engineer), isDisplayed()));
        textInputEditText4.perform(replaceText("S. Linton"), closeSoftKeyboard());

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.job_description), isDisplayed()));
        textInputEditText5.perform(replaceText("Test description"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.createspinner), isDisplayed()));
        appCompatSpinner.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(android.R.id.text1), withText("1-Critical"),
                        childAtPosition(
                                allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                                        withParent(withClassName(is("android.widget.FrameLayout")))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.menu_create_clear), withContentDescription("Clear"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button2), withText("No")));
        appCompatButton.perform(scrollTo(), click());

        pressBack();

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button2), withText("No")));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.menu_create_create), withContentDescription("Create"), isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.snackbar_action), withText("Home"), isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fabCreate),
                        withParent(allOf(withId(R.id.activity_main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction textInputEditText6 = onView(
                allOf(withId(R.id.job_title), isDisplayed()));
        textInputEditText6.perform(replaceText("T"), closeSoftKeyboard());

        ViewInteraction textInputEditText7 = onView(
                allOf(withId(R.id.job_customer), isDisplayed()));
        textInputEditText7.perform(replaceText("Hahs"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView4 = onView(
                allOf(withId(R.id.menu_create_clear), withContentDescription("Clear"), isDisplayed()));
        actionMenuItemView4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("Yes")));
        appCompatButton5.perform(scrollTo(), click());

        pressBack();

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(android.R.id.button1), withText("Yes")));
        appCompatButton6.perform(scrollTo(), click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
