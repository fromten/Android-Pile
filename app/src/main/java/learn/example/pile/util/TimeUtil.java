package learn.example.pile.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created on 2016/7/13.
 */
public class TimeUtil {

    public static final String FORMAT_YMD_HMS="yyyy-MM-dd  HH:mm:ss";
    public static final String FORMAT_YMD_HM="yyyy-MM-dd HH:mm";
    public static final String FORMAT_YMD="yyyy-MM-dd";



    /**
     * 转换成 年-月-日 小时-分钟-秒
     * @param timestamp 时间戳
     * @return 字符串
     */
    public static String formatTime(String format,long timestamp)
    {
        SimpleDateFormat dateFormat=new SimpleDateFormat(format,Locale.CHINA);
        Date date = new Date(timestamp*1000);
        return dateFormat.format(date);
    }

    /**
     * 转换成 分钟-秒,如 204 = 3'4
     * @param second 秒数 ,应该传递小于一小时的秒数
     * @return string
     */
    public static String formatMS(int second)
    {
        int minute=second/60;
        int s=second%60;
        return minute+"'"+s;
    }

    //return ms
    public static long getCurrentTime()
    {
        return new Date().getTime();
    }


    /**
     * 获得下一天指定的时间
     * @param hour 指定小时
     * @param minute 指定分钟
     * @param second 指定秒
     * @return 时间戳
     */
    public static long getNextDayTime(int hour,int minute,int second)
    {
        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        calendar.roll(Calendar.DAY_OF_YEAR, +1);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

}
