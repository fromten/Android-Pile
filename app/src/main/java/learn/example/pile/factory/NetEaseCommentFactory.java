package learn.example.pile.factory;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import learn.example.pile.pojo.Comment;

/**
 * Created on 2016/8/16.
 */
public class NetEaseCommentFactory implements CommentFactory.ProduceInterface {

    NetEaseCommentFactory() {
    }

    @Override
    public Comment produceComment(String responseStr) {
        JsonReader reader=new JsonReader(new StringReader(responseStr));
        reader.setLenient(true);
        Comment comment = null;
        try {
            reader.beginObject();
            while (reader.hasNext())
            {
                String keyName=reader.nextName();
                if (keyName.equals("hotPosts")||keyName.equals("newPosts"))//热门和普通评论
                {
                    List<Comment.CommentItem> list=new ArrayList<>();
                    readArray(reader,list);

                    comment=new Comment();
                    comment.setComments(list);

                }else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException|IndexOutOfBoundsException e) {
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
               reader.beginObject();
               while (reader.hasNext())
               {
                   String name=reader.nextName();
                   if (name.equals("1"))
                   {
                       outList.add(readComment(reader));
                   }else {
                       reader.skipValue();
                   }
               }
               reader.endObject();

           }
           reader.endArray();
    }

    private Comment.CommentItem readComment(JsonReader reader) throws IOException {
        reader.beginObject();
        Comment.CommentItem comment=new Comment.CommentItem();
        while (reader.hasNext())
        {   String name=reader.nextName();
            switch (name) {
                case "n":
                    comment.setAuthor(reader.nextString());
                    break;
                case "t":
                    comment.setTime(reader.nextString());
                    break;
                case "b":
                    comment.setContent(reader.nextString());
                    break;
                case "v":
                    comment.setLikeNumber(reader.nextInt());
                    break;
                case "timg":
                    comment.setUsePic(reader.nextString());
                    break;
                case "f":
                    String address=reader.nextString();
                    int index = address!=null?address.indexOf("&nbsp"):0;
                    if (index > 0) {
                        address = address.substring(0, index);
                    }
                    comment.setAddress(address);
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
