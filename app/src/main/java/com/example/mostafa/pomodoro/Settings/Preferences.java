package com.example.mostafa.pomodoro.Settings;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    static String MyPREFERENCES = "Pomodoro";

    public static String loadData(Context ctx) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("token", "");
    }

    public static void saveData(Context ctx, String token) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("token", token);
        editor.putString("checkTokenValidityAndUpdatePrefrences", "true");
        editor.commit();
    }

    public static void saveDataFlag(Context ctx, boolean bool) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("checkTokenValidityAndUpdatePrefrences", bool);
        editor.commit();
    }

    public static boolean loadDataFlag(Context ctx) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean("checkTokenValidityAndUpdatePrefrences", false);
    }

    public static boolean isTokenPresent(Context ctx){
        return !loadData(ctx).equals("");
    }
}
