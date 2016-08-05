package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.adapters.CommentListAdapter;
import learn.example.pile.jsonbean.OpenEyeComment;
import learn.example.pile.net.IService;
import learn.example.pile.net.OpenEyeService;
import learn.example.pile.object.Comment;
import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/8/5.
 */
public class OpenEyeCommentFragment  extends CommentFragment implements IService.Callback<OpenEyeComment>{

    private OpenEyeService mOpenEyeService;
    private int id;
    private String nextPageUrl;
    public static OpenEyeCommentFragment newInstance(int id) {
        OpenEyeCommentFragment fragment = new OpenEyeCommentFragment();
        fragment.id=id;
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOpenEyeService=new OpenEyeService();
        mOpenEyeService.getComments(id,this);
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
        mOpenEyeService.nextCommentList(nextPageUrl,this);
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
