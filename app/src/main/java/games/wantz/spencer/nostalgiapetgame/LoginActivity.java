/**
 * TCSS450 Mobile Applications, Spring 2018
 * Group Project - Nostalgia Pet
 */
package games.wantz.spencer.nostalgiapetgame;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
 * @author Keegan Wantz k@uw.edu
 *
 * A simple {@link RegisterFragment.registerListener} subclass.
 * A simple {@link SignInFragment.signInListener} subclass.
 * A simple {@link LoginMenuFragment.registerButtonListener} subclass.
 * A simple {@link LoginMenuFragment.loginButtonListener} subclass.
 *
 * @version 1.0, 11 May 2018
 */
public class LoginActivity
        extends AppCompatActivity
        implements RegisterFragment.registerListener,
                    SignInFragment.signInListener,
                    LoginMenuFragment.registerButtonListener,
                    LoginMenuFragment.loginButtonListener {

    /**
     * onCreate creates...
     *
     * @param savedInstanceState passes the saved instance state of...
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        LoginTask task = new LoginTask();
        task.execute(new String[]{url.toString()});
        getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * registerUser does...
     *
     * @param url passes...
     */
    @Override
    public void registerUser(String url) {
        LoginTask task = new LoginTask();
        task.execute(new String[]{url.toString()});
        getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * openRegisterFragment does...
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
     * openLoginFragment does...
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
     * LoginTask is an inner class that
     *
     * A simple {@link AsyncTask} subclass.
     */
    private class LoginTask extends AsyncTask<String, Void, String> {

        /**
         * onPreExecute makes a call to super in order to... (What?)
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * doInBackground does...
         *
         * @param urls passes...
         *
         * @return the created string.
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
                    response = "Unable to add course, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
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
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Login Accepted!"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Email and/or Password incorrect."
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}