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

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 *  Tests to verify the program rejects or accepts the registration of a user as appropriate.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterFragmentTest {

    /**
     * A JUnit {@link Rule @Rule} to launch your activity under test.
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link @Before} method.
     * <p>
     * {@link ActivityTestRule} will create and launch of the activity for you and also expose
     * the activity under test. To get a reference to the activity you can use
     * the {@link ActivityTestRule#getActivity()} method.
     */
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<> (
            LoginActivity.class);

    /**
     *  Tests to verify the program opens the RegisterFragment.
     */
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

    /**
     *  Tests to verify the program will reject an invalid password.
     */
    @Test
    public void testRegisterFragmentPassShort() {
        onView(withId(R.id.fillable_register_email_id))
                .perform(new TypeTextAction("w@w.w")).perform(closeSoftKeyboard());

        onView(withId(R.id.fillable_register_password))
                .perform(new TypeTextAction("123")).perform(closeSoftKeyboard());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail("Sleep got interrupted.");
        }

        onView(withId(R.id.btn_user_register))
                .perform(click());

        onView(withText("Email and/or Password incorrect: Please enter a valid password (longer than five characters)."))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    /**
     *  Tests to verify the program will reject an invalid email format.
     */
    @Test
    public void testRegisterFragmentInvalidEmail() {
        onView(withId(R.id.fillable_register_email_id))
                .perform(new TypeTextAction("w.w.w")).perform(closeSoftKeyboard());

        onView(withId(R.id.fillable_register_password))
                .perform(new TypeTextAction("12345678")).perform(closeSoftKeyboard());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail("Sleep got interrupted.");
        }

        onView(withId(R.id.btn_user_register))
                .perform(click());

        onView(withText("Email and/or Password incorrect: Please enter a valid email."))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    /**
     *  Tests to verify the program will accept a new email that is randomly generated.
     */
    @Test
    public void testRegisterFragmentRandomEmailGeneration() {
        Random random = new Random();
        //Generate an email address
        String email = "email" + (random.nextInt(10000) + 1)
                + (random.nextInt(900) + 1) + (random.nextInt(700) + 1)
                + (random.nextInt(400) + 1) + (random.nextInt(100) + 1)
                + "@uw.edu";

        // Type text and then press the button.
        onView(withId(R.id.fillable_register_email_id))
                .perform(typeText(email)).perform(closeSoftKeyboard());
        onView(withId(R.id.fillable_register_password))
                .perform(typeText("test1@#")).perform(closeSoftKeyboard());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail("Sleep got interrupted.");
        }

        onView(withId(R.id.btn_user_register))
                .perform(click());

        onView(withText("Registration Completed!"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    /**
     *  Tests to verify the program will reject an already existing email.
     */
    @Test
    public void testRegistrationFragmentAlreadyExistEmail() {
        onView(withId(R.id.fillable_register_email_id))
                .perform(new TypeTextAction("w@w.w")).perform(closeSoftKeyboard());

        onView(withId(R.id.fillable_register_password))
                .perform(new TypeTextAction("12345678")).perform(closeSoftKeyboard());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail("Sleep got interrupted.");
        }

        onView(withId(R.id.btn_user_register))
                .perform(click());

        onView(withText("Email and/or Password incorrect: Failed to add the user."))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }
}