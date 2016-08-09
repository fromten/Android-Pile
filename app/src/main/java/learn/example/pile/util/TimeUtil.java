package learn.example.pile.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created on 2016/7/13.
 */
public class TimeUtil {


    /**
     * 转换成 年-月-日
     * @param timestamp 时间戳 秒格式
     * @return string
     */
    public static String formatYMD(long timestamp)
    {
        DateFormat format=SimpleDateFormat.getDateInstance(SimpleDateFormat.DEFAULT, Locale.CHINA);
        Date date = new Date(timestamp*1000);
        return format.format(date);
    }

    public static String formatTimeFull(long timestamp)
    {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss",Locale.CHINA);
        Date date = new Date(timestamp*1000);
        return dateFormat.format(date);
    }

    /**
     * 转换成 分钟-秒,如 204 = 3'4
     * @param second 秒数
     * @return string
     */
    public static String formatMS(int second)
    {
        int minute=second/60;
        int s=second-minute*60;
        return minute+"'"+s;
    }


    public static long getTime()
    {
        return new Date().getTime();
    }

    public static long getNextDayTime()
    {
        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        calendar.roll(Calendar.DAY_OF_YEAR, +1);
        return calendar.getTime().getTime();
    }

    public static long getNextDayZeroClock()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.roll(Calendar.DAY_OF_YEAR,1);
        return cal.getTime().getTime();
    }
}
