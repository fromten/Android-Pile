package learn.example.pile.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created on 2016/7/13.
 */
public class TimeUtil {


    /**
     * 转换成 年-月-日
     * @param timestamp 时间戳
     * @return string
     */
    public static String formatYMD(long timestamp)
    {
        DateFormat format=SimpleDateFormat.getDateInstance(SimpleDateFormat.DEFAULT, Locale.CHINA);
        Date date = new Date(timestamp*1000);
        return format.format(date);
    }

    /**
     * 转换成 分钟-秒,如 204 = 3'4
     * @param second 秒数
     * @return string
     */
    public static String formatMS(int second)
    {
        int minute=second/60;
        int s=second%60;
        return minute+"'"+s;
    }


    public static long getTime()
    {
        return new Date().getTime();
    }
}
