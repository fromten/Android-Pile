package learn.example.pile.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.jsonbean.ZhihuStories;

/**
 * Created on 2016/7/9.
 */
public class Zhihu {
    public static final String STORY_URL="http://news-at.zhihu.com/api/4/news/latest";
    public static final String STORY_URL_AT_TIME="http://news.at.zhihu.com/api/4/news/before/";
    public static final String CONTENT_URL="http://news-at.zhihu.com/api/4/news/";

    public static List<Story> valueOf(ZhihuStories stories)
    {
        if (stories==null)
      {
          stories.getClass();
      }else {
            List<Story> list=new ArrayList<>();
            String date=stories.getDate();
            int size=stories.getStories().size();
            List<ZhihuStories.StoriesBean> bean=stories.getStories();
            if (bean!=null)
            {
                for (int i = 0; i < size; i++) {
                    String title=bean.get(i).getTitle();
                    int id=bean.get(i).getId();
                    String[] image=bean.get(i).getImages();
                    list.add(new Story(image,date,title,id));
                }
            }

            List<ZhihuStories.TopStoriesBean> topBean=stories.getTopStories();
            if (topBean!=null)
            {
                for (int i = 0; i < topBean.size(); i++) {
                    String title=topBean.get(i).getTitle();
                    int id=topBean.get(i).getId();
                    String[] image={ topBean.get(i).getImage()};
                    list.add(new Story(image,date,title,id));
                }
            }

            return list;
        }
        return null;
    }







    public static class Story implements Parcelable {
        private String[] imageUrls;
        private String date;
        private String title;
        private int id;

        public Story(String[] imageUrls, String date, String title, int id) {
            this.imageUrls = imageUrls;
            this.date = date;
            this.title = title;
            this.id = id;
        }

        public String[] getImageUrls() {
            return imageUrls;
        }

        public String getDate() {
            return date;
        }

        public String getTitle() {
            return title;
        }

        public int getId() {
            return id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringArray(this.imageUrls);
            dest.writeString(this.date);
            dest.writeString(this.title);
            dest.writeInt(this.id);
        }

        protected Story(Parcel in) {
            this.imageUrls = in.createStringArray();
            this.date = in.readString();
            this.title = in.readString();
            this.id = in.readInt();
        }

        public static final Parcelable.Creator<Story> CREATOR = new Parcelable.Creator<Story>() {
            @Override
            public Story createFromParcel(Parcel source) {
                return new Story(source);
            }

            @Override
            public Story[] newArray(int size) {
                return new Story[size];
            }
        };
    }
}
