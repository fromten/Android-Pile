package learn.example.pile.util;

/**
 * Created on 2016/5/7.
 */
public class UrlCheck {

    public static boolean isGifImg(String url)
    {

        return url!=null&&url.contains(".gif");
    }

    //坑爹的数据源
    public static String fixUrl(String url)
    {
        String result=url.replace("\"></p>","");
        return result;
    }
}
