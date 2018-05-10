package games.wantz.spencer.nostalgiapetgame;

import android.content.Context;
import android.net.Uri;
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

public class SignInFragment extends Fragment {

    private static final String TAG = "SignInFragment";
    private static final String COURSE_ADD_URL =
            "http://www.kairuni.com/NostalgiaPet/login.php?";

    private signInListener mListener;
    private EditText mSIUserEmail;
    private EditText mSIUserPassword;
    private Button mLoginButton;

    public SignInFragment() {

    }

    public static SignInFragment newInstance() { return null; }

    public interface signInListener {
        void loginUser (String url);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mSIUserEmail = v.findViewById(R.id.fillable_login_email_id);
        mSIUserPassword = v.findViewById(R.id.fillable_login_password);
        mLoginButton = v.findViewById(R.id.btn_user_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildCourseURL(v);
                mListener.loginUser(url);
            }
        });
        return v;
    }

    private String buildCourseURL(View v) {
        StringBuilder sb = new StringBuilder(COURSE_ADD_URL);
        try {
            String courseId = mSIUserEmail.getText().toString();
            sb.append("email=");
            sb.append(URLEncoder.encode(courseId, "UTF-8"));
            String courseShortDesc = mSIUserPassword.getText().toString();
            sb.append("&password=");
            sb.append(URLEncoder.encode(courseShortDesc, "UTF-8"));
            Log.i(TAG, sb.toString());
        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        return sb.toString();
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
