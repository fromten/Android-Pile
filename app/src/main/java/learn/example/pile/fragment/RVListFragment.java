package learn.example.pile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import learn.example.pile.R;


/**
 * Created on 2016/7/16.
 */
public class RVListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    private FrameLayout mRootLayout;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ScrollHelper mScrollHelper=new ScrollHelper();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_rvlist,container,false);
        mRootLayout= (FrameLayout) view;
        mRecyclerView= (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(mScrollHelper);
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager)
    {
       mRecyclerView.setLayoutManager(manager);
    }

    public void setAdapter(RecyclerView.Adapter adapter)
    {
       mRecyclerView.setAdapter(new AdapterWrapper(adapter));
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


    public void addFooterHolder(FooterHolder holder) {
        RecyclerView.Adapter adapter=mRecyclerView.getAdapter();
        ((AdapterWrapper)adapter).setFooterHolder(holder);
    }


    public void setEmptyView(View view) {
        if (view==null)
        {
            view.getClass();
        }
        mRootLayout.addView(view);
    }

    public void setRefreshing(final boolean refreshing)
    {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(refreshing);
            }
        });
    }

    public void cancelLoadMore()
    {
        mScrollHelper.clearState();
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
        super.onDestroy();
    }

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



    private static class AdapterWrapper extends RecyclerView.Adapter{

        private final int TYPE_FOOTER=66;

        private RecyclerView.Adapter mInnerAdapter;
        private FooterHolder mFooterHolder;

        public AdapterWrapper(RecyclerView.Adapter adapter) {
            mInnerAdapter = adapter;
            mInnerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    AdapterWrapper.this.notifyDataSetChanged();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    AdapterWrapper.this.notifyItemRangeChanged(positionStart,itemCount);
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                    AdapterWrapper.this.notifyItemRangeChanged(positionStart,itemCount,payload);
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    AdapterWrapper.this.notifyItemRangeInserted(positionStart,itemCount);
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    AdapterWrapper.this.notifyItemRangeRemoved(positionStart,itemCount);
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    AdapterWrapper.this.notifyItemMoved(fromPosition,toPosition);
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType==TYPE_FOOTER&&mFooterHolder!=null)
            {
                return mFooterHolder;
            }
            return mInnerAdapter.onCreateViewHolder(parent,viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position)==TYPE_FOOTER)
            {
                mFooterHolder.onBindHolder(mInnerAdapter);
            }else {
                mInnerAdapter.onBindViewHolder(holder,position);
            }
        }

        @Override
        public int getItemCount() {
            int num=mFooterHolder==null?0:1;
            return mInnerAdapter.getItemCount()+num;
        }

        @Override
        public int getItemViewType(int position) {
            if (position==mInnerAdapter.getItemCount())
            {
                return TYPE_FOOTER;
            }
            return mInnerAdapter.getItemViewType(position);
        }


        public void setFooterHolder(FooterHolder holder)
        {
            mFooterHolder=holder;
            notifyDataSetChanged();
        }

    }

    public static abstract class FooterHolder extends RecyclerView.ViewHolder{

        public FooterHolder(View itemView) {
            super(itemView);
        }

        //当footer显示时调用
        public abstract void onBindHolder(RecyclerView.Adapter adapter);
    }


}
