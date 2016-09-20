package learn.example.pile.jsonbean;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2016/7/19.
 */
public class NetEaseNews {

    private List<T1348647909107Bean> T1348647909107;

    public List<T1348647909107Bean> getT1348647909107() {
        return T1348647909107;
    }

    @Table(name = "News")
    public static class T1348647909107Bean extends Model implements Parcelable {

        @Column(name = "boardId")
        private String boardid;

        @Column(name="docId",unique = true,onUniqueConflict = Column.ConflictAction.REPLACE)
        @SerializedName(value = "docid",alternate = "id")
        private String docid;

        @Column(name = "imgUrl")
        @SerializedName(value = "img",alternate = "imgsrc")
        private String imgUrl;

        @Column(name = "replyCount")
        private int replyCount;

        @Column(name = "replyId")
        private String replyid;

        @Column(name = "source")
        private String source;

        @Column(name = "title")
        private String title;

        @Column(name = "skipId")
        private String skipID;

        @Column(name = "skipType")
        private String skipType;


        @Column(name = "imageUrls")
        @SerializedName(value = "imgnewextra",alternate = "imgextra")
        private ImageExtraBean[] imageUrls;

        private String replayString;


        public String getSource() {
            return source;
        }


        public String getTitle() {
            return title;
        }


        public String getBoardid() {
            return boardid;
        }



        public String getDocid() {
            return docid;
        }


        public String getImgsrc() {
            return imgUrl;
        }

        public int getReplyCount() {
            return replyCount;
        }

        public String getSkipType() {
            return skipType;
        }

        public void setReplayString(String replayString) {
            this.replayString = replayString;
        }

        public String getReplayString() {
            return replayString;
        }

        public ImageExtraBean[] getImgnewextra() {
            return imageUrls;
        }



        public static class ImageExtraBean implements Parcelable {

            private String imgsrc;

            public String getImgsrc() {
                return imgsrc;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.imgsrc);
            }

            public ImageExtraBean() {
                super();
            }

            protected ImageExtraBean(Parcel in) {
                this.imgsrc = in.readString();
            }

            public static final Creator<ImageExtraBean> CREATOR = new Creator<ImageExtraBean>() {
                @Override
                public ImageExtraBean createFromParcel(Parcel source) {
                    return new ImageExtraBean(source);
                }

                @Override
                public ImageExtraBean[] newArray(int size) {
                    return new ImageExtraBean[size];
                }
            };
        }

        public String getSkipID() {
            return skipID;
        }

        public T1348647909107Bean() {
            super();
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.boardid);
            dest.writeString(this.docid);
            dest.writeString(this.imgUrl);
            dest.writeInt(this.replyCount);
            dest.writeString(this.replayString);
            dest.writeString(this.replyid);
            dest.writeString(this.source);
            dest.writeString(this.title);
            dest.writeString(this.skipID);
            dest.writeString(this.skipType);
            dest.writeTypedArray(this.imageUrls, flags);
        }

        protected T1348647909107Bean(Parcel in) {
            this.boardid = in.readString();
            this.docid = in.readString();
            this.imgUrl = in.readString();
            this.replyCount = in.readInt();
            this.replayString = in.readString();
            this.replyid = in.readString();
            this.source = in.readString();
            this.title = in.readString();
            this.skipID = in.readString();
            this.skipType = in.readString();
            this.imageUrls = in.createTypedArray(ImageExtraBean.CREATOR);
        }

        public static final Creator<T1348647909107Bean> CREATOR = new Creator<T1348647909107Bean>() {
            @Override
            public T1348647909107Bean createFromParcel(Parcel source) {
                return new T1348647909107Bean(source);
            }

            @Override
            public T1348647909107Bean[] newArray(int size) {
                return new T1348647909107Bean[size];
            }
        };
    }
}
