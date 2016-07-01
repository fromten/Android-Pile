package learn.example.pile.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created on 2016/5/23.
 */
public class RecyclerPullUPImpl extends RecyclerView.OnScrollListener {
    /**
     * 上拉加载更多实现
     */
    private boolean inLoading =false;
    private RecyclerView mRecyclerView;


    private View.OnClickListener footerClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            inLoading=false;
            onScrollStateChanged(mRecyclerView,RecyclerView.SCROLL_STATE_IDLE);
        }
    };


    public interface PullUpRefreshListener{
         void pullUpRefresh(RecyclerView recyclerView);
    }
    private PullUpRefreshListener mPullUpListener;
    public RecyclerPullUPImpl(@NonNull RecyclerView recyclerView, @NonNull PullUpRefreshListener pullUpListener) {
        this.mRecyclerView=recyclerView;
        this.mPullUpListener = pullUpListener;
    }
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int itemcount = manager.getItemCount();
            int lastpos = manager.findLastVisibleItemPosition();
            //itemcount等于全部个数减去底部刷新视图
            if (!inLoading &&itemcount>1&&itemcount-1== lastpos)
            {
                resetFooterView();
                inLoading =true;
                mPullUpListener.pullUpRefresh(recyclerView);
            }
        }
    }
    public void loadFail()
    {
        int count=mRecyclerView.getAdapter().getItemCount();
        RecyclerView.ViewHolder holder=mRecyclerView.findViewHolderForAdapterPosition(count-1);
        if (holder instanceof FooterAdapter.FooterViewHolder)
        {
            holder.itemView.setVisibility(View.VISIBLE);
            ((FooterAdapter.FooterViewHolder) holder).mProgressBar.setVisibility(View.INVISIBLE);
            ((FooterAdapter.FooterViewHolder) holder).mTextView.setText("加载失败,点击重新加载");
            holder.itemView.setOnClickListener(footerClick);
        }else {
            throw new RuntimeException("RecyclerAdapter must extend FooterAdapter");
        }
    }
    public void resetFooterView()
    {
        int count=mRecyclerView.getAdapter().getItemCount();
        RecyclerView.ViewHolder holder=mRecyclerView.findViewHolderForAdapterPosition(count-1);
        if (holder instanceof FooterAdapter.FooterViewHolder)
        {
            holder.itemView.setVisibility(View.VISIBLE);
            ((FooterAdapter.FooterViewHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
            ((FooterAdapter.FooterViewHolder) holder).mTextView.setText(null);
            holder.itemView.setOnClickListener(null);
        }else {
            throw new RuntimeException("RecyclerAdapter must extend FooterAdapter");
        }
    }
    public void loadSuccess()
    {
        inLoading=false;
    }

    public void setInLoading()
    {
        inLoading=true;
    }

}
