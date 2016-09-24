package learn.example.pile.jsonbean;


import com.google.gson.JsonArray;

/**
 * Created on 2016/7/22.
 */
public class OpenEyeVideo {

    private String nextPageUrl;
    private long nextPublishTime;
    private String newestIssueType;
    private JsonArray issueList;
    private JsonArray itemList;

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public long getNextPublishTime() {
        return nextPublishTime;
    }

    public String getNewestIssueType() {
        return newestIssueType;
    }

    public JsonArray getIssueList() {
        return issueList;
    }

    public JsonArray getItemList() {
        return itemList;
    }
}
