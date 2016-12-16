package learn.example.pile.pojo;

import java.util.List;

/**
 * Created on 2016/11/4.
 */
public class Joke {
    List<Joke.Item> jokeItems;

    public Joke(List<Joke.Item> jokeItems) {
        this.jokeItems = jokeItems;
    }

    public Joke() {
    }

    public List<Joke.Item> getJokeItems() {
        return jokeItems;
    }

    public void setJokeItems(List<Joke.Item> jokeItems) {
        this.jokeItems = jokeItems;
    }

    public static class Item {
        private boolean isGif;
        private boolean isVideo;
        private boolean isMultiImages;
        private String text;
        private String title;
        private long pushTime;
        private int commentCount;
        private int likeCount;
        private int unLikeCount;
        private String id_str;
        private String userName;
        private String avatar;
        private Image[] imageUrls;
        private Image image;
        private Video mVideo;

        public boolean isGif() {
            return isGif;
        }

        public void setGif(boolean gif) {
            isGif = gif;
        }

        public boolean isVideo() {
            return isVideo;
        }

        public void setVideo(boolean video) {
            isVideo = video;
        }

        public boolean isMultiImages() {
            return isMultiImages;
        }

        public void setMultiImages(boolean multiImages) {
            isMultiImages = multiImages;
        }


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getPushTime() {
            return pushTime;
        }

        public void setPushTime(long pushTime) {
            this.pushTime = pushTime;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public int getUnLikeCount() {
            return unLikeCount;
        }

        public void setUnLikeCount(int unLikeCount) {
            this.unLikeCount = unLikeCount;
        }

        public String getId_str() {
            return id_str;
        }

        public void setId_str(String id_str) {
            this.id_str = id_str;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Image[] getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(Image[] imageUrls) {
            this.imageUrls = imageUrls;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public Video getVideo() {
            return mVideo;
        }

        public void setVideo(Video video) {
            mVideo = video;
        }
    }
    public static class Image {
        String url;
        boolean isGif;

        public Image(String url, boolean isGif) {
            this.url = url;
            this.isGif = isGif;
        }

        public String getUrl() {
            return url;
        }

        public boolean isGif() {
            return isGif;
        }
    }

    public static class Video {
        private String url;

        public Video(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
