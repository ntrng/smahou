package com.moripro.morina.mylawdialog.preference;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class SettingsPref {

    public static String getTextPref(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String textSize = preferences.getString("text_size", null);
        return textSize;
    }

    public static void setTextPref(Context context, String textSize){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("text_size", textSize);
        editor.commit();
    }

    public static void setDefaultPref(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String textSize = preferences.getString("text_size", null);
        if(textSize == null){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("text_size", "55");
            editor.commit();
        }
    }
}
