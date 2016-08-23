package learn.example.pile.object;

import java.util.Locale;

import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/7/19.
 */
public class NetEase {
    //头条新闻,%d-%d 页数-个数
    public static final String TOUTAI_URL="http://c.m.163.com/nc/article/headline/T1348647909107/%d-%d.html";


    //文章,%s 新闻id
    public static final String ARTICLE_URL="http://c.m.163.com/nc/article/%s/full.html";
    public static final String ARTICLE_URL2="http://c.3g.163.com/nc/article/%s/full.html";


    //热门评论
    // %s 区域,%s 新闻id ,%d 哪里开始查询,%d 查询个数
    public static final String HOT_COMMENT_URL="http://comment.api.163.com/api/json/post/list/new/hot/%S/%S/%d/%d/10/2/2";


    /**
     *  普通评论
     *  差数与热门评论相同
     *  @see #HOT_COMMENT_URL
     */
    public static final String NORMAL_COMMENT_URL="http://comment.api.163.com/api/json/post/list/new/normal/%s/%s/desc/%d/%d/10/2/2 ";

    public static final String PHOTOS_SET_URL="http://c.m.163.com/photo/api/set/%S/%S.json";


    public static String generateHotCommentUrl(String replayBorad,String docID,int start,int len)
    {
        return String.format(Locale.CHINA,HOT_COMMENT_URL,replayBorad,docID,start,len);
    }

    public static String generateNormalCommentUrl(String replayBorad,String newsID,int start,int len)
    {
        return String.format(Locale.CHINA,NORMAL_COMMENT_URL,replayBorad,newsID,start,len);
    }

    public static String generateParamsUrl(int page,String sign)
    {   //EdhK6XoROwO4zVDtZT7s3CbpOEaotwy5nGQGUH2n6yB48ErR02zJ6/KXOnxX046I
        String queryUrl="http://c.3g.163.com/nc/article/headline/T1348647909107/"+page+"-20.html?";
        String normalParam= "from=toutiao&size=20&prog=LTitleA&fn=1&passport=&devId=kKD8pCd0XRRW7xWYqMiVpEdxCGl%2F7BNok2%2FsiNT73k5TdKiqocj0eExU0s49fzqe&lat=&lon=&version=20.0&net=wifi&canal=news_lljc1&mac=racUMC0A9havm%2BHe6jH3YAvVdjgSXYDtwEDZ03eH1l8%3D";
        String tsParam="&ts="+String.valueOf(TimeUtil.getTime()/1000);
        String signParam="&sign="+sign;
        return queryUrl+normalParam+tsParam+signParam;
    }


}
