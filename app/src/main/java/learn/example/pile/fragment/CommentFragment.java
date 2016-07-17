package learn.example.pile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import learn.example.pile.adapters.CommentListAdapter;
import learn.example.pile.jsonbean.ZhihuComment;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuContentService;

/**
 * Created on 2016/7/13.
 */
public class CommentFragment extends RVListFragment implements IService.Callback<ZhihuComment> {

    private CommentListAdapter mCommentListAdapter;
    private ZhihuContentService mService;

    public static final String KEY_ID="id";
    private TextView mEmptyTextView;
    private int id;

    private boolean finishRequest;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLayoutManager(new LinearLayoutManager(getContext()));
        setEnableSwipeLayout(false);
        mEmptyTextView= (TextView) generateEmptyView();
        setEmptyView(mEmptyTextView);

        mCommentListAdapter=new CommentListAdapter();
        setAdapter(mCommentListAdapter);


        mService=new ZhihuContentService();
        Bundle bundle=getArguments();
        if (bundle!=null)
        {
            id=bundle.getInt(KEY_ID);
            mService.getLongComment(id,this);
            finishRequest=false;
        }
    }

    private View generateEmptyView()
    {
        TextView view =new TextView(getContext());
        view.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity=Gravity.CENTER;
        view.setLayoutParams(params);
        view.setClickable(false);
        view.setFocusable(false);
        return view;
    }

    @Override
    public void onDestroy() {
        mService.cancelAll();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
       //do nothing
    }

    @Override
    public void onLoadMore() {
        //do nothing
    }

    @Override
    public void onSuccess(ZhihuComment data) {
        mCommentListAdapter.addAll(data.getComments());

        if (!finishRequest)
        {
            mService.getShortComment(id,this);
            finishRequest=true;
        }else {
            if (mCommentListAdapter.getItemCount()==0)
            {
                mEmptyTextView.setText("暂时没有评论");
            }
        }

    }

    @Override
    public void onFailure(String message) {

    }
}
