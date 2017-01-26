package io.nearby.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Marc on 2017-01-26.
 */

public class SignInManager {

    public static final int LAST_SIGN_IN_METHOD_NONE = 0;
    public static final int LAST_SIGN_IN_METHOD_GOOGLE = 1;
    public static final int LAST_SIGN_IN_METHOD_FACEBOOK = 2;

    private SignInManager() {}

    public static boolean hasUserAlreadySignedIn(Activity activity){
        boolean hasAlready = false;
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

        hasAlready = sharedPref.contains(activity.getString(R.string.pref_last_social_login_used));

        if(hasAlready){
            hasAlready = sharedPref.getInt(activity.getString(R.string.pref_last_social_login_used),LAST_SIGN_IN_METHOD_NONE) != LAST_SIGN_IN_METHOD_NONE;
        }
        else {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(activity.getString(R.string.pref_last_social_login_used), LAST_SIGN_IN_METHOD_NONE);
            editor.commit();
        }

        return hasAlready;
    }

    public static int getLastSignInMethod(Activity activity){
        int lastMethod = LAST_SIGN_IN_METHOD_NONE;

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        if(sharedPref.contains(activity.getString(R.string.pref_last_social_login_used))){
            lastMethod = sharedPref.getInt(activity.getString(R.string.pref_last_social_login_used),LAST_SIGN_IN_METHOD_NONE);
        }

        return lastMethod;
    }

    public static void updateLastSignInMethod(Activity activity, int method){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(activity.getString(R.string.pref_last_social_login_used), method);
        editor.commit();
    }

}
