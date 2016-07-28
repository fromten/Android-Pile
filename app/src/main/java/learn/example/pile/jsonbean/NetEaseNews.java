package learn.example.pile.jsonbean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created on 2016/7/19.
 */
public class NetEaseNews {

    private List<T1348647909107Bean> T1348647909107;

    public List<T1348647909107Bean> getT1348647909107() {
        return T1348647909107;
    }

    public static class T1348647909107Bean implements Parcelable {
        private String boardid;
        private int clkNum;
        private String digest;
        private String docid;
        private int downTimes;
        private String id;
        private String img;
        private int imgType;
        private String imgsrc;
        private String interest;
        private String lmodify;
        private int picCount;
        private String program;
        private String prompt;
        private String ptime;
        private String recReason;
        private String recSource;
        private int recType;
        private String recprog;
        private int replyCount;
        private String replyid;
        private String source;
        private String template;
        private String title;
        private int upTimes;
        private String skipID;
        private ImageExtraBean[] imgnewextra;



        public String getReplyid() {
            return replyid;
        }

        public String getSource() {
            return source;
        }

        public String getTemplate() {
            return template;
        }

        public String getTitle() {
            return title;
        }

        public int getUpTimes() {
            return upTimes;
        }

        public String getBoardid() {
            return boardid;
        }

        public int getClkNum() {
            return clkNum;
        }

        public String getDigest() {
            return digest;
        }

        public String getDocid() {
            return docid;
        }

        public int getDownTimes() {
            return downTimes;
        }

        public String getId() {
            return id;
        }

        public String getImg() {
            return img;
        }

        public int getImgType() {
            return imgType;
        }

        public String getImgsrc() {
            return imgsrc;
        }

        public String getInterest() {
            return interest;
        }

        public String getLmodify() {
            return lmodify;
        }

        public int getPicCount() {
            return picCount;
        }

        public String getProgram() {
            return program;
        }

        public String getPrompt() {
            return prompt;
        }

        public String getPtime() {
            return ptime;
        }

        public String getRecReason() {
            return recReason;
        }

        public String getRecSource() {
            return recSource;
        }

        public int getRecType() {
            return recType;
        }

        public String getRecprog() {
            return recprog;
        }

        public int getReplyCount() {
            return replyCount;
        }



        public ImageExtraBean[] getImgnewextra() {
            return imgnewextra;
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
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.boardid);
            dest.writeInt(this.clkNum);
            dest.writeString(this.digest);
            dest.writeString(this.docid);
            dest.writeInt(this.downTimes);
            dest.writeString(this.id);
            dest.writeString(this.img);
            dest.writeInt(this.imgType);
            dest.writeString(this.imgsrc);
            dest.writeString(this.interest);
            dest.writeString(this.lmodify);
            dest.writeInt(this.picCount);
            dest.writeString(this.program);
            dest.writeString(this.prompt);
            dest.writeString(this.ptime);
            dest.writeString(this.recReason);
            dest.writeString(this.recSource);
            dest.writeInt(this.recType);
            dest.writeString(this.recprog);
            dest.writeInt(this.replyCount);
            dest.writeString(this.replyid);
            dest.writeString(this.source);
            dest.writeString(this.template);
            dest.writeString(this.title);
            dest.writeInt(this.upTimes);
            dest.writeString(this.skipID);
            dest.writeTypedArray(this.imgnewextra, flags);
        }

        protected T1348647909107Bean(Parcel in) {
            this.boardid = in.readString();
            this.clkNum = in.readInt();
            this.digest = in.readString();
            this.docid = in.readString();
            this.downTimes = in.readInt();
            this.id = in.readString();
            this.img = in.readString();
            this.imgType = in.readInt();
            this.imgsrc = in.readString();
            this.interest = in.readString();
            this.lmodify = in.readString();
            this.picCount = in.readInt();
            this.program = in.readString();
            this.prompt = in.readString();
            this.ptime = in.readString();
            this.recReason = in.readString();
            this.recSource = in.readString();
            this.recType = in.readInt();
            this.recprog = in.readString();
            this.replyCount = in.readInt();
            this.replyid = in.readString();
            this.source = in.readString();
            this.template = in.readString();
            this.title = in.readString();
            this.upTimes = in.readInt();
            this.skipID = in.readString();
            this.imgnewextra = in.createTypedArray(ImageExtraBean.CREATOR);
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
