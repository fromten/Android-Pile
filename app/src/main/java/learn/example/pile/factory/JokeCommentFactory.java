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
        public List<Comment> produceComment(String responseStr) {
            JsonReader reader=new JsonReader(new StringReader(responseStr));
            List<Comment> list = null;
            try {
                reader.beginObject();
                while (reader.hasNext())
                {
                    String name=reader.nextName();
                    if (name.equals("data"))
                    {
                        list=new ArrayList<>();
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
                    }else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
                return list;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void readArray(JsonReader reader,List<Comment> outList) throws IOException {

            reader.beginArray();
            while (reader.hasNext())
            {
                outList.add(readComment(reader));
            }
            reader.endArray();
        }

       private Comment readComment(JsonReader reader) throws IOException {
             reader.beginObject();
             String avatar = null;
             String userName = null;
             String text = null;
             int likeCount=0;
             long time=0;
             while (reader.hasNext())
             {
                 String name=reader.nextName();
                 if (name.equals("avatar_url"))
                 {
                     avatar=reader.nextString();
                 }else if (name.equals("digg_count"))
                 {
                     likeCount=reader.nextInt();
                 }else if (name.equals("text"))
                 {
                     text=reader.nextString();
                 }else if (name.equals("create_time"))
                 {
                     time=reader.nextLong();
                 }else if (name.equals("user_name"))
                 {
                     userName=reader.nextString();
                 }else {
                     reader.skipValue();
                 }
             }
            reader.endObject();
            return new Comment(userName,likeCount, TimeUtil.formatYMD(time),avatar,null,text);
        }
}
