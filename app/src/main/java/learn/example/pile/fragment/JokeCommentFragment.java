package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import learn.example.pile.factory.CommentFactory;
import learn.example.pile.net.IService;
import learn.example.pile.net.JokeService;
import learn.example.pile.object.Comment;

/**
 * Created on 2016/8/14.
 */
public class JokeCommentFragment extends CommentFragment implements IService.Callback<String> {

     private JokeService mJokeService;

     public final static String KEY_LAST_POSITION="last_position";
     public final static String KEY_GROUP_ID="group_id";


    public static JokeCommentFragment newInstance(long groupId) {

        Bundle args = new Bundle();
        args.putLong(KEY_GROUP_ID,groupId);
        JokeCommentFragment fragment = new JokeCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private long groupId;
    private int start;
    private final int count=20;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args=getArguments();
        if (args==null)
        {
            return;
        }
        mJokeService=new JokeService();
        groupId=args.getLong(KEY_GROUP_ID);
        if (savedInstanceState==null)
        {
            mJokeService.getComment(start,20,groupId,this);
        }else {
            start=savedInstanceState.getInt(KEY_LAST_POSITION);
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_LAST_POSITION,start);//保存请求开始位置
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSuccess(String data) {
        addComments(CommentFactory.newInstance().produceJokeComment(data));
        start+=count;
        notifySuccess();
    }

    @Override
    public void onFailure(String message) {
        notifyError();
    }

    @Override
    public void onLoadMore() {
        mJokeService.getComment(start,count,groupId,this);
    }
}
