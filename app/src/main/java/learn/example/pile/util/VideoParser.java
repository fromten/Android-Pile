package learn.example.pile.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created on 2016/5/30.
 */
public class VideoParser {


    //从网络去解析微博的视频地址
    public static String getWeiboVideoFileUrl(String html)
    {
        try {
        Document doc=Jsoup.parse(html);
        Element video=doc.select("video").first();
        return video.attr("src");
     } catch (NullPointerException|IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //从网络去解析微博的视频地址
    public static String getWeiboVideoImgUrl(String html)
    {
        try {
            Document doc=Jsoup.parse(html);
            Element img=doc.select("img.poster").first();
            return img.attr("src");
        }catch (NullPointerException|IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //从网络去解析秒怕的视频地址
    public static String getMPVidFileUrlFromNet(String html)
    {
        try {
            Document doc=Jsoup.parse(html);
            Element video=doc.select("video").first();
            return video.attr("src");
        } catch (NullPointerException|IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //从网络去解析秒怕的视频地址
    public static String getMPVidImgUrlFromNet(String html)
    {
        try {
            if(html.contains("http://wscdn.miaopai.com/static20131031/miaopai20140729/img/del.png"))
                return null;
            Document doc=Jsoup.parse(html);
            Element img=doc.select("div.video_img").first();
            return img.attr("data-url");
        } catch (NullPointerException|IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //从视频的Url地址去解析秒怕的视频地址
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
    //从视频的Url地址去解析秒怕的图片地址
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
}
