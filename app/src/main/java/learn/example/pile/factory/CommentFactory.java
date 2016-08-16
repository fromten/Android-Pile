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

    private CommentFactory()
    {

    }

    public interface ProduceInterface{
        /**
         * 生成评论
         * @param responseStr 网络请求响应的数据
         * @return 新的Comment
         */
         Comment produceComment(String responseStr);
    }

    //网易
    public static CommentFactory newInstance() {
        return new CommentFactory();
    }
    //笑话
    public Comment produceJokeComment(String responseStr)
    {
         return new JokeCommentFactory().produceComment(responseStr);
    }

    public Comment produceNetEaseComment(String responseStr)
    {
        return new NetEaseCommentFactory().produceComment(responseStr);
    }

    //知乎
    public Comment produceZhihuComment(String responseStr)
    {
        return new ZhihuCommentFactory().produceComment(responseStr);
    }

    //开眼
    public Comment produceOpenEyeComment(String responseStr)
    {
        return new OpenEyeCommentFactory().produceComment(responseStr);
    }


}
