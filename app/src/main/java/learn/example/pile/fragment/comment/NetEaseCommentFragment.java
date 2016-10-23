package learn.example.pile.fragment.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import learn.example.pile.factory.CommentFactory;
import learn.example.pile.factory.NetEaseCommentFactory;
import learn.example.pile.net.NetEaseNewsService;
import learn.example.pile.pojo.Comment;

/**
 * Created on 2016/8/3.
 */
public class NetEaseCommentFragment extends CommentFragment implements NetEaseNewsService.Callback<String>{

    private static final String KEY_POSITION="position";
    public static final String KEY_DOCID="docId";
    public static final String KEY_BOARDID="boardID";

    private String docId;
    private String boardId;

    private final int MAX_LEN=10;
    private int currentPos=0;

    private NetEaseNewsService mNetEaseNewsService;


    public static NetEaseCommentFragment newInstance(String docId,String boardID) {
        Bundle bundle=new Bundle();
        NetEaseCommentFragment fragment = new NetEaseCommentFragment();
        bundle.putString(KEY_DOCID,docId);
        bundle.putString(KEY_BOARDID,boardID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle argument=getArguments();
        if (argument!=null)
        {
            docId=argument.getString(KEY_DOCID);
            boardId=argument.getString(KEY_BOARDID);

            mNetEaseNewsService =new NetEaseNewsService();
            if (savedInstanceState!=null)
            {
                currentPos=savedInstanceState.getInt(KEY_POSITION,0);
            }

            if (currentPos<=0)
            {
                mNetEaseNewsService.getHotComment(boardId,docId,currentPos,10,this);
            }
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_POSITION,currentPos);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSuccess(String data) {
        Comment comment=CommentFactory.newInstance().produceComment(NetEaseCommentFactory.class,data);
        if (comment!=null)
        {
            addComments(comment.getComments());
        }

        notifySuccess();
    }

    @Override
    public void onFailure(String message) {
        notifyError();
    }

    @Override
    public void onRefresh() {
        if (mNetEaseNewsService!=null)
        mNetEaseNewsService.getHotComment(boardId,docId,0,10,this);
    }

    @Override
    public void onLoadMore() {
       currentPos+=MAX_LEN;
       if (mNetEaseNewsService!=null)
       mNetEaseNewsService.getNormalComment(boardId,docId,currentPos,MAX_LEN,this);
    }

    @Override
    public void onDestroy() {
        if (mNetEaseNewsService !=null)
        mNetEaseNewsService.cancelAll();
        super.onDestroy();
    }



}
