package io.nearby.android.ui.login;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.nearby.android.data.source.local.SharedPreferencesHelper;
import io.nearby.android.data.source.remote.NearbyService;
import io.nearby.android.ui.Presenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Marc on 2017-02-08.
 */

public class LoginPresenter extends Presenter {

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
        Observable<Response<ResponseBody>> call = mNearbyService.login();
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> response) throws Exception {
                        switch(response.code()){
                            case 200:
                                //Normal login
                                mLoginView.onLoginSuccessful();
                                break;
                            case 201:
                                // Account created
                                mLoginView.onLoginSuccessful();
                                break;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);
                    }
                });

        mCompositeDisposable.add(disposable);
    }
}