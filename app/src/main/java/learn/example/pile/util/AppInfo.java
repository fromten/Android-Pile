package learn.example.pile.util;

import android.os.Environment;

/**
 * Created on 2016/6/4.
 */
public class AppInfo {


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
