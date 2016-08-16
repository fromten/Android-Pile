package learn.example.pile.factory;

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
public class ZhihuCommentFactory implements CommentFactory.ProduceInterface {

    @Override
    public Comment produceComment(String responseStr) {
        JsonReader reader=new JsonReader(new StringReader(responseStr));
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

                    Comment comment=new Comment();
                    comment.setComments(list);
                    return comment;
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

        return null;
    }


    private Comment.CommentItem readComment(JsonReader reader) throws IOException {
        reader.beginObject();
        Comment.CommentItem comment=new Comment.CommentItem();
        while (reader.hasNext())
        {
            String name=reader.nextName();
            switch (name) {
                case "author":
                    comment.setAuthor(reader.nextString());
                    break;
                case "content":
                    comment.setContent(reader.nextString());
                    break;
                case "likes":
                    comment.setLikeNumber(reader.nextInt());
                    break;
                case "time":
                    comment.setTime(TimeUtil.formatYMD(reader.nextInt()));
                    break;
                case "avatar":
                    comment.setUsePic(reader.nextString());
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
