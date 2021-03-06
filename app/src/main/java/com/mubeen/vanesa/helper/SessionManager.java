package com.mubeen.vanesa.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by mubeen on 16/03/2017.
 */

public class SessionManager {

    private static String TAG = SessionManager.class.getSimpleName();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "USERLOGIN";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_PRODUCTS_COLUMN_COUNT = "columncount";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedin)
    {
        editor.putBoolean(KEY_IS_LOGGEDIN,isLoggedin);
        editor.commit();

        Log.d(TAG, "User with id login session modified");
    }


    public boolean isLoggedin(){
        return pref.getBoolean(KEY_IS_LOGGEDIN,false);
    }

    public void setColumnCount(int count) {
        editor.putInt(KEY_PRODUCTS_COLUMN_COUNT,count);
        editor.commit();
    }

    public int getColumnCount(){
        return pref.getInt(KEY_PRODUCTS_COLUMN_COUNT,1);
    }

}
