package learn.example.joke.util;

/**
 * Created on 2016/5/7.
 */
public class UrlCheck {
    public static boolean isGifImg(String url)
    {
        int last=url.lastIndexOf(".");
        String str=url.substring(last+1,url.length());
        System.out.println(str);
        return str.equals("gif");
    }

    //把Url地址和请求参数分离，array【0】地址,array【1】请求参数
    public static String[] cropUrl(String url)
    {
        int last=url.lastIndexOf("?");
        String[] strarry=new String[2];
        strarry[0]=url.substring(0,last);
        strarry[1]=url.substring(last+1,url.length());
        return strarry;
    }
}
