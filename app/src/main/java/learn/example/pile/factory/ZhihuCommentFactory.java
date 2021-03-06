package learn.example.pile.factory;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import learn.example.pile.pojo.Comment;
import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/8/16.
 */
public class ZhihuCommentFactory implements CommentFactory.ProduceInterface {

    ZhihuCommentFactory()
    {

    }
    @Override
    public Comment produceComment(String responseStr) {
        JsonReader reader=new JsonReader(new StringReader(responseStr));
        Comment comment=null;
        try {
            reader.beginObject();
            while (reader.hasNext())
            {
                String name=reader.nextName();
                if (name.equals("comments"))
                {
                    List<Comment.CommentItem> list=new ArrayList<>();
                    reader.beginArray();
                    while (reader.hasNext())
                    {
                        list.add(readComment(reader));
                    }
                    reader.endArray();

                    comment=new Comment();
                    comment.setComments(list);
                }else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return comment;
    }


    private Comment.CommentItem readComment(JsonReader reader) throws IOException {
        reader.beginObject();
        Comment.CommentItem comment=new Comment.CommentItem();
        while (reader.hasNext())
        {
            String name=reader.nextName();
            switch (name) {
                case "author":
                    comment.setUserName(reader.nextString());
                    break;
                case "content":
                    comment.setContent(reader.nextString());
                    break;
                case "likes":
                    comment.setLikeNumber(reader.nextInt());
                    break;
                case "time":
                    comment.setCommentTime(TimeUtil.formatTime(TimeUtil.FORMAT_YMD_HM,reader.nextInt()));
                    break;
                case "avatar":
                    comment.setAvatar(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return comment;
    }
}
