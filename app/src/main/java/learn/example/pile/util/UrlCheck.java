package learn.example.pile.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created on 2016/5/7.
 */
public class UrlCheck {

    public static boolean isGifImg(String url)
    {

        return url.endsWith(".gif");
    }
    //坑爹的数据源
    public static String fixUrl(String url)
    {
        String result=url.replace("\"></p>","");
        return result;
    }
}
