package learn.example.pile.jsonbean;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2016/8/11.
 */
public class JokeBean {


    private String message;
    private DataBean data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private boolean has_more;
        private boolean has_new_message;
        private double max_time;
        private int min_time;

        private List<DataListBean> data;

        public boolean isHas_more() {
            return has_more;
        }

        public boolean isHas_new_message() {
            return has_new_message;
        }

        public double getMax_time() {
            return max_time;
        }

        public int getMin_time() {
            return min_time;
        }

        public List<DataListBean> getData() {
            return data;
        }

        public static class DataListBean {
            private double display_time;
            private GroupBean group;
            private int type;

            public double getDisplay_time() {
                return display_time;
            }

            public GroupBean getGroup() {
                return group;
            }

            public int getType() {
                return type;
            }

            public static class GroupBean {

                @SerializedName(value ="text",alternate = "title")
                private String text;
                private int comment_count;
                private int digg_count;
                private int bury_count;
                private int create_time;
                private String id_str;
                private int is_gif=-1;
                private int is_video=-1;
                private String mp4_url;
                private JsonObject gifvideo;
                private User user;
                private int is_multi_image=-1;
                @SerializedName(value = "middle_image",alternate = "large_cover")
                private ImagesBean images;
                private ImagesBean[] large_image_list;

                public String getText() {
                    return text;
                }

                public int getComment_count() {
                    return comment_count;
                }

                public int getDigg_count() {
                    return digg_count;
                }

                public int getBury_count() {
                    return bury_count;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public String getGroup_id() {
                    return id_str;
                }

                public boolean is_gif() {
                    return is_gif==1;
                }

                public String getMp4_url() {
                    return mp4_url;
                }

                public User getUser() {
                    return user;
                }

                public JsonObject getGifVideo() {
                    return gifvideo;
                }

                public String getGifUrl() {
                    if (gifvideo==null)
                    {
                        return null;
                    }
                    JsonObject origin=gifvideo.getAsJsonObject("origin_video");
                    JsonArray array=origin.getAsJsonArray("url_list");
                    return array==null||array.size()<=0?null:array.get(0).getAsJsonObject().get("url").getAsString();
                }

                public ImagesBean getImages() {
                    return images;
                }

                public boolean is_multi_image() {
                    return is_multi_image==1;
                }

                public boolean is_video() {
                    return is_video==1;
                }

                public ImagesBean[] getLarge_image_list() {
                    return large_image_list;
                }

                public static class ImagesBean {
                    UrlBean[] url_list;
                    boolean is_gif;
                    String url;
                    public UrlBean[] getUrl_list() {
                        return url_list;
                    }

                    public boolean is_gif() {
                        return is_gif;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public String getFirstUrl()
                    {
                        return url_list==null||url_list.length<=0?null:url_list[0].url;
                    }

                    public static class UrlBean{
                        private String url;

                        public String getUrl() {
                            return url;
                        }
                    }
                }

                public static class User
                {

                    private long user_id;
                    private String name;
                    private String avatar_url;

                    public long getUser_id() {
                        return user_id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getAvatar_url() {
                        return avatar_url;
                    }

                }
            }
        }
    }
}
