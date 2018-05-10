package games.wantz.spencer.nostalgiapetgame;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginMenuFragment extends Fragment {

    private registerButtonListener mRListener;
    private loginButtonListener mSListener;
    private Button mRButton;
    private Button mSButton;


    public LoginMenuFragment() {
        // Required empty public constructor
    }

    public interface registerButtonListener {
        void openRegisterFragment ();
    }


    public interface loginButtonListener {
        void openLoginFragment ();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof registerButtonListener) {
            mRListener = (registerButtonListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ");
        }
        if (context instanceof loginButtonListener) {
            mSListener = (loginButtonListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_login, container, false);
        mRButton = v.findViewById(R.id.btn_sign_up);
        mSButton = v.findViewById(R.id.btn_login);

        mRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRListener.openRegisterFragment();
            }
        });
        mSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSListener.openLoginFragment();
            }
        });
        return v;
    }

}
