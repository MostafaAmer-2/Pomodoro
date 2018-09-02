package com.dreidev.mostafa.pomodoro.Settings;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    static String MyPREFERENCES = "Pomodoro";

    public static String loadTrelloToken(Context ctx) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("token", "");
    }

    public static void saveTrelloToken(Context ctx, String token) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("token", token);
        editor.putString("isTokenValid", "true");
        editor.commit();
    }

    public static void saveDataFlag(Context ctx, String bool) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("isTokenValid", bool);
        editor.commit();
    }

    public static String loadDataFlag(Context ctx) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("isTokenValid", "false");
    }


    public static void saveUserID(Context ctx, String id) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("userID", id);
        editor.commit();
    }

    public static String loadUserID(Context ctx) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("userID", "");
    }


    public static boolean isTokenPresent(Context ctx){
        return !loadTrelloToken(ctx).equals("");
    }
}
