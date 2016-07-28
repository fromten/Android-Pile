package learn.example.pile.jsonbean;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2016/7/9.
 */
public class ZhihuNewsContent {

    private String body;
    @SerializedName("image_source")
    private String imageSource;
    private String title;
    private String image;
    @SerializedName("share_url")
    private String shareUrl;
    private String ga_prefix;
    private int type;
    private int id;
    private String[] js;
    private String[] images;
    private String[] css;

    public String getBody() {
        return body;
    }

    public String getImageSource() {
        return imageSource;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String[] getJs() {
        return js;
    }

    public String[] getImages() {
        return images;
    }

    public String[] getCss() {
        return css;
    }
}
