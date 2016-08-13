package learn.example.pile.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;

/**
 * Created on 2016/6/4.
 */
public class DeviceInfo {

     public   int SCREEN_WIDTH=-1;
     public   int SCREEN_HEIGHT=-1;

    public DeviceInfo(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        SCREEN_HEIGHT = displaymetrics.heightPixels;
        SCREEN_WIDTH = displaymetrics.widthPixels;
    }

    /**
     *
     * @return 外部存储是否可用
     */
    public static boolean checkExternalStorageState()
    {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


}
