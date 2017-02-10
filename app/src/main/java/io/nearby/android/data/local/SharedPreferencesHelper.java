package io.nearby.android.data.local;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.nearby.android.R;


/**
 * Created by Marc on 2017-02-08.
 */
@Singleton
public class SharedPreferencesHelper {

    private static final String SHARED_PREF_FILE_NAME = "nearby_prefs";

    public static final int LAST_SIGN_IN_METHOD_NONE = 0;
    public static final int LAST_SIGN_IN_METHOD_GOOGLE = 1;
    public static final int LAST_SIGN_IN_METHOD_FACEBOOK = 2;

    private SharedPreferences mPrefs;
    private Context mContext;

    @Inject
    public SharedPreferencesHelper(Application application){
        mContext = application;
        mPrefs = mContext.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public boolean hasUserAlreadySignedIn(){
        boolean hasAlready = false;

        hasAlready = mPrefs.contains(mContext.getString(R.string.pref_last_social_login_used));

        if(hasAlready){
            hasAlready = mPrefs.getInt(mContext.getString(R.string.pref_last_social_login_used),LAST_SIGN_IN_METHOD_NONE) != LAST_SIGN_IN_METHOD_NONE;
        }
        else {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putInt(mContext.getString(R.string.pref_last_social_login_used), LAST_SIGN_IN_METHOD_NONE);
            editor.commit();
        }

        return hasAlready;
    }

    public int getLastSignInMethod(){
        int lastMethod = LAST_SIGN_IN_METHOD_NONE;

        if(mPrefs.contains(mContext.getString(R.string.pref_last_social_login_used))){
            lastMethod = mPrefs.getInt(mContext.getString(R.string.pref_last_social_login_used),LAST_SIGN_IN_METHOD_NONE);
        }

        return lastMethod;
    }

    public void setLastSignInMethod(int method){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(mContext.getString(R.string.pref_last_social_login_used), method);
        editor.commit();
    }


    /*
     *  Token Management
     */

    private String getToken(@StringRes int prefKey, String defaultValue){
        String token = "";

        if(mPrefs.contains(mContext.getString(prefKey))){
            token = mPrefs.getString(mContext.getString(prefKey), defaultValue);
        }

        return token;
    }

    private void setToken(@StringRes int prefKey, String token){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(mContext.getString(prefKey), token);
        editor.commit();
    }

    public String getFacebookToken() {
        return getToken(R.string.pref_facebook_token,"");
    }

    public String getGoogleToken() {
        return getToken(R.string.pref_google_token,"");
    }

    public void setFacebookToken(String token){
        setToken(R.string.pref_facebook_token, token);
    }

    public void setGoogleToken(String token){
        setToken(R.string.pref_google_token, token);
    }

}
