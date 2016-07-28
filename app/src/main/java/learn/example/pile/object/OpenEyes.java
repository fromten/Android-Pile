package learn.example.pile.object;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.jsonbean.OpenEyeVideo;

/**
 * Created on 2016/7/22.
 */
public class OpenEyes {


    public static String HOT_URL="http://baobab.wandoujia.com/api/v2/feed?udid=5cf7bfcf19a84c618fd8d1e41f55518025ce2f94&vc=89&vn=1.13.1&deviceModel=Samsung%20Galaxy%20S6%20-%206.0.0%20-%20API%2023%20-%201440x2560&first_channel=eyepetizer_web&last_channel=eyepetizer_web";
    public static String APP_PARAMS="&udid=5cf7bfcf19a84c618fd8d1e41f55518025ce2f94&vc=89&vn=1.13.1&deviceModel=Samsung%20Galaxy%20S6%20-%206.0.0%20-%20API%2023%20-%201440x2560&first_channel=eyepetizer_web&last_channel=eyepetizer_web";

    public static String getHotUrl(int num)
    {
        return HOT_URL+"&num="+num;
    }

    public static String getNextHotUrl(String nextUrl)
    {
        return nextUrl+APP_PARAMS;
    }


    public static class VideoInfo implements Parcelable {
        private String title;
        private String playUrl;
        private String imgUrl;
        private int  duration;

        public VideoInfo(String title, String playUrl, String imgUrl,int duration) {
            this.title = title;
            this.playUrl = playUrl;
            this.imgUrl = imgUrl;
            this.duration=duration;

        }

        public int getDuration() {
            return duration;
        }

        public String getTitle() {
            return title;
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.playUrl);
            dest.writeString(this.imgUrl);
            dest.writeInt(this.duration);
        }

        protected VideoInfo(Parcel in) {
            this.title = in.readString();
            this.playUrl = in.readString();
            this.imgUrl = in.readString();
            this.duration = in.readInt();
        }

        public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
            @Override
            public VideoInfo createFromParcel(Parcel source) {
                return new VideoInfo(source);
            }

            @Override
            public VideoInfo[] newArray(int size) {
                return new VideoInfo[size];
            }
        };
    }

    public static List<VideoInfo> buildVideoInfo(OpenEyeVideo object)
    {

        List<VideoInfo> list=new ArrayList<>();

        String title=null;
        String imgUrl=null;
        String playUrl=null;
        int   duration=0;

          for (OpenEyeVideo.IssueListBean bean:object.getIssueList())
          {
              for (OpenEyeVideo.IssueListBean.ItemListBean item:bean.getItemList())
              {
                  try {
                      if (!TextUtils.equals(item.getType(),"video"))
                      {
                          continue;
                      }
                      title=item.getData().getTitle();
                      imgUrl=item.getData().getCover().getDetail();
                      playUrl=item.getData().getPlayUrl();
                      duration=item.getData().getDuration();
                  }catch (NullPointerException e)
                  {
                      e.printStackTrace();
                  }
                  list.add(new VideoInfo(title,playUrl,imgUrl,duration));
              }
          }

        return list;
    }

}
