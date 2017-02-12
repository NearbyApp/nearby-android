package io.nearby.android.ui.login;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.nearby.android.data.local.SharedPreferencesHelper;
import io.nearby.android.data.remote.NearbyService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Marc on 2017-02-08.
 */

public class LoginPresenter  {

    private final SharedPreferencesHelper mSharedPreferenceHelper;
    private NearbyService mNearbyService;
    private LoginView mLoginView;

    public LoginPresenter(LoginView loginView, NearbyService nearbyService, SharedPreferencesHelper sharedPreferencesHelper) {
        mLoginView = loginView;
        mNearbyService = nearbyService;
        mSharedPreferenceHelper = sharedPreferencesHelper;
    }

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

        mSharedPreferenceHelper.setFacebookUserId(loginResult.getAccessToken().getUserId());
        mSharedPreferenceHelper.setFacebookToken(loginResult.getAccessToken().getToken());
        mSharedPreferenceHelper.setLastSignInMethod(SharedPreferencesHelper.LAST_SIGN_IN_METHOD_FACEBOOK);

        login();
    }

    public void loginWithGoogle(GoogleSignInAccount account) {
        String idToken = account.getIdToken();
        String userId = account.getId();

        mSharedPreferenceHelper.setGoogleUserId(userId);
        mSharedPreferenceHelper.setGoogleToken(idToken);
        mSharedPreferenceHelper.setLastSignInMethod(SharedPreferencesHelper.LAST_SIGN_IN_METHOD_GOOGLE);

        login();
    }

    private void login(){
        mNearbyService.login().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()){
                    case 201:
                        //Account created
                        //TODO Show a tutorial?
                    case 200:
                        mLoginView.onLoginSuccessful();
                        break;
                    default:
                        Timber.d("An error occured when loging in.");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Timber.e(t);
            }
        });
    }

}