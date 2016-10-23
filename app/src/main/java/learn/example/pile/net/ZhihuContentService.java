package learn.example.pile.net;


import learn.example.pile.jsonbean.ZhihuNewsContent;
import learn.example.pile.provider.Zhihu;

/**
 * Created on 2016/7/10.
 */
public class ZhihuContentService extends NetService {

    public String TAG_CONTENT = "tag_content";
    public String TAG_COMMENT_LONG = "tag_comment_long";
    public String TAG_COMMENT_SHORT = "tag_comment_short";

    public void getContent(int id, Callback<ZhihuNewsContent> callback) {
        String url = Zhihu.CONTENT_URL + id;
        newRequest(TAG_CONTENT, ZhihuNewsContent.class, url, callback);
    }

    public void getLongComment(int id, Callback<String> callback) {
        String url = Zhihu.COMMENT_LONG.replace("?", String.valueOf(id));
        newStringRequest(TAG_COMMENT_LONG, url, callback);
    }

    public void getShortComment(int id, Callback<String> callback) {
        String url = Zhihu.COMMENT_SHORT.replace("?", String.valueOf(id));
        newStringRequest(TAG_COMMENT_SHORT, url, callback);
    }


}
