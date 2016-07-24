package learn.example.pile.jsonbean;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Created on 2016/7/20.
 */
public class NetEaseComment  {


    private String againstLock;
    private String audioLock;
    private String code;
    private String docUrl;
    private String isTagOff;
    private JsonArray hotPosts;
    private JsonArray newPosts;


    public JsonArray getNewPosts() {
        return newPosts;
    }

    public String getAgainstLock() {
        return againstLock;
    }

    public void setAgainstLock(String againstLock) {
        this.againstLock = againstLock;
    }

    public String getAudioLock() {
        return audioLock;
    }

    public void setAudioLock(String audioLock) {
        this.audioLock = audioLock;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getIsTagOff() {
        return isTagOff;
    }

    public void setIsTagOff(String isTagOff) {
        this.isTagOff = isTagOff;
    }

    public JsonArray getHotPosts() {
        return hotPosts;
    }
}
