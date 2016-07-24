package learn.example.pile.factory;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.jsonbean.NetEaseComment;
import learn.example.pile.jsonbean.ZhihuComment;
import learn.example.pile.object.Comment;
import learn.example.pile.util.GsonHelper;
import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/7/23.
 */
public class CommentsFactory {


    public static CommentsFactory newInstance()
    {
        return new CommentsFactory();
    }


    public List<Comment> newNetEaseComments(NetEaseComment netEaseComment)
    {
       return new NetEaseScanner().scanAll(netEaseComment);
    }

    public List<Comment> newZhihuComments(ZhihuComment comment)
    {
        return new ZhihuScanner().scanAll(comment);
    }



    private static class NetEaseScanner{

        public List<Comment> scanAll(NetEaseComment netEaseComment)
        {
            List<Comment> list = new ArrayList<>();
            JsonArray array = netEaseComment.getHotPosts()==null?netEaseComment.getNewPosts():netEaseComment.getHotPosts();
            if (array!=null)
            {
                for (JsonElement item:array) {
                    try {
                        JsonObject object = item.getAsJsonObject();
                        Comment comment = scanItem(object.getAsJsonObject("1"));
                        if (comment != null)
                            list.add(comment);
                    }catch (NullPointerException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            return list;
        }

        public Comment scanItem(JsonObject netEaseCommen) {
            String author = GsonHelper.getAsString(netEaseCommen.get("n"), "网友");
            String time = GsonHelper.getAsString(netEaseCommen.get("t"), null);
            String content = GsonHelper.getAsString(netEaseCommen.get("b"), null);
            int like = GsonHelper.getAsInteger(netEaseCommen.get("v"), 0);
            String imgUrl = GsonHelper.getAsString(netEaseCommen.get("timg"), null);
            String address = GsonHelper.getAsString(netEaseCommen.get("f"), null);
            try {
                int index = address.indexOf("&nbsp");
                if (index > 0) {
                    address = address.substring(0, index);
                }
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                address = null;
            }
            return new Comment(author, like, time, imgUrl, address, content);
        }
    }


    private static class ZhihuScanner{

        public List<Comment> scanAll(ZhihuComment zhihuComment)
        {
            List<Comment> list = new ArrayList<>();
            for (ZhihuComment.CommentsBean item : zhihuComment.getComments()) {
                Comment comment = scanItem(item);
                if (comment != null)
                    list.add(comment);
            }
            return list;
        }

        public Comment scanItem(ZhihuComment.CommentsBean zhihuCommentItem) {
            String timeStr = TimeUtil.formatYMD(zhihuCommentItem.getTime());
            Comment comment = new Comment
                    (zhihuCommentItem.getAuthor(),
                            zhihuCommentItem.getLikes(), timeStr,
                            zhihuCommentItem.getAvatar(), null,
                            zhihuCommentItem.getContent());
            return comment;
        }
    }
}
