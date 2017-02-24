package io.nearby.android.ui.settings;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import javax.inject.Inject;

import io.nearby.android.data.User;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;

public class SettingsPresenter implements SettingsContract.Presenter {

    private final SettingsContract.View mView;
    private final DataManager mDataManager;

    @Inject
    public SettingsPresenter(SettingsContract.View view, DataManager dataManager) {
        mView = view;
        mDataManager = dataManager;
    }

    @Inject
    void setupListeners(){
        mView.setPresenter(this);
    }

    @Override
    public void getUserInfo() {
        mDataManager.getUserInfo(new SpottedDataSource.UserInfoLoadedCallback() {
            @Override
            public void onUserInfoLoaded(User user) {
                mView.onUserInfoReceived(user);
            }

            @Override
            public void onError() { }
        });
    }

    @Override
    public void linkFacebookAccount(LoginResult loginResult) {
        String userId = loginResult.getAccessToken().getUserId();
        String token = loginResult.getAccessToken().getToken();

        //mDataManager.linkFacebookAccount(userId, token);
    }

    @Override
    public void linkGoogleAccount(GoogleSignInAccount account) {
        String userId = account.getId();
        String token = account.getIdToken();

        //mDataManager.linkGoogleAccount(userId, token);
    }

    @Override
    public void logout() {
        mDataManager.signOut(new SpottedDataSource.Callback() {
            @Override
            public void onSuccess() {
                mView.onSignOutCompleted();
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void deactivateAccount() {
        mDataManager.deactivateAccount(new SpottedDataSource.Callback() {
            @Override
            public void onSuccess() {
                mView.onAccountDeactivated();
            }

            @Override
            public void onError() {

            }
        });
    }
}
