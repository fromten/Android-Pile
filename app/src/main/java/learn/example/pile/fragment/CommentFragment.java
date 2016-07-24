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
import learn.example.pile.jsonbean.NetEaseComment;
import learn.example.pile.jsonbean.ZhihuComment;
import learn.example.pile.net.IService;
import learn.example.pile.net.NewsService;
import learn.example.pile.net.ZhihuContentService;
import learn.example.pile.object.Comment;
import learn.example.uidesign.DividerItemDecoration;

/**
 * Created on 2016/7/13.
 */
public class CommentFragment extends RVListFragment {

    private CommentListAdapter mCommentListAdapter;

    public static final String KEY_ZHIHU_ID="zhihuid";
    public static final String KEY_NETEASE_ID="neteaseid";
    private TextView mEmptyTextView;


    private NetEaseCommentHandler mNetEaseHandler;
    private ZhihuCommentHandler mZhihuHandler;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLayoutManager(new LinearLayoutManager(getContext()));
        addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        setEnableSwipeLayout(false);
        mEmptyTextView= (TextView) generateEmptyView();
        setEmptyView(mEmptyTextView);
        mCommentListAdapter=new CommentListAdapter();
        setAdapter(mCommentListAdapter);
        initComment();
    }

    private void initComment()
    {
        Bundle bundle=getArguments();
        if (bundle!=null)
        {
            if (bundle.containsKey(KEY_ZHIHU_ID))
            {
                int id=bundle.getInt(KEY_ZHIHU_ID);
                mZhihuHandler=new ZhihuCommentHandler(id);
                mZhihuHandler.perfromRequest();
            }else if (bundle.containsKey(KEY_NETEASE_ID))
            {
                String[] array=bundle.getStringArray(KEY_NETEASE_ID);
                String netEaseBoradId=array[0];
                String netEaseId=array[1];
                mNetEaseHandler=new NetEaseCommentHandler(netEaseBoradId,netEaseId);
                mNetEaseHandler.perfromHotRequest();
            }
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
        if (mNetEaseHandler!=null)
        {
            mNetEaseHandler.cancelAll();
        }
        if (mZhihuHandler!=null)
        {
            mZhihuHandler.cancelAll();
        }
        super.onDestroy();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        if (mNetEaseHandler!=null)
        {
            mNetEaseHandler.next();
        }
    }


    private class NetEaseCommentHandler{
        private String boradId;
        private String docId;
        private NewsService mNewsService;
        private int start;
        private static final int MAX_LENGTH=15;

        public NetEaseCommentHandler(String boradId, String docId) {
            this.boradId = boradId;
            this.docId = docId;
            mNewsService=new NewsService();
        }

        public void perfromHotRequest()
        {
            mNewsService.getHotComment(boradId, docId, start, MAX_LENGTH, new IService.Callback<NetEaseComment>() {
                @Override
                public void onSuccess(NetEaseComment data) {
                    mCommentListAdapter.addAll(Comment.toList(data));
                    start+=MAX_LENGTH;
                }

                @Override
                public void onFailure(String message) {

                }
            });
        }

        public void next()
        {
            mNewsService.getNormalComment(boradId, docId, start, MAX_LENGTH, new IService.Callback<NetEaseComment>() {
                @Override
                public void onSuccess(NetEaseComment data) {
                    mCommentListAdapter.addAll(Comment.toList(data));
                    start+=MAX_LENGTH;
                    cancelLoadMore();
                }

                @Override
                public void onFailure(String message) {
                   cancelLoadMore();
                }
            });
        }
        public void cancelAll()
        {
            mNewsService.cancelAll();
        }
    }


    private class ZhihuCommentHandler{

        private boolean isFinishRequest;
        private ZhihuContentService mService;
        private int id;
        public ZhihuCommentHandler(int id) {
            mService=new ZhihuContentService();
            this.id=id;
        }

        public void perfromRequest()
        {
            isFinishRequest=false;
            mService.getLongComment(id, new IService.Callback<ZhihuComment>() {
                @Override
                public void onSuccess(ZhihuComment data) {
                    mCommentListAdapter.addAll(Comment.toList(data));
                    if (!isFinishRequest)
                    {
                        mService.getShortComment(id,this);
                        isFinishRequest=true;
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
            });
        }
        public void cancelAll()
        {
            mService.cancelAll();
        }

    }

}
