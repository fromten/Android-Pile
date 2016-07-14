package learn.example.pile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import learn.example.pile.adapters.CommentListAdapter;
import learn.example.pile.jsonbean.ZhihuComment;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuContentService;
import learn.example.uidesign.CommonRecyclerView;

/**
 * Created on 2016/7/13.
 */
public class CommentFragment extends BaseListFragment implements IService.Callback<ZhihuComment> {

    private CommentListAdapter mCommentListAdapter;
    private ZhihuContentService mService;
    public static final String KEY_ID="id";

    private boolean isShortShow=false;
    private int id;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCommentListAdapter=new CommentListAdapter();
        setAdapter(mCommentListAdapter);
        setLayoutManager(new LinearLayoutManager(getContext()));
        mService=new ZhihuContentService();

        Bundle bundle=getArguments();
        if (bundle!=null)
        {
             id=bundle.getInt(KEY_ID);
            mService.getLongComment(id,this);
        }
//        id=4232852;
//        mService.getLongComment(id,this);


    }

    @Override
    public void onDestroy() {
        mService.cancelAll();
        super.onDestroy();
    }

    @Override
    public void refresh(CommonRecyclerView recyclerView) {
        hideRefreshProgressbar();
    }

    @Override
    public void loadMore(CommonRecyclerView recyclerView) {
        if (!isShortShow)
        {
            mService.getShortComment(id,this);
            isShortShow=true;
        }
    }

    @Override
    public void onSuccess(ZhihuComment data) {
         if (data==null)
             return;

        if (data.getComments().isEmpty())
        {
            setEmptyViewText("暂时没有评论");
            return;
        }
         mCommentListAdapter.addAll(data.getComments());
    }

    @Override
    public void onFailure(String message) {

    }
}
