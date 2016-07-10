package learn.example.pile.jsonbean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2016/7/9.
 */
public class ZhihuStories {


    private String date;
    private List<StoriesBean> stories;
    @SerializedName("top_stories")
    private List<TopStoriesBean> topStories;


    public String getDate() {
        return date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public List<TopStoriesBean> getTopStories() {
        return topStories;
    }

    public static class StoriesBean {
        private int type;
        private int id;
        @SerializedName("ga_prefix")
        private String gaPrefix;
        private String title;
        private String[] images;

        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public String getGaPrefix() {
            return gaPrefix;
        }

        public String getTitle() {
            return title;
        }

        public String[] getImages() {
            return images;
        }
    }

    public static class TopStoriesBean {
        private String image;
        private int type;
        private int id;

        @SerializedName("ga_prefix")
        private String gaPrefix;

        private String title;

        public String getImage() {
            return image;
        }

        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public String getGaPrefix() {
            return gaPrefix;
        }

        public String getTitle() {
            return title;
        }
    }


}
