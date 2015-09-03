package com.pundroid.bestmoviesapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pumba30 on 30.08.2015.
 */
public class PrefUtils {
    private SharedPreferences sharedPref;
    private static PrefUtils instance;

    public static final String KEY_SESSION_USER_USERNAME = "sessionUserUsername";
    public static final String KEY_SESSION_USER_ID = "sessionUserID";
    public static final String KEY_SESSION_ID = "sessionID";
    public static final String KEY_USER_IS_IN_ACCOUNT = "userIsInAccount";
    //***************************************************************

    //data pref
    public static final String KEY_SHARED_PREF = "ANDROID_MOVIE_LIST";
    private static final int KEY_MODE_PRIVATE = 0;

    private PrefUtils(Context context) {
        sharedPref = context.getSharedPreferences(KEY_SHARED_PREF,
                KEY_MODE_PRIVATE);
    }

    public static PrefUtils getInstance(Context context) {
        if (instance == null) {
            instance = new PrefUtils(context.getApplicationContext());
        }
        return instance;
    }

    public void storeSessionUser(int userID, String userName, String session_ID) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_SESSION_USER_ID, userID);
        editor.putString(KEY_SESSION_USER_USERNAME, userName);
        editor.putString(KEY_SESSION_ID, session_ID);
        editor.putBoolean(KEY_USER_IS_IN_ACCOUNT, true);
        editor.apply();
    }


}
