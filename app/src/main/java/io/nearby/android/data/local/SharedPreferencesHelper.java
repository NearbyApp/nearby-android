package io.nearby.android.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import io.nearby.android.R;


/**
 * Created by Marc on 2017-02-08.
 */

public class SharedPreferencesHelper {

    private static final String SHARED_PREF_FILE_NAME = "nearby_prefs";

    public static final int LAST_SIGN_IN_METHOD_NONE = 0;
    public static final int LAST_SIGN_IN_METHOD_GOOGLE = 1;
    public static final int LAST_SIGN_IN_METHOD_FACEBOOK = 2;

    private static SharedPreferencesHelper instance;

    private SharedPreferences mPrefs;
    private Context mContext;

    public SharedPreferencesHelper(){
    }

    public static SharedPreferencesHelper getInstance(){
        if(instance == null){
            instance = new SharedPreferencesHelper();
        }

        return instance;
    }

    public void setContext(Context context){
        mPrefs = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        mContext = context;
    }

    public SharedPreferences getSharedPref(){
        if(mPrefs == null){
            throw new IllegalStateException("Can't get shared prefs if the context is not set.");
        }

        return mPrefs;
    }

    public boolean hasUserAlreadySignedIn(){
        boolean hasAlready = false;
        SharedPreferences sharedPref = SharedPreferencesHelper.getInstance().getSharedPref();

        hasAlready = sharedPref.contains(mContext.getString(R.string.pref_last_social_login_used));

        if(hasAlready){
            hasAlready = sharedPref.getInt(mContext.getString(R.string.pref_last_social_login_used),LAST_SIGN_IN_METHOD_NONE) != LAST_SIGN_IN_METHOD_NONE;
        }
        else {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(mContext.getString(R.string.pref_last_social_login_used), LAST_SIGN_IN_METHOD_NONE);
            editor.commit();
        }

        return hasAlready;
    }

    public int getLastSignInMethod(){
        int lastMethod = LAST_SIGN_IN_METHOD_NONE;

        SharedPreferences sharedPref = SharedPreferencesHelper.getInstance().getSharedPref();
        if(sharedPref.contains(mContext.getString(R.string.pref_last_social_login_used))){
            lastMethod = sharedPref.getInt(mContext.getString(R.string.pref_last_social_login_used),LAST_SIGN_IN_METHOD_NONE);
        }

        return lastMethod;
    }

    public void updateLastSignInMethod(int method){
        SharedPreferences sharedPref = SharedPreferencesHelper.getInstance().getSharedPref();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(mContext.getString(R.string.pref_last_social_login_used), method);
        editor.commit();
    }


}
