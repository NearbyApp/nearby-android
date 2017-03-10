package io.nearby.android.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import io.nearby.android.R;
import io.nearby.android.ui.launcher.LauncherActivity;

public abstract class BaseActivity<T> extends AppCompatActivity implements BaseView<T> {

    @Override
    public void onUserAccountDisabled() {
        showAccountAlreadyDisabledDialog(this);
    }

    @Override
    public void onUserUnauthorized() {
        Intent intent = new Intent(this, LauncherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public static final void showAccountAlreadyDisabledDialog(final Activity activity){
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(activity, LauncherActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                })
                .create()
                .show();
    }
}
