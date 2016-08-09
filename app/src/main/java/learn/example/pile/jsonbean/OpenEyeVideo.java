package learn.example.pile.jsonbean;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.JsonArray;

import java.util.List;

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
