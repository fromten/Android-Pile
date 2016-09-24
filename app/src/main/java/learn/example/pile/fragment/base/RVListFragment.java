package learn.example.pile.fragment.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import learn.example.pile.R;
import learn.example.pile.ui.RecyclerViewImprove;


/**
 * Created on 2016/7/16.
 */
public class RVListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    private FrameLayout mRootLayout;
    private RecyclerViewImprove mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ScrollHelper mScrollHelper;

    private View mEmptyView;

    private RecyclerView.Adapter innerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_rvlist,container,false);
        mRootLayout= (FrameLayout) view;
        mRecyclerView= (RecyclerViewImprove) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        return view;
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mScrollHelper=new ScrollHelper();
        mRecyclerView.addOnScrollListener(mScrollHelper);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!=null)
        mAdapterDataObserver.onChanged();
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager)
    {
       mRecyclerView.setLayoutManager(manager);
    }


    /**
     * 使用Wrap模式添加Adapter,
     * @see RecyclerView#setAdapter(RecyclerView.Adapter)
     */
    public void setAdapter(RecyclerView.Adapter adapter)
    {

        if (innerAdapter!=null)
        {
            innerAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
        }

        if (adapter==null)
        {
            mRecyclerView.setAdapter(null);
            innerAdapter=null;
        }else {
            innerAdapter=adapter;
            adapter.registerAdapterDataObserver(mAdapterDataObserver);
            mRecyclerView.setAdapter(adapter);
        }
    }


    public void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        mRecyclerView.addItemDecoration(decoration);
    }


    public void setRefreshSchemeColors(int... color) {
       mSwipeRefreshLayout.setColorSchemeColors(color);
    }

    //是否禁止下拉刷新
    public void setDisEnableRefresh(boolean whether)
    {
        mSwipeRefreshLayout.setEnabled(!whether);
    }

    @Override
    public void onRefresh() {
       //TODO
    }


    public void onLoadMore() {
        //TODO
    }

    //添加底部Holder
    public void addFooterHolder(RecyclerViewImprove.FooterHolder holder) {
        mRecyclerView.getAdapter().setFooterHolder(holder);
    }

    //添加头部Holder
    public void addHeadHolder(RecyclerViewImprove.HeadHolder holder) {
        mRecyclerView.getAdapter().setHeadHolder(holder);
    }


    /**
     * 在布局中心添加空View
     * @param view 如果是Null,将移除View
     */
    public void setEmptyView(View view) {
        if (mEmptyView!=null)
        {
            mRootLayout.removeView(view);
        }
        if (view!=null)
        {
            FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity= Gravity.CENTER;
            view.setLayoutParams(params);
            mRootLayout.addView(view);
        }
        mEmptyView=view;
    }

    //设置是否刷新,true 刷新,否则 false
    public void setRefreshing(final boolean refreshing)
    {
        if (mSwipeRefreshLayout.isRefreshing()==refreshing)
        {
            return;
        }
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(refreshing);
            }
        });
    }

    public boolean isRefreshing()
    {
        return mSwipeRefreshLayout.isRefreshing();
    }

    //取消加载更多,如果没有调用,后续的onLoadMore方法将不会调用
    public void cancelLoadMore()
    {
        if (mScrollHelper!=null)
        mScrollHelper.clearState();
    }



    /**
     * 禁用加载更多,使用后 onLoadMore 将不会调用
     */
    public void disableLoadMore()
    {
        mRecyclerView.removeOnScrollListener(mScrollHelper);
        mScrollHelper=null;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void onDestroy() {
        if (mSwipeRefreshLayout.isRefreshing())
        {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (innerAdapter!=null)
        {
            innerAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
            innerAdapter=null;
        }
        if (mScrollHelper!=null)
        {
            mRecyclerView.removeOnScrollListener(mScrollHelper);
            mScrollHelper=null;
        }

        super.onDestroy();
    }


    private RecyclerView.AdapterDataObserver mAdapterDataObserver
            =new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            mRecyclerView.getAdapter().notifyDataSetChanged();
            checkItemCount();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mRecyclerView.getAdapter().notifyItemRangeChanged(positionStart, itemCount);
            checkItemCount();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mRecyclerView.getAdapter().notifyItemRangeChanged(positionStart, itemCount, payload);
            checkItemCount();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mRecyclerView.getAdapter().notifyItemRangeInserted(positionStart, itemCount);
            checkItemCount();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mRecyclerView.getAdapter().notifyItemRangeRemoved(positionStart, itemCount);
            checkItemCount();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mRecyclerView.getAdapter().notifyItemRangeRemoved(fromPosition, itemCount);
            checkItemCount();
        }

        public void checkItemCount() {
            //如果在刷新,不显示空view

//            int invalidChildCount=innerAdapter.getItemCount();
//
//            if (mSwipeRefreshLayout.isRefreshing())
//            {
//                return;
//            }
            if (mEmptyView != null&&innerAdapter!=null) {
                int visibility = innerAdapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE;
                mEmptyView.setVisibility(visibility);
            }
        }
    };

    private class ScrollHelper extends RecyclerView.OnScrollListener{
        private boolean inLoading=false;
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
             if (inLoading||mSwipeRefreshLayout.isRefreshing()||newState== RecyclerView.SCROLL_STATE_DRAGGING)
             {
                 return;
             }
              int count=recyclerView.getAdapter().getItemCount();
              boolean lastVisible=false;
              if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager)
              {
                  int []arry=((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPositions(null);
                  for (int i:arry) {
                      if (i==count-1)
                      {
                          lastVisible=true;
                      }
                  }
              }else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager)
              {
                   int lastPost=((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                  if (lastPost==count-1){
                      lastVisible=true;
                  }
              }
               if (lastVisible)
              {
                 inLoading=true;
                 RVListFragment.this.onLoadMore();
              }
        }

        public void clearState()
        {
            inLoading=false;
        }
    }



}
