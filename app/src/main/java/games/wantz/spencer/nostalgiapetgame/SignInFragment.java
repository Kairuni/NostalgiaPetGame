/*
 * TCSS450 Mobile Applications, Spring 2018
 * Group Project - Nostalgia Pet
 */
package games.wantz.spencer.nostalgiapetgame;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;

/**
 * SignInFragment allows the user to login to their account
 * which will allow the user to access their game status.
 *
 * @author Norris Spencer nisj@uw.edu
 * @author Keegan Wantz wantzkt@uw.edu
 *
 * @version 0.1, 11 May 2018
 *
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    /** TAG is a constant used to identify log events into the Logcat.*/
    private static final String TAG = "SignInFragment";

    /** SIGN_IN_URL is a constant to store the url for the database that stores the login info.*/
    private static final String SIGN_IN_URL = "http://www.kairuni.com/NostalgiaPet/login.php?";

    /**
     * mListener is a var that allows us to use the activity for callbacks.
     */
    private signInListener mListener;

    /**
     * mSIUserEmail is a var that stores a reference to the EditText for getting the email.
     */
    private EditText mSIUserEmail;

    /**
     * mSIUserPassword is a var that stores a reference to the EditText for getting the password.
     */
    private EditText mSIUserPassword;

    /** mSignInButton is a var that stores a reference to the button for callbacks. */
    private Button mSignInButton;

    /** SignInFragment is an empty, required constructor*/
    public SignInFragment() {
    }

    /**
     * newInstance does nothing. Unused.
     *
     * @return null.
     */
    public static SignInFragment newInstance() { return null; }

    /** signInListener is an interface to execute signInUser*/
    public interface signInListener {
        void signInUser(String url);
    }

    /**
     * onCreate passes the state to the parent.
     *
     * @param savedInstanceState passes the saved instance state of...
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * onCreateView retrieves references for the fragment's EditTexts, and sets up a callback via the
     * SignInListener interface.
     *
     * @param inflater The inflater for this view.
     * @param container The container for this view.
     * @param savedInstanceState Previously saved instance state.
     *
     * @return the created view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mSIUserEmail = v.findViewById(R.id.fillable_login_email_id);
        mSIUserPassword = v.findViewById(R.id.fillable_login_password);
        mSignInButton = v.findViewById(R.id.btn_user_login);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildSignInURL(v);
                mListener.signInUser(url);
            }
        });
        return v;
    }

    /**
     * buildSignInURL builds a string to return the url to locate the entered email and password.
     *
     * @param v passes the current view.
     *
     * @return the built string to the method call.
     */
    private String buildSignInURL(View v) {
        StringBuilder sb = new StringBuilder(SIGN_IN_URL);
        try {
            String email = mSIUserEmail.getText().toString();
            sb.append("email=");
            sb.append(URLEncoder.encode(email, "UTF-8"));
            String password = mSIUserPassword.getText().toString();
            sb.append("&password=");
            sb.append(URLEncoder.encode(password, "UTF-8"));
            Log.i(TAG, sb.toString());
        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        return sb.toString();
    }

    /**
     * onAttach ensures that the parent implemented the required signInListener interface.
     *
     * @param context the current context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof signInListener) {
            mListener = (signInListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}