package com.example.android.testing.espresso.MultiWindowSample;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class TestingWindow {

    @Rule
    public ActivityTestRule<SuggestActivity> mActivityRule = new ActivityTestRule<>(
            SuggestActivity.class);

    private SuggestActivity mActivity = null;

    @Before
    public void setActivity() {
        mActivity = mActivityRule.getActivity();
    }

    @Test
    public void autoCompleteTextView_twoSuggestions() {
        // Type "So" to trigger two suggestions.
        onView(withId(R.id.auto_complete_text_view))
                .perform(typeText("So"), closeSoftKeyboard());

        // Check that both suggestions are displayed.
        onView(withText("South China Sea"))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(withText("Southern Ocean"))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void autoCompleteTextView_oneSuggestion() {
        // Type "South" to trigger one suggestion.
        onView(withId(R.id.auto_complete_text_view))
                .perform(typeTextIntoFocusedView("South "), closeSoftKeyboard());

        // Should be displayed
        onView(withText("South China Sea"))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        // Should not be displayed.
        onView(withText("Southern Ocean"))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(doesNotExist());
    }

    @Test
    public void autoCompleteTextView_clickAndCheck() {
        // Type text into the text view
        onView(withId(R.id.auto_complete_text_view))
                .perform(typeTextIntoFocusedView("South "), closeSoftKeyboard());

        // Tap on a suggestion.
        onView(withText("South China Sea"))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .perform(click());

        // By clicking on the auto complete term, the text should be filled in.
        onView(withId(R.id.auto_complete_text_view))
                .check(matches(withText("South China Sea")));
    }
}
