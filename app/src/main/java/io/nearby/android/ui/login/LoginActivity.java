package io.nearby.android.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import io.nearby.android.R;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        LoginFragment fragment = new LoginFragment();
        fragment.setLoginListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onGoogleLogin(GoogleSignInAccount account) {
        String id = account.getId();
        String name = account.getDisplayName();
        String email = account.getEmail();
        String idToken = account.getIdToken();
        //TODO Make API call
    }

    @Override
    public void onFacebookLogin(LoginResult loginResult) {
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
}
