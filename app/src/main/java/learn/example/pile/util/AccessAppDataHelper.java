package learn.example.pile.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created on 2016/6/25.
 */
public class AccessAppDataHelper {


    public static String KEY_JOKE_PAGE="KEY_JOKE_PAGE";
    public static String KEY_VIDEO_PAGE="KEY_VIDEO_PAGE";
    public static String KEY_READ_PAGE="KEY_READ_PAGE";


    public static int readInteger(Activity activity,String key,int defValue)
    {
       SharedPreferences preferences=activity.getPreferences(Context.MODE_PRIVATE);
       return preferences.getInt(key,defValue);
    }

    public static void saveInteger(Activity activity,String key,int data)
    {
        SharedPreferences preferences=activity.getPreferences(Context.MODE_PRIVATE);
        preferences.edit().putInt(key,data).apply();
    }
}
