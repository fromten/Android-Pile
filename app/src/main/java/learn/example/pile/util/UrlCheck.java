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

    //解析微博,[0] 视频源地址,[1]图片url
    public static String[] parserWeiboHtml(Document doc)
    {
        Element video=doc.select("video").first();
        Element img=doc.select("img.poster").first();
        String[] arr=new String[2];
        if(video!=null)
        {
            arr[0]=video.attr("src");
        }
        if(img!=null)
        {
            arr[1]=img.attr("src");
        }
        return arr;
    }
    //解析秒拍,[0] 视频源地址,[1]图片url
    public static String[] parserMiaopaiHtml(Document doc)
    {
        Element video=doc.select("video").first();
        Element img=doc.select("div.video_img").first();
        String[] arr=new String[2];
        if(video!=null)
        {
            arr[0]=video.attr("src");
        }
        if(img!=null)
        {
            arr[1]=img.attr("data-url");
        }
        return arr;
    }
}
