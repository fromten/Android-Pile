package learn.example.pile.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.jsonbean.ZhihuStories;

/**
 * Created on 2016/7/9.
 */
public class Zhihu {
    public static final String STORY_URL="http://news-at.zhihu.com/api/4/news/latest";
    public static final String STORY_URL_AT_TIME="http://news.at.zhihu.com/api/4/news/before/";
    public static final String CONTENT_URL="http://news-at.zhihu.com/api/4/news/";
    public static final String COMMENT_LONG="http://news-at.zhihu.com/api/4/story/?/long-comments";
    public static final String COMMENT_SHORT="http://news-at.zhihu.com/api/4/story/?/short-comments";

}
