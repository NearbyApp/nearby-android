package io.nearby.android.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import io.nearby.android.R;
import io.nearby.android.ui.launcher.LauncherActivity;

public abstract class BaseFragment<T> extends Fragment implements BaseView<T> {
    @Override
    public void onUserAccountDisabled() {
        showAccountAlreadyDisabledDialog(getActivity());
    }

    @Override
    public void onUserUnauthorized() {
        Intent intent = new Intent(getActivity(), LauncherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
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
