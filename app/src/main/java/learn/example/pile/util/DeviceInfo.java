package learn.example.pile.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created on 2016/6/4.
 */
public class DeviceInfo {

    public final int SCREEN_WIDTH;
    public final int SCREEN_HEIGHT;

    public DeviceInfo(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(displaymetrics);
        SCREEN_HEIGHT = displaymetrics.heightPixels;
        SCREEN_WIDTH = displaymetrics.widthPixels;
    }

    /**
     * 外部存储是否可用
     * @return true 如果可以,否则 false
     */
    public static boolean checkExternalStorageState() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 检查网络是否连接
     * @param context
     * @return 如果连接过或连接中返回true,否则false
     */
    public static boolean checkNetConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }


    /**
     * 检查连接类型是不是wifi
     * @param context
     * @return 网络连接类型是Wifi返回true,否则false
     */
    public static boolean ifWifiConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return info != null && info.isConnected();
    }


}
