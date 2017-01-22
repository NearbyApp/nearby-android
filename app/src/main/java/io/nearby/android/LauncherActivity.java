package io.nearby.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.nearby.android.login.LoginActivity;

/**
 * Created by Marc on 2017-01-22.
 */

public class LauncherActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;
        //TODO Create algo to validate if the user is logged in
        boolean isLoggedIn = false;

        if(isLoggedIn){
            intent = new Intent(this, LoginActivity.class);
        }
        else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
