/**
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
 * RegisterFragment allows the user to register a new account
 * that will allow the user to later access their game status.
 *
 * @author Norris Spencer nisj@uw.edu
 * @author Keegan Wantz k@uw.edu
 *
 * @version 1.0, 11 May 2018
 *
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    /** TAG is a constant used to identify log events into the Logcat.*/
    private static final String TAG = "RegisterFragment";

    /** REGISTER_URL is a constant to store the url for the database that stores the login info.*/
    private static final String REGISTER_URL = "http://www.kairuni.com/NostalgiaPet/addUser.php?";

    /**mListener*/
    private registerListener mListener;

    /**mRUserEmail*/
    private EditText mRUserEmail;

    /**mRUserPassword*/
    private EditText mRUserPassword;

    /**mRegisterButton*/
    private Button mRegisterButton;

    /** RegisterFragment is an empty, required constructor*/
    public RegisterFragment() {    }

    /**
     * newInstance does...
     *
     * @return null.
     */
    public static RegisterFragment newInstance() { return null; }

    /** registerListener is an interface to execute registerUser*/
    public interface registerListener { void registerUser (String url); }

    /**
     * onCreate creates...
     *
     * @param savedInstanceState passes the saved instance state of...
     */
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    /**
     * onCreateView does...
     *
     * @param inflater passes the...
     * @param container passes the...
     * @param savedInstanceState passes the...
     *
     * @return the created view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        mRUserEmail = (EditText) v.findViewById(R.id.fillable_register_email_id);
        mRUserPassword = (EditText) v.findViewById(R.id.fillable_register_password);
        mRegisterButton = v.findViewById(R.id.btn_user_register);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildRegisterURL(v);
                mListener.registerUser(url);
            }
        });
        return v;
    }

    /**
     * buildRegisterURL builds a string to return the url to locate the entered email and password.
     *
     * @param v passes the current view.
     *
     * @return the built string to the method call.
     */
    private String buildRegisterURL(View v) {
        StringBuilder sb = new StringBuilder(REGISTER_URL);
        try {
            String email = mRUserEmail.getText().toString();
            sb.append("email=");
            sb.append(URLEncoder.encode(email, "UTF-8"));

            String password = mRUserPassword.getText().toString();
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
     * onAttach does...
     *
     * @param context passes the...
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof registerListener) {
            mListener = (registerListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
