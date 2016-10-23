package learn.example.pile.fragment.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.Comparator;

import learn.example.pile.factory.CommentFactory;
import learn.example.pile.factory.JokeCommentFactory;
import learn.example.pile.net.IService;
import learn.example.pile.net.JokeService;
import learn.example.pile.pojo.Comment;

/**
 * Created on 2016/8/14.
 */
public class JokeCommentFragment extends CommentFragment implements IService.Callback<String> {

     private JokeService mJokeService;

     public final static String KEY_LAST_POSITION="last_start";
     public final static String KEY_GROUP_ID="group_id";


    public static JokeCommentFragment newInstance(String groupId) {
        Bundle args = new Bundle();
        args.putString(KEY_GROUP_ID,groupId);
        JokeCommentFragment fragment = new JokeCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String groupId;
    private int start;
    private final int count=20;
    private boolean hasMore;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args=getArguments();
        if (args==null)
        {
            return;
        }
        super.onViewCreated(view, savedInstanceState);
        mJokeService=new JokeService();
        groupId=args.getString(KEY_GROUP_ID);

        if (savedInstanceState!=null)
        {
            start=savedInstanceState.getInt(KEY_LAST_POSITION,0);
        }

        if (start<=0)
        {
            mJokeService.getComment(start,20,groupId,this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_LAST_POSITION,start);//保存请求开始位置
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSuccess(String data) {
        Comment comment=CommentFactory.newInstance().produceComment(JokeCommentFactory.class,data);
        if (comment!=null) {
            Collections.sort(comment.getComments(), new Comparator<Comment.CommentItem>() {
                @Override
                public int compare(Comment.CommentItem lhs, Comment.CommentItem rhs) {
                    return rhs.getLikeNumber() - lhs.getLikeNumber();
                }
            });
            addComments(comment.getComments());

            JsonObject extra = comment.getExtraMsg();
            hasMore = extra != null && extra.get("has_more").getAsBoolean();
        }
        start+=count;
        notifySuccess();
    }

    @Override
    public void onFailure(String message) {
        notifyError();
    }


    @Override
    public void onRefresh() {
        //重头开始
        mJokeService.getComment(0,20,groupId,this);
    }

    @Override
    public void onLoadMore() {
        if (hasMore)
        {
            mJokeService.getComment(start,count,groupId,this);
        }else {
            notifyRequestEnd();
        }

    }
}
