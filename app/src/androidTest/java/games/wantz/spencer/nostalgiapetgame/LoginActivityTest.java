package games.wantz.spencer.nostalgiapetgame;

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
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<> (
            LoginActivity.class);

    @Before
    public void testLaunchSignInFragment() {
        onView(withId(R.id.btn_login)).perform(click());
    }

    @Test
    public void testSignInFragmentInvalid() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        onView(withId(R.id.fillable_login_email_id))
                .perform(typeText("w@w.w"));
        onView(withId(R.id.fillable_login_password))
                .perform(typeText(""));
        onView(withId(R.id.btn_user_login))
                .perform(click());

        onView(withText("Unable to login: Invalid password"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Ignore
    @Test
    public void registerUser() {
    }

    @Ignore
    @Test
    public void openRegisterFragment() {
    }

    @Ignore
    @Test
    public void openLoginFragment() {
    }
}