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

    public LoginPresenter(NearbyService nearbyService, SharedPreferencesHelper sharedPreferencesHelper, LoginView loginView) {
        mNearbyService = nearbyService;
        mSharedPreferenceHelper = sharedPreferencesHelper;
        mLoginView = loginView;
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

        mSharedPreferenceHelper.setFacebookToken(loginResult.getAccessToken().getToken());
        mSharedPreferenceHelper.setLastSignInMethod(SharedPreferencesHelper.LAST_SIGN_IN_METHOD_FACEBOOK);

        mNearbyService.loginWithFacebook().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mLoginView.onLoginSuccessful();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Timber.e(t);
            }
        });
    }

    public void loginWithGoogle(GoogleSignInAccount account) {
        String idToken = account.getIdToken();

        mSharedPreferenceHelper.setGoogleToken(idToken);
        mSharedPreferenceHelper.setLastSignInMethod(SharedPreferencesHelper.LAST_SIGN_IN_METHOD_GOOGLE);

        mNearbyService.loginWithGoogle().enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                mLoginView.onLoginSuccessful();
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }



}