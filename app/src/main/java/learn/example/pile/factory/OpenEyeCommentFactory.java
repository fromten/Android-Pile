package learn.example.pile.factory;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import learn.example.pile.object.Comment;
import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/8/16.
 */
public class OpenEyeCommentFactory implements CommentFactory.ProduceInterface {

    OpenEyeCommentFactory() {
    }

    @Override
    public Comment produceComment(String responseStr) {
        JsonReader reader = new JsonReader(new StringReader(responseStr));
        try {
            reader.beginObject();
            Comment comment=new Comment();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "replyList":
                        comment.setComments(readArray(reader));
                        break;
                    case "nextPageUrl":
                        JsonObject object = new JsonObject();
                        object.addProperty("nextPageUrl", reader.nextString());
                        comment.setExtraMsg(object);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            return comment;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private List<Comment.CommentItem> readArray(JsonReader reader) throws IOException {
        reader.beginArray();
        List<Comment.CommentItem> list = new ArrayList<>();
        while (reader.hasNext()) {
            list.add(readObject(reader));
        }
        reader.endArray();
        return list;
    }
    private Comment.CommentItem readObject(JsonReader reader) throws IOException {
        reader.beginObject();
        Comment.CommentItem commentItem=new Comment.CommentItem();
        while (reader.hasNext())
        {   String name=reader.nextName();
            switch (name) {
                case "createTime":
                    commentItem.setTime(TimeUtil.formatYMD(reader.nextLong()/1000));
                    break;
                case "likeCount":
                    commentItem.setLikeNumber(reader.nextInt());
                    break;
                case "message":
                    commentItem.setContent(reader.nextString());
                    break;
                case "user":
                    commentItem.setUsePic(getUserPic(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return commentItem;
    }

    private String getUserPic(JsonReader reader) throws IOException {
        reader.beginObject();
        String url=null;
        while (reader.hasNext())
        {
            String name=reader.nextName();
            if (name.equals("avatar"))
            {  url=reader.nextString();
            }else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return url;
    }
}
