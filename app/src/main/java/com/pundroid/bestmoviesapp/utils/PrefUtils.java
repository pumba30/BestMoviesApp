package com.pundroid.bestmoviesapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pumba30 on 30.08.2015.
 */
public class PrefUtils {
    private SharedPreferences sharedPref;
    private static PrefUtils instanse;

    private static final String KEY_SESSION_USER_USERNAME = "sessionUserUsername";
    private static final String KEY_SESSION_USER_ID = "sessionUserID";
    private static final String KEY_SESSION_ID = "sessionID";
    private static final String KEY_USER_LOGIN_BOOLEAN = "userBooleanLoginLabel";
    //data pref
    private static final String KEY_SHARED_PREF = "ANDROID_MOVIE_LIST";
    private static final int KEY_MODE_PRIVATE = 0;

    //guest data
    private static final String KEY_GUEST_SESSION_ID = "guestSessionID";
    private static final String KEY_GUEST_BOOLEAN = "guestBoolean";

    private PrefUtils(Context context) {
        sharedPref = context.getSharedPreferences(KEY_SHARED_PREF,
                KEY_MODE_PRIVATE);
    }

    public static PrefUtils getInstance(Context applicationContext) {
        if (instanse == null) {
            instanse = new PrefUtils(applicationContext.getApplicationContext());
        }
        return instanse;
    }

    public void storeSessionUser(int userID, String userName, String session_ID) {
        //setGuest(false);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_SESSION_USER_ID, userID);
        editor.putString(KEY_SESSION_USER_USERNAME, userName);
        editor.putString(KEY_SESSION_ID, session_ID);
        editor.putBoolean(KEY_GUEST_BOOLEAN, false);
        editor.apply();
    }

    public boolean isUserLogin() {
        return sharedPref.getBoolean(KEY_USER_LOGIN_BOOLEAN, false);
    }

    public String getSessionID() {
        return sharedPref.getString(KEY_SESSION_ID, null);
    }

    public void setGuest(boolean guest) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_GUEST_BOOLEAN, guest);
        editor.apply();
    }

    public String getGuestSessionID() {
        return sharedPref.getString(KEY_GUEST_SESSION_ID, null);
    }


    public void storeGuestSessionUser(String session_ID) {
        setGuest(true);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_GUEST_BOOLEAN, true);
        editor.putString(KEY_GUEST_SESSION_ID, session_ID);
        editor.apply();
    }


}