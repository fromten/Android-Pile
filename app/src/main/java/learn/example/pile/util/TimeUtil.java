package learn.example.pile.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created on 2016/7/13.
 */
public class TimeUtil {

    public static String formatYMD(long timestamp)
    {
        DateFormat format=SimpleDateFormat.getDateInstance(SimpleDateFormat.DEFAULT, Locale.CHINA);
        Date date = new Date(timestamp*1000);
        return format.format(date);
    }

    public static long getTimeStamp()
    {
        return new Date().getTime();
    }
}
