package io.nearby.android.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;

import io.nearby.android.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        LoginFragment fragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
