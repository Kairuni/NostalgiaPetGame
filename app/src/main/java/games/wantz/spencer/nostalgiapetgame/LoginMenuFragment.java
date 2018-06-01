/*
 * TCSS450 Mobile Applications, Spring 2018
 * Group Project - Nostalgia Pet
 */
package games.wantz.spencer.nostalgiapetgame;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * LoginMenuFragment supports the Sign in and Register Fragments by directing the user to the
 * desired action.
 *
 * @author Norris Spencer nisj@uw.edu
 * @author Keegan Wantz wantzkt@uw.edu
 *
 * @version 1.B, 31 May 2018
 *
 * A simple {@link Fragment} subclass.
 */
public class LoginMenuFragment extends Fragment {

    /** mSListener is a member var that listens for the selection of the "SignIn" button.*/
    private loginButtonListener mSListener;
    /** mRListener is a member var that listens for the selection of the "Create Acct" button.*/
    private registerButtonListener mRListener;

    /** LoginMenuFragment is an empty, required constructor. */
    public LoginMenuFragment() {
    }

    /** registerButtonListener is an interface to execute openRegisterFragment. */
    public interface registerButtonListener {
        void openRegisterFragment();
    }

    /** loginButtonListener is an interface to execute openLoginFragment. */
    public interface loginButtonListener {
        void openLoginFragment();
    }

    /**
     * onAttach ensures that the parent implemented the required registerButtonListener
     *  and loginButtonListener interfaces.
     *
     * @param context the current context.
     */
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

    /**
     * onCreateView retrieves the button views and sets callbacks to open the register or sign in fragments.
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
        View v = inflater.inflate(R.layout.fragment_menu_login, container, false);
        Button mRButton = v.findViewById(R.id.btn_sign_up);
        Button mSButton = v.findViewById(R.id.btn_login);

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