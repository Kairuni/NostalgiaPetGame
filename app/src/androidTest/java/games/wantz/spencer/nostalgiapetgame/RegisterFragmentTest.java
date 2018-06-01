package games.wantz.spencer.nostalgiapetgame;

import android.support.test.espresso.action.TypeTextAction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterFragmentTest {

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
        onView(withId(R.id.btn_sign_up)).perform(click());
    }

    @Test
    public void testRegisterFragmentPassShort() {
        onView(withId(R.id.fillable_register_email_id))
                .perform(new TypeTextAction("w@w.w")).perform(closeSoftKeyboard());

        onView(withId(R.id.fillable_register_password))
                .perform(new TypeTextAction("123")).perform(closeSoftKeyboard());

        onView(withId(R.id.btn_user_register))
                .perform(click());

        onView(withText("Email and/or Password incorrect: Please enter a valid password (longer than five characters)."))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterFragmentInvalidEmail() {
        onView(withId(R.id.fillable_register_email_id))
                .perform(new TypeTextAction("w.w.w")).perform(closeSoftKeyboard());

        onView(withId(R.id.fillable_register_password))
                .perform(new TypeTextAction("12345678")).perform(closeSoftKeyboard());

        onView(withId(R.id.btn_user_register))
                .perform(click());

        onView(withText("Email and/or Password incorrect: Please enter a valid email."))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }
}