package io.nearby.android.data.source.local;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.nearby.android.R;
import io.nearby.android.data.source.Local;


/**
 * Created by Marc on 2017-02-08.
 */
@Singleton
@Local
public class SharedPreferencesHelper {

    public static final int LAST_SIGN_IN_METHOD_NONE = 0;
    public static final int LAST_SIGN_IN_METHOD_GOOGLE = 1;
    public static final int LAST_SIGN_IN_METHOD_FACEBOOK = 2;

    private SharedPreferences mPrefs;
    private Context mContext;

    @Inject
    public SharedPreferencesHelper(Context context){
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean hasUserAlreadySignedIn(){
        boolean hasAlready = mPrefs.contains(mContext.getString(R.string.pref_last_social_login_used));

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

    private String getUserId(@StringRes int prefKey){
        String userId = "";

        if(mPrefs.contains(mContext.getString(prefKey))){
            userId = mPrefs.getString(mContext.getString(prefKey), "");
        }

        return userId;
    }

    private void setUserId(@StringRes int prefKey, String userId){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(mContext.getString(prefKey), userId);
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

    public String getFacebookUserId(){
        return getUserId(R.string.pref_facebook_user_id);
    }

    public String getGoogleUserId(){
        return getUserId(R.string.pref_google_user_id);
    }

    public void setFacebookUserId(String userId) {
        setUserId(R.string.pref_facebook_user_id,userId);
    }

    public void setGoogleUserId(String userId) {
        setUserId(R.string.pref_google_user_id,userId);
    }

    /**
     * Gets the default anonymity or the the last anonymity setting used.
     * The default returned value is true.
     * @return true if the user is anonymous.
     */
    public boolean getDefaultAnonymity() {
        boolean anonymity = true;

        if(mPrefs.contains(mContext.getString(R.string.pref_anonymity))){
            anonymity = mPrefs.getBoolean(mContext.getString(R.string.pref_anonymity),true);
        }

        return anonymity;
    }

    public void setDefaultAnonymity(boolean anonymity){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(mContext.getString(R.string.pref_anonymity), anonymity);
        editor.commit();
    }
}
