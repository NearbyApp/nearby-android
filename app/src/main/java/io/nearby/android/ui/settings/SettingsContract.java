package io.nearby.android.ui.settings;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.nearby.android.data.User;
import io.nearby.android.ui.BaseView;

public interface SettingsContract {

    interface View extends BaseView<Presenter>{
        void onUserInfoReceived(User user);

        void onSignOutCompleted();
        void onAccountDeactivated();
    }

    interface Presenter{
        void getUserInfo();

        void linkFacebookAccount(LoginResult loginResult);
        void linkGoogleAccount(GoogleSignInAccount account);

        void logout();
        void deactivateAccount();
    }
}
