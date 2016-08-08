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

    private static final String KEY_DOCID="zhihudocid";

    private int docID;
    private ZhihuContentService mCommentService;

    private boolean hadRequestedShortComment;

    public static ZhihuCommentFragment newInstance(int zhihuDocID) {

        Bundle args = new Bundle();
        args.putInt(KEY_DOCID,zhihuDocID);
        ZhihuCommentFragment fragment = new ZhihuCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args=getArguments();
        if (args!=null)
        {
            docID=args.getInt(KEY_DOCID);
            mCommentService=new ZhihuContentService();
            if (savedInstanceState==null)
            {
                mCommentService.getLongComment(docID,this);
            }
        }

    }

    @Override
    public void onSuccess(ZhihuComment data) {
        addComments(new ZhihuCommentFactory().getCommentList(data));
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
        if (mCommentService!=null)
         mCommentService.cancelAll();
        super.onDestroy();
    }

    private void requestShortComment()
    {
        if (!hadRequestedShortComment&&mCommentService!=null)
        {
            mCommentService.getShortComment(docID,this);
            hadRequestedShortComment=true;
            notifyRequestEnd();
        }
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
