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
public class NetEaseCommentFragment extends CommentFragment implements NetEaseNewsService.Callback<String> {

    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String ARGUMENT_DOCID = "ARGUMENT_DOCID";
    public static final String ARGUMENT_BOARDID = "ARGUMENT_BOARDID";

    private String docId;
    private String boardId;

    private final int MAX_LEN = 10;
    private int currentPos = 0;

    private NetEaseNewsService mNetEaseNewsService;


    public static NetEaseCommentFragment newInstance(String docId, String boardID) {
        Bundle bundle = new Bundle();
        NetEaseCommentFragment fragment = new NetEaseCommentFragment();
        bundle.putString(ARGUMENT_DOCID, docId);
        bundle.putString(ARGUMENT_BOARDID, boardID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle argument = getArguments();
        if (argument != null) {
            docId = argument.getString(ARGUMENT_DOCID);
            boardId = argument.getString(ARGUMENT_BOARDID);

            mNetEaseNewsService = new NetEaseNewsService();
            if (savedInstanceState != null) {
                currentPos = savedInstanceState.getInt(STATE_POSITION, 0);
            }
            if (currentPos <= 0) {
                mNetEaseNewsService.getHotComment(boardId, docId, currentPos, 10, this);
            }
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, currentPos);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSuccess(String data) {
        Comment comment = CommentFactory.newInstance().produceNetEaseComment(data);
        if (comment != null) {
            addComments(comment.getComments());
            notifySuccess();
        }else {
            notifyNonComment();
        }
    }

    @Override
    public void onFailure(String message) {
        notifyError();
    }

    @Override
    public void onRefresh() {
        if (mNetEaseNewsService != null) {
            mNetEaseNewsService.getHotComment(boardId, docId, 0, 10, this);
        }

    }

    @Override
    public void onLoadMore() {
        currentPos += MAX_LEN;
        if (mNetEaseNewsService != null) {
            mNetEaseNewsService.getNormalComment(boardId, docId, currentPos, MAX_LEN, this);
        }
    }

    @Override
    public void onDestroy() {
        if (mNetEaseNewsService != null)
            mNetEaseNewsService.cancelAll();
        super.onDestroy();
    }


}
