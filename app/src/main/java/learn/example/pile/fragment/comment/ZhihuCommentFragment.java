package learn.example.pile.fragment.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import learn.example.pile.factory.CommentFactory;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuContentService;
import learn.example.pile.pojo.Comment;

/**
 * Created on 2016/8/3.
 */
public class ZhihuCommentFragment extends CommentFragment implements IService.Callback<String> {

    public static final String ARGUMENT_DOCID = "ARGUMENT_DOCID";

    private int docID;
    private ZhihuContentService mCommentService;

    private boolean hadRequestedShortComment;

    public static ZhihuCommentFragment newInstance(int zhihuDocID) {

        Bundle args = new Bundle();
        args.putInt(ARGUMENT_DOCID, zhihuDocID);
        ZhihuCommentFragment fragment = new ZhihuCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        super.onViewCreated(view, savedInstanceState);
        docID = args.getInt(ARGUMENT_DOCID);
        mCommentService = new ZhihuContentService();
        if (savedInstanceState == null) {
            mCommentService.getLongComment(docID, this);
        }
    }


    @Override
    public void onSuccess(String data) {
        Comment comment = CommentFactory.newInstance().produceZhihuComment(data);
        if (comment != null) {
            addComments(comment.getComments());
        }
        notifySuccess();

        //请求长评论成功后请求短评论
        requestShortComment();
    }

    @Override
    public void onFailure(String message) {
        notifyError();

        //请求长评论失败后请求短评论
        requestShortComment();
    }

    @Override
    public void onDestroy() {
        if (mCommentService != null)
            mCommentService.cancelAll();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        mCommentService.getLongComment(docID, this);
    }

    private void requestShortComment() {
        if (!hadRequestedShortComment && mCommentService != null) {
            mCommentService.getShortComment(docID, this);
            hadRequestedShortComment = true;
            notifyRequestEnd();
        }
    }

}
