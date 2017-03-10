package io.nearby.android.ui.login;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.nearby.android.ui.BaseView;

/**
 * Created by Marc on 2017-02-16.
 */

public interface LoginContract {

    interface View extends BaseView<Presenter>{
        void onLoginSuccessful();
        void onLoginFailed();
    }

    interface Presenter{
        void loginWithFacebook(LoginResult loginResult);
        void loginWithGoogle(GoogleSignInAccount account);
    }
}
