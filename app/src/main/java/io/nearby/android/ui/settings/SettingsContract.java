package io.nearby.android.ui.settings;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.nearby.android.data.User;
import io.nearby.android.ui.BaseView;

public interface SettingsContract {

    interface View extends BaseView<Presenter>{
        void onUserInfoReceived(User user);

        void onGoogleAccountLinked();
        void onFacebookAccountLinked();

        void onSignOutCompleted();
        void onAccountDeactivated();

        void onGoogleAccountAlreadyExist(String userId, String token);
        void onFacebookAccountAlreadyExist(String userId, String token);

        void onGoogleAccountMerged();
        void onFacebookAccountMerged();

        void linkAccountFailed();

        void deactivateAccountFailed();

        void mergeAccountFailed();
    }

    interface Presenter{
        void getUserInfo();

        void linkFacebookAccount(LoginResult loginResult);
        void linkGoogleAccount(GoogleSignInAccount account);

        void logout();
        void deactivateAccount();

        void mergeFacebookAccount(String userId, String token);

        void mergeGoogleAccount(String userId, String token);
    }
}
