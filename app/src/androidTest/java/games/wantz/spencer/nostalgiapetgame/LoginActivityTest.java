package games.wantz.spencer.nostalgiapetgame;

import android.support.test.espresso.action.TypeTextAction;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<> (
            LoginActivity.class);

    @Before
    public void testLaunchSignInFragment() {
        try {
            onView(withId(R.id.button_logout))
                    .perform(click());
        } catch (Exception e) {
            // Not worried about this
        }
        onView(withId(R.id.btn_login)).perform(click());
    }

    @Test
    public void testSignInFragmentInvalidPass() {
        onView(withId(R.id.fillable_login_email_id))
                .perform(new TypeTextAction("w@w.w")).perform(closeSoftKeyboard());

        onView(withId(R.id.fillable_login_password))
                .perform(new TypeTextAction("")).perform(closeSoftKeyboard());

        onView(withId(R.id.btn_user_login))
                .perform(click());

        onView(withText("Email and/or Password incorrect."))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testSignInFragmentInvalidEmail() {
        onView(withId(R.id.fillable_login_email_id))
                .perform(new TypeTextAction("w.w.w")).perform(closeSoftKeyboard());

        onView(withId(R.id.fillable_login_password))
                .perform(new TypeTextAction("12345678")).perform(closeSoftKeyboard());

        onView(withId(R.id.btn_user_login))
                .perform(click());

        onView(withText("Email and/or Password incorrect."))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testSignInFragmentValid() {
        onView(withId(R.id.fillable_login_email_id))
                .perform(new TypeTextAction("w@w.w")).perform(closeSoftKeyboard());

        onView(withId(R.id.fillable_login_password))
                .perform(new TypeTextAction("123456")).perform(closeSoftKeyboard());

        //onView(withId(R.id.fillable_login_password)).perform(closeSoftKeyboard());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail("Sleep got interrupted.");
        }

        onView(withId(R.id.btn_user_login))
                .perform(click());

        onView(allOf(withId(R.id.button_logout)
                , withText("LOGOUT")))
                .check(matches(isDisplayed()));


        onView(withId(R.id.button_logout))
                .perform(click());
    }
}