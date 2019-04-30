package com.example.sayan.locationtracking;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref
{
    public static String FILE_NAME = "MyPref";


    public static String readSharedSetting(Context ctx, String settingName, String defaultValue)
    {
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getString(settingName,defaultValue);
    }



    public static void saveSettings(Context ctx,String settingName,String defaultValue)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(settingName,defaultValue);
        editor.apply();
    }


    public static void SharedPrefSave(Context ctx,String email,String password)
    {
        SharedPreferences prefs = ctx.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Email",email);
        editor.putString("Password",password);
        editor.putBoolean("UserExists",true);
        editor.commit();
    }


    public static void removeData(Context ctx)
    {
        SharedPreferences prefs = ctx.getSharedPreferences("NAME",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("Email");
        editor.remove("Password");
        editor.commit();
    }

}
