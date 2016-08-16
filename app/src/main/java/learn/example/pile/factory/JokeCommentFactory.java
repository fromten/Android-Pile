package learn.example.pile.factory;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import learn.example.pile.object.Comment;
import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/8/14.
 */
public class JokeCommentFactory implements CommentFactory.ProduceInterface {

    JokeCommentFactory() {
    }

    @Override
        public Comment produceComment(String responseStr) {
            JsonReader reader=new JsonReader(new StringReader(responseStr));
            try {
                reader.beginObject();
                while (reader.hasNext())
                {
                    String name=reader.nextName();
                    if (name.equals("data"))
                    {
                        List<Comment.CommentItem> list = null;list=new ArrayList<>();
                        reader.beginObject();
                        while (reader.hasNext())
                        {
                            String n=reader.nextName();
                            if (n.equals("recent_comments")||n.equals("top_comments"))
                            {
                               readArray(reader,list);
                            }else {
                               reader.skipValue();
                            }
                        }
                        reader.endObject();
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

        private void readArray(JsonReader reader,List<Comment.CommentItem> outList) throws IOException {

            reader.beginArray();
            while (reader.hasNext())
            {
                outList.add(readComment(reader));
            }
            reader.endArray();
        }

       private Comment.CommentItem readComment(JsonReader reader) throws IOException {
             reader.beginObject();
             String avatar = null;
             String userName = null;
             String text = null;
             int likeCount=0;
             long time=0;
             while (reader.hasNext())
             {
                 String name=reader.nextName();
                 switch (name) {
                     case "avatar_url":
                         avatar = reader.nextString();
                         break;
                     case "digg_count":
                         likeCount = reader.nextInt();
                         break;
                     case "text":
                         text = reader.nextString();
                         break;
                     case "create_time":
                         time = reader.nextLong();
                         break;
                     case "user_name":
                         userName = reader.nextString();
                         break;
                     default:
                         reader.skipValue();
                         break;
                 }
             }
            reader.endObject();
            return new Comment.CommentItem(userName,likeCount, TimeUtil.formatYMD(time),avatar,null,text);
        }
}
