package learn.example.pile.jsonobject;

import java.util.List;

/**
 * Created on 2016/5/24.
 */
public class VideoJsonData {

    /**
     * error : false
     * results : [{"_id":"5741a6526776597d979ad009","createdAt":"2016-05-22T20:30:10.811Z","desc":"日本团队用1000多杯拿铁做成了一部令人心动的动画，创意满分，甜蜜高糖！","publishedAt":"2016-05-24T11:56:12.924Z","source":"chrome","type":"休息视频","url":"http://weibo.com/p/2304449a1fb0b126ab01616812e39991c51aa8","used":true,"who":"lxxself"},{"_id":"5741a6346776597d9df10b51","createdAt":"2016-05-22T20:29:40.180Z","desc":"8分钟混剪，看尽电影世界里的哲学","publishedAt":"2016-05-23T10:54:25.890Z","source":"chrome","type":"休息视频","url":"http://www.miaopai.com/show/GzWUGh4N4rGxv3VDyL5KGw__.htm","used":true,"who":"lxxself"},{"_id":"573d3b9c6776591ca2f31b99","createdAt":"2016-05-19T12:05:48.633Z","desc":"Google IO 2016 KeyNote","publishedAt":"2016-05-19T12:09:32.865Z","source":"chrome","type":"休息视频","url":"https://www.youtube.com/watch?v=862r3XS2YB0","used":true,"who":"代码家"},{"_id":"573c877c6776591ca681f8c0","createdAt":"2016-05-18T23:17:16.446Z","desc":"美美美：新加坡JKAI携手美女《太阳的后裔》中文版Always","publishedAt":"2016-05-20T10:05:09.959Z","source":"chrome","type":"休息视频","url":"http://v.youku.com/v_show/id_XMTU3MjUwMDkxMg==.html","used":true,"who":"lxxself"},{"_id":"57374a0c6776591ca2f31b3e","createdAt":"2016-05-14T23:53:48.527Z","desc":"施瓦辛格的励志演讲","publishedAt":"2016-05-17T12:17:17.785Z","source":"chrome","type":"休息视频","url":"http://www.miaopai.com/show/4kU9dsswDerxmthxDsSPWg__.htm","used":true,"who":"LHF"}]
     */

    private boolean error=true;
    /**
     * _id : 5741a6526776597d979ad009
     * createdAt : 2016-05-22T20:30:10.811Z
     * desc : 日本团队用1000多杯拿铁做成了一部令人心动的动画，创意满分，甜蜜高糖！
     * publishedAt : 2016-05-24T11:56:12.924Z
     * source : chrome
     * type : 休息视频
     * url : http://weibo.com/p/2304449a1fb0b126ab01616812e39991c51aa8
     * used : true
     * who : lxxself
     */

    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }
    }
}
