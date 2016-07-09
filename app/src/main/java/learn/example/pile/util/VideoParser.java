package learn.example.pile.util;


import android.text.Html;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.ParserAdapter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created on 2016/5/30.
 */
public class VideoParser {


    //从网络去解析微博的视频地址
    public static String getWeiboVideoFileUrl(String html)
    {
        return getAttrValue(html,"video","src")[0];
    }

    //从网络去解析微博的视频地址
    public static String getWeiboVideoImgUrl(String html)
    {
        return getAttrValue(html,"img","src")[1];
    }

    //从网络去解析秒怕的视频地址
    public static String getMPVidFileUrlFromNet(String html)
    {
        return getAttrValue(html,"video","src")[0];
    }

    //从网络去解析秒怕的视频地址
    public static String getMPVidImgUrlFromNet(String html)
    {
        return getAttrValue(html,"img","data-url")[0];
    }

    //从视频的Url地址去分析秒怕的视频地址
    public static String getMPVidImgFromUrl(String url)
    {
        try {
            int start=url.lastIndexOf("/");
            int end=url.indexOf(".",start);
            String id=url.substring(start+1,end);
            return "http://wscdn.miaopai.com/stream/"+id+"_tmp_12.jpg";
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }
    //从视频的Url地址去分析秒怕的图片地址
    public static String getMPVidFileFromUrl(String url)
    {  try {
        int start=url.lastIndexOf("/");
        int end=url.indexOf(".",start);
        String id=url.substring(start+1,end);
        return "http://gslb.miaopai.com/stream/"+id+".mp4";
      }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getAttrValue(String html,String tag,String attr)
    {
        Document doc=Jsoup.parse(html);
        Elements elements=doc.select(tag);
        int size=elements.size();
        String array[]=new String[size];
        int i=0;
        for (Element element:elements)
        {
            array[i]=element.attr(attr);
            i++;
        }
        return array;
    }

}
