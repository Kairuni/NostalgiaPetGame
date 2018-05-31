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
 * @author Keegan Wantz wantzkt@uw.edu
 *
 * @version 0.1, 11 May 2018
 *
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    //Final Field Variables
    /** TAG is a constant used to identify log events into the Logcat. */
    private static final String TAG = "RegisterFragment";
    /** REGISTER_URL is a constant to store the url for the database that stores the login info. */
    private static final String REGISTER_URL = "http://www.kairuni.com/NostalgiaPet/addUser.php?";

    //Non-Final Field Variables
    /** Button used for registration. */
    private Button mRegisterButton;
    /** EditText for retrieving the user's email. */
    private EditText mRUserEmail;
    /** EditText for retrieving the user's password. */
    private EditText mRUserPassword;
    /** Allows the fragment to use the Activity when the button is pushed. */
    private registerListener mListener;

    /** RegisterFragment is an empty, required constructor. */
    public RegisterFragment() {
    }

    /**
     * newInstance does nothing. Unused.
     *
     * @return null.
     */
    public static RegisterFragment newInstance() {
        return null;
    }

    /** registerListener is an interface to execute registerUser. */
    public interface registerListener {
        void registerUser(String url);
    }

    /**
     * onCreate passes the state to the parent.
     *
     * @param savedInstanceState passes the saved instance state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * onCreateView retrieves references for the fragment's EditTexts, and sets up a callback via the
     * registerListener interface.
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
     * onAttach ensures that the parent implemented the required registerListener interface.
     *
     * @param context the current context.
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
