/*
 * TCSS450 Mobile Applications, Spring 2018
 * Group Project - Nostalgia Pet
 */
package games.wantz.spencer.nostalgiapetgame;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * LoginActivity coordinates the Login Menu, Sign In, and Register Fragments.
 *
 * @author Norris Spencer nisj@uw.edu
 * @author Keegan Wantz wantzkt@uw.edu
 *
 * A simple {@link RegisterFragment.registerListener} subclass.
 * A simple {@link SignInFragment.signInListener} subclass.
 * A simple {@link LoginMenuFragment.registerButtonListener} subclass.
 * A simple {@link LoginMenuFragment.loginButtonListener} subclass.
 *
 * @version 0.1, 11 May 2018
 */
public class LoginActivity
        extends AppCompatActivity
        implements RegisterFragment.registerListener,
                    SignInFragment.signInListener,
                    LoginMenuFragment.registerButtonListener,
                    LoginMenuFragment.loginButtonListener {
    /**
     * Debug tag for Logging.
     */
    private static String DEBUG_TAG = "L_ACT";

    /**
     * Async Task for logging in.
     */
    private LoginTask mLoginTask;
    /**
     * Async Task for registering.
     */
    private RegisterTask mRegisterTask;

    /**
     * Constructor that initializes fields.
     */
    public LoginActivity() {
        mLoginTask = null;
        mRegisterTask = null;
    }

    /**
     * onCreate creates the LoginActivity
     *
     * @param savedInstanceState passes the saved instance state of...
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LoginMenuFragment loginMenuFragment = new LoginMenuFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.login_fragment_container, loginMenuFragment)
                .commit();
    }

    /**
     * signInUser instantiates a new LoginTask to allow the user to login to their acct.
     *
     * @param url passes... (the user entered information???)
     */
    @Override
    public void signInUser(String url) {
        if (mLoginTask == null) {
            mLoginTask = new LoginTask();
            mLoginTask.execute(new String[]{url.toString()});
        }
        getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * registerUser creates a new RegisterTask and uses that to register a new user.
     *
     * @param url passes the url provided by the user.
     */
    @Override
    public void registerUser(String url) {
        if (mRegisterTask == null) {
            mRegisterTask = new RegisterTask();
            mRegisterTask.execute(new String[]{url.toString()});
        }
        getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * openRegisterFragment creates a new RegisterFragment and swaps to that, allowing
     * a user to register for the game.
     */
    @Override
    public void openRegisterFragment() {
        RegisterFragment registerFragment = new RegisterFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_fragment_container, registerFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * openLoginFragment creates a new LoginFragment and swaps to that, allowing
     * a user to log in to the game.
     */
    @Override
    public void openLoginFragment() {
        SignInFragment signInFragment = new SignInFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_fragment_container, signInFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * RegisterTask is an inner class that handles http requests for registering new users.
     *
     * A simple {@link AsyncTask} subclass.
     */
    private class RegisterTask extends AsyncTask<String, Void, String> {
        /**
         * onPreExecute makes a call to super in order to... (What?)
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * doInBackground retrieves data from the HTTP socket, and then closes it.
         *
         * @param urls passes the website url.
         *
         * @return the JSON string returned from the website.
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to register account. Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            Log.d(DEBUG_TAG, "Background completed with: " + response);
            return response;
        }


        /**
         * onPostExecute checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result passes the result.
         */
        @Override
        protected void onPostExecute(String result) {
            mRegisterTask = null;
            // Something wrong with the network or the URL.
            try {
                Log.d(DEBUG_TAG, "ON POST EXECUTE");
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Registration Completed!"
                            , Toast.LENGTH_LONG)
                            .show();

                } else {
                    Log.d(DEBUG_TAG, "Fail");
                    Toast.makeText(getApplicationContext(), "Email and/or Password incorrect: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();

                }
            } catch (JSONException e) {
                Log.d(DEBUG_TAG, "JSON exception");
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * LoginTask is an inner class that handles login attempts.
     *
     * A {@link RegisterTask} subclass that only changes onPostExecute.
     */
    private class LoginTask extends RegisterTask {
        /**
         * onPostExecute checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result passes the result.
         */
        @Override
        protected void onPostExecute(String result) {
            mLoginTask = null;
            try {
                Log.d(DEBUG_TAG, "ON POST EXECUTE 2");
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Login Accepted!"
                            , Toast.LENGTH_LONG)
                            .show();

                    // Push all the retrieved pet's data to the GameActivity.
                    Intent intent = new Intent(getBaseContext(), GameActivity.class);
                    intent.putExtra(GameActivity.UID, (String)jsonObject.get(GameActivity.UID));
                    intent.putExtra(GameActivity.BREED, Integer.valueOf((String)jsonObject.get(GameActivity.BREED)));
                    intent.putExtra(GameActivity.MAX_HEALTH, Float.valueOf((String)jsonObject.get(GameActivity.MAX_HEALTH)));
                    intent.putExtra(GameActivity.HEALTH, Float.valueOf((String)jsonObject.get(GameActivity.HEALTH)));
                    intent.putExtra(GameActivity.MAX_STAMINA, Float.valueOf((String)jsonObject.get(GameActivity.MAX_STAMINA)));
                    intent.putExtra(GameActivity.STAMINA, Float.valueOf((String)jsonObject.get(GameActivity.STAMINA)));
                    intent.putExtra(GameActivity.MAX_HUNGER, Float.valueOf((String)jsonObject.get(GameActivity.MAX_HUNGER)));
                    intent.putExtra(GameActivity.HUNGER, Float.valueOf((String)jsonObject.get(GameActivity.HUNGER)));
                    intent.putExtra(GameActivity.MAX_BLADDER, Float.valueOf((String)jsonObject.get(GameActivity.MAX_BLADDER)));
                    intent.putExtra(GameActivity.BLADDER, Float.valueOf((String)jsonObject.get(GameActivity.BLADDER)));


                    startActivity(intent);
                    finish();

                } else {
                    Log.d(DEBUG_TAG, "Fail 2");
                    Toast.makeText(getApplicationContext(), "Email and/or Password incorrect."
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();

                }
            } catch (JSONException e) {
                Log.d(DEBUG_TAG, "Fail 4");
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}