package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.jsonbean.OpenEyeComment;
import learn.example.pile.net.IService;
import learn.example.pile.net.OpenEyeService;
import learn.example.pile.object.Comment;
import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/8/5.
 */
public class OpenEyeCommentFragment  extends CommentFragment implements IService.Callback<OpenEyeComment>{

    public static final String KEY_NEXT_PAGE_URL="url";
    private static final String KEY_VIDEO_ID="id";
    private OpenEyeService mOpenEyeService;
    private int id;
    private String nextPageUrl;

    public static OpenEyeCommentFragment newInstance(int videoId) {

        Bundle args = new Bundle();
        args.putInt(KEY_VIDEO_ID,videoId);
        OpenEyeCommentFragment fragment = new OpenEyeCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args=getArguments();
        if (args!=null)
        {
            id=args.getInt(KEY_VIDEO_ID);
            mOpenEyeService=new OpenEyeService();
            if (savedInstanceState!=null)
            {
                nextPageUrl=savedInstanceState.getString(KEY_NEXT_PAGE_URL);
            }else {
                mOpenEyeService.getComments(id,this);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_NEXT_PAGE_URL,nextPageUrl);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSuccess(OpenEyeComment data) {
       nextPageUrl=data.getNextPageUrl();
       addComments(new OpenEyeCommentFactory().getCommentList(data));
       notifySuccess();
    }

    @Override
    public void onFailure(String message) {
        notifyError();
    }

    @Override
    public void onLoadMore() {
        if (nextPageUrl!=null)
        {
            mOpenEyeService.nextCommentList(nextPageUrl,this);
        }else {
            notifyRequestEnd();
        }
    }

    @Override
    public void onDestroy() {
        mOpenEyeService.cancelAll();
        super.onDestroy();
    }

    public static class OpenEyeCommentFactory{

        public List<Comment> getCommentList(OpenEyeComment data)
        {
            if (data==null)
            {
                return null;
            }
            List<Comment> list=new ArrayList<>();
            for (OpenEyeComment.ReplyListBean item:data.getReplyList())
            {
                String content=item.getMessage();
                String time= TimeUtil.formatYMD(item.getCreateTime());
                int likeCount=item.getLikeCount();

                OpenEyeComment.ReplyListBean.UserBean user=item.getUser();
                String author=user.getNickname();
                String userPic=user.getAvatar();
                list.add(new Comment(author,likeCount,time,userPic,null,content));
            }
            return list;
        }
    }
}
