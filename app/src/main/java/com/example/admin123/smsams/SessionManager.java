package com.example.admin123.smsams;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    public SessionManager(Context context) { //Constructor
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.sharedPreferences_editor = this.sharedPreferences.edit();
    }

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferences_editor;

    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "users";
    private static final String IS_LOGIN = "isLoggedIn";
    public static final String KEY_USERID = "123";
    public static final String KEY_USERNAME = "name";

    public void createLoginSession(String name, String userid) { //Create a login session
        sharedPreferences_editor.putBoolean(IS_LOGIN, true);
        sharedPreferences_editor.putString(KEY_USERNAME, name);
        sharedPreferences_editor.putString(KEY_USERID, userid);
        sharedPreferences_editor.commit();
    }

    public void checkLogin() {
        if (!isLoggedin()) {
            Intent intent = new Intent(this.context, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            this.context.startActivity(intent);
        }
    }

    public boolean isLoggedin() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USERNAME, sharedPreferences.getString(KEY_USERNAME, null));
        user.put(KEY_USERID, sharedPreferences.getString(KEY_USERID, null));
        return user;
    }

    public void logoutUser() {
        sharedPreferences_editor.clear();
        sharedPreferences_editor.commit();
        Intent intent = new Intent(this.context, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.context.startActivity(intent);
    }
}
