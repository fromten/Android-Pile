package learn.example.pile.jsonbean;

import java.util.List;

/**
 * Created on 2016/8/5.
 */
public class OpenEyeComment {


    private int count;
    private int total;
    private String nextPageUrl;
    private List<ReplyListBean> replyList;

    public int getCount() {
        return count;
    }


    public int getTotal() {
        return total;
    }
    public String getNextPageUrl() {
        return nextPageUrl;
    }


    public List<ReplyListBean> getReplyList() {
        return replyList;
    }


    public static class ReplyListBean {
        private long id;
        private int videoId;
        private String videoTitle;
        private int parentReplyId;
        private int sequence;
        private String message;
        private String replyStatus;
        private long createTime;
        private UserBean user;
        private int likeCount;
        private boolean liked;
        private boolean hot;

        public long getId() {
            return id;
        }

        public int getVideoId() {
            return videoId;
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public int getParentReplyId() {
            return parentReplyId;
        }

        public int getSequence() {
            return sequence;
        }

        public String getMessage() {
            return message;
        }

        public String getReplyStatus() {
            return replyStatus;
        }

        public long getCreateTime() {
            return createTime;
        }

        public UserBean getUser() {
            return user;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public boolean isLiked() {
            return liked;
        }

        public boolean isHot() {
            return hot;
        }

        public void setHot(boolean hot) {
            this.hot = hot;
        }

        public static class UserBean {
            private int uid;
            private String nickname;
            private String avatar;

            public int getUid() {
                return uid;
            }

            public String getNickname() {
                return nickname;
            }

            public String getAvatar() {
                return avatar;
            }
        }
    }
}
