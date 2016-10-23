package learn.example.pile.factory;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import learn.example.pile.pojo.Comment;
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
            Comment comment = new Comment();
            try {
                reader.beginObject();
                while (reader.hasNext())
                {
                    String name=reader.nextName();
                    switch (name) {
                        case "data":
                            reader.beginObject();
                            List<Comment.CommentItem> list = new ArrayList<>();
                            while (reader.hasNext()) {
                                String n = reader.nextName();
                                if (n.equals("recent_comments")||n.equals("top_comments")) {
                                    readArray(reader, list);
                                    comment.setComments(list);
                                } else {
                                    reader.skipValue();
                                }
                            }
                            reader.endObject();
                            break;
                        case "has_more":
                            JsonObject object = new JsonObject();
                            object.addProperty("has_more", reader.nextBoolean());
                            comment.setExtraMsg(object);
                            break;
                        default:
                            reader.skipValue();
                            break;
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
            return new Comment.CommentItem(userName,likeCount,
                                            TimeUtil.formatTime(TimeUtil.FORMAT_YMD_HM,time),
                                            avatar,null,text);
        }
}
