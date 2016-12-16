package learn.example.pile.factory;




import learn.example.pile.pojo.Comment;


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


    public static CommentFactory newInstance() {
        return new CommentFactory();
    }

    public Comment produceZhihuComment(String response)
    {
        return new ZhihuCommentFactory().produceComment(response);
    }

    public Comment produceNetEaseComment(String response)
    {
        return new NetEaseCommentFactory().produceComment(response);
    }

    public Comment produceOpenEyesComment(String response)
    {
        return new OpenEyeCommentFactory().produceComment(response);
    }

    public Comment produceJokeComment(String response)
    {
        return new JokeCommentFactory().produceComment(response);
    }

}
