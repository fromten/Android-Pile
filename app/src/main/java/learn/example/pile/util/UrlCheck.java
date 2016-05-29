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
        boolean bool=false;
        try {

            int last=url.lastIndexOf(".");
            char c1=url.charAt(last+1);
            char c2=url.charAt(last+2);
            char c3=url.charAt(last+3);
            bool=c1=='g'&&c2=='i'&&c3=='f';
        }catch (StringIndexOutOfBoundsException e)
        {
           e.printStackTrace();
        }
        return bool;
    }
    //坑爹的数据源
    public static String fixUrl(String url)
    {
        String result=null;
        try {
            int first=url.indexOf("h");
            int last=url.lastIndexOf(".");
            result=url.substring(first,last+4);
        }catch (StringIndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }
        return result;
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
    public static String[] parserWandoujiaHtml(Document doc)
    {
        Element video=doc.select("video").first();
        Element img=doc.select("#").first();
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
