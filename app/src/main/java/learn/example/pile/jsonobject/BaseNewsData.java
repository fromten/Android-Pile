package learn.example.pile.jsonobject;

/**
 * Created on 2016/5/24.
 */
public class BaseNewsData {
    private String newsTitle;
    private String newsDesc;
    private String newsIMgUrl;
    private String newsUrl;

    public BaseNewsData() {
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDesc() {
        return newsDesc;
    }

    public void setNewsDesc(String newsDesc) {
        this.newsDesc = newsDesc;
    }

    public String getNewsIMgUrl() {
        return newsIMgUrl;
    }

    public void setNewsIMgUrl(String newsIMgUrl) {
        this.newsIMgUrl = newsIMgUrl;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}
