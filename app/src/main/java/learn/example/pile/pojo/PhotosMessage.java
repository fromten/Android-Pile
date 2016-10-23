package learn.example.pile.pojo;

/**
 * Created on 2016/8/15.
 */
public class PhotosMessage{
    private String title;
    private String content;
    private String mUrl;

    public PhotosMessage(String title, String content,String url) {
        this.title = title;
        this.content = content;
        this.mUrl=url;
    }

    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }

    public String getUrl() {
        return mUrl;
    }
}
