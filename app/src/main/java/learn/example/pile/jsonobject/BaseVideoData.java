package learn.example.pile.jsonobject;

/**
 * Created on 2016/5/25.
 */
public class BaseVideoData {
    private String videoUrl;
    private String desc;
    private String time;
    private String imgUrl;
    private String htmlUrl;
    public BaseVideoData(String url, String desc, String time) {
        this.htmlUrl = url;
        this.desc = desc;
        this.time = time;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }
}
