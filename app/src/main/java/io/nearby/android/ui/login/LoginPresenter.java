package io.nearby.android.ui.login;

import android.os.Bundle;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import io.nearby.android.ui.Presenter;
import timber.log.Timber;

/**
 * Created by Marc on 2017-02-08.
 */

public class LoginPresenter extends Presenter<LoginView>  {

    public LoginPresenter() { }

    public void loginWithFacebook(LoginResult loginResult) {
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

        //TODO Make API call
    }

    public void loginWithGoogle(GoogleSignInAccount account) {
        String id = account.getId();
        String idToken = account.getIdToken();
        // String name = account.getDisplayName();
        // String email = account.getEmail();
        // TODO Make API call

    }



}