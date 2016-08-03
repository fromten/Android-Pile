package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.jsonbean.ZhihuComment;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuContentService;
import learn.example.pile.object.Comment;
import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/8/3.
 */
public class ZhihuCommentFragment extends CommentFragment implements IService.Callback<ZhihuComment> {
    private int docID;
    private ZhihuContentService mCommentService;

    private boolean hadRequestedShortComment;

    public static ZhihuCommentFragment newInstance(int docID) {
        ZhihuCommentFragment fragment = new ZhihuCommentFragment();
        fragment.docID=docID;
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCommentService=new ZhihuContentService();
        mCommentService.getLongComment(docID,this);
    }

    @Override
    public void onSuccess(ZhihuComment data) {
        addComments(new ZhihuCommentFactory().getCommentList(data));
        notifySuccess();
    }

    @Override
    public void onFailure(String message) {
         notifyError();
    }

    @Override
    public void onLoadMore() {
        if (!hadRequestedShortComment)
        {
            mCommentService.getShortComment(docID,this);
            hadRequestedShortComment=true;
        }
    }

    @Override
    public void onDestroy() {
        if (mCommentService!=null)
         mCommentService.cancelAll();
        super.onDestroy();
    }

    public static class ZhihuCommentFactory{
        public List<Comment> getCommentList(ZhihuComment zhihuComment) {
            List<Comment> list = new ArrayList<>();
            for (ZhihuComment.CommentsBean item : zhihuComment.getComments()) {
                Comment comment = getComment(item);
                if (comment != null)
                    list.add(comment);
            }
            return list;
        }
        public Comment getComment(ZhihuComment.CommentsBean zhihuCommentItem) {
            String timeStr = TimeUtil.formatYMD(zhihuCommentItem.getTime());
            Comment comment = new Comment
                    (zhihuCommentItem.getAuthor(),
                            zhihuCommentItem.getLikes(), timeStr,
                            zhihuCommentItem.getAvatar(), null,
                            zhihuCommentItem.getContent());
            return comment;
        }
    }
}
