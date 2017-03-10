package io.nearby.android.ui.login;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import javax.inject.Inject;

import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;

public class LoginPresenter implements LoginContract.Presenter {

    private DataManager mDataManager;
    private LoginContract.View mView;

    @Inject
    public LoginPresenter(LoginContract.View loginView, DataManager dataManager) {
        mView = loginView;
        mDataManager = dataManager;
    }

    @Inject
    void setupListeners(){ mView.setPresenter(this);}

    @Override
    public void loginWithFacebook(LoginResult loginResult) {
        /*
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                // Application code
                Timber.d("Graph request completed");
                if(response.getError() == null){
                    try {
                        String id = object.getString("id");
                        String name = object.getString("name");
                        String email = object.getString("email");
                    } catch (JSONException e) {
                        Timber.e("Graph request failed");
                    }
                }
            }
        });


        Bundle param = new Bundle();
        param.putString("fields","id,name,email");
        request.setParameters(param);
        request.executeAsync();
        */

        String token = loginResult.getAccessToken().getToken();
        String userId = loginResult.getAccessToken().getUserId();

        mDataManager.facebookLogin(userId,token,
                new SpottedDataSource.LoginCallback() {
                    @Override
                    public void onAccountCreated() {
                        mView.onLoginSuccessful();
                    }

                    @Override
                    public void onLoginSuccess() {
                        mView.onLoginSuccessful();
                    }

                    @Override
                    public void onError(SpottedDataSource.ErrorType errorType) {
                        mView.onLoginFailed();
                    }
                });


    }

    @Override
    public void loginWithGoogle(GoogleSignInAccount account) {
        String idToken = account.getIdToken();
        String userId = account.getId();

        mDataManager.googleLogin(userId, idToken, new SpottedDataSource.LoginCallback() {
            @Override
            public void onAccountCreated() {
                mView.onLoginSuccessful();
            }

            @Override
            public void onLoginSuccess() {
                mView.onLoginSuccessful();
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                mView.onLoginFailed();
            }
        });
    }
}