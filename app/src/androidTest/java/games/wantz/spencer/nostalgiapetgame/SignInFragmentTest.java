package games.wantz.spencer.nostalgiapetgame;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignInFragmentTest {
    /**
     * A JUnit {@link Rule @Rule} to launch your activity under test.
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link @Before} method.
     * <p>
     * {@link ActivityTestRule} will create and launch of the activity for you and also expose
     * the activity under test. To get a reference to the activity you can use
     * the {@link ActivityTestRule#getActivity()} method
     */
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Test
    public void newInstance() {
    }

    @Test
    public void onCreate() {
    }

    @Test
    public void onCreateView() {
    }

    @Test
    public void onAttach() {
    }
}