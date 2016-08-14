package learn.example.pile.factory;


import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import learn.example.pile.object.Comment;

/**
 * Created on 2016/8/14.
 */
public class CommentFactory {

    public interface ProduceInterface{
         List<Comment> produceComment(String responseStr);
    }

    public static CommentFactory newInstance() {
        return new CommentFactory();
    }

    public List<Comment> produceJokeComment(String responseStr)
    {
         return new JokeCommentFactory().produceComment(responseStr);
    }


}
