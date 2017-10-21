package com.example.ahmed.thebakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.ahmed.thebakingapp.Activities.RecipesActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<RecipesActivity> activityTestRule =
            new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void recipeListActivityTest() {
        onView(allOf(withId(R.id.recipesRecycler),isDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.recipesRecycler))).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(allOf(withId(R.id.recipesInfoRecycler),isDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.recipesInfoRecycler))).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
    }
}
