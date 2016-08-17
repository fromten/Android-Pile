package learn.example.pile.factory;


import android.text.Editable;
import android.text.Spannable;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import learn.example.pile.object.Comment;
import okhttp3.internal.cache.CacheStrategy;

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

    public <T extends ProduceInterface> Comment produceComment(Class<T> clazz,String response)
    {
        try {
            ProduceInterface producer=clazz.newInstance();
            return producer.produceComment(response);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Disable access construction method ,must have default empty construction " +
                "method ");
    }
}
