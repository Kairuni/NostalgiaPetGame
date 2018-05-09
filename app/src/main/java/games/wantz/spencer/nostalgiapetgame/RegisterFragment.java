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

public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";
    private static final String COURSE_ADD_URL =
            "http://www.kairuni.com/NostalgiaPet/addUser.php?";

    private registerListener mListener;
    private EditText mRUserEmail;
    private EditText mRUserPassword;
    private Button mRegisterButton;

    public RegisterFragment() {

    }

    public static RegisterFragment newInstance() { return null; }

    public interface registerListener {
        public void registerUser (String url);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

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
                String url = buildCourseURL(v);
                mListener.registerUser(url);
            }
        });
        return v;
    }

    private String buildCourseURL(View v) {
        StringBuilder sb = new StringBuilder(COURSE_ADD_URL);
        try {
            String courseId = mRUserEmail.getText().toString();
            sb.append("email=");
            sb.append(URLEncoder.encode(courseId, "UTF-8"));
            String courseShortDesc = mRUserPassword.getText().toString();
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
        if (context instanceof registerListener) {
            mListener = (registerListener) context;
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
