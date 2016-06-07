package learn.example.pile.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import learn.example.pile.adapters.FooterAdapter;

/**
 * Created on 2016/5/23.
 */
public class RecyclerPullUPImpl extends RecyclerView.OnScrollListener {

    private boolean inRefreshing;
    public interface PullUpRefreshListener{
         void pullUpRefresh(RecyclerView recyclerView);
    }
    private PullUpRefreshListener mPullUpListener;
    public RecyclerPullUPImpl(PullUpRefreshListener pullUpListener) {
        this.mPullUpListener = pullUpListener;
    }
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int itemcount = manager.getItemCount();
            int lastpos = manager.findLastVisibleItemPosition();
            //itemcount等于全部个数减去底部刷新视图
            if (!inRefreshing&&itemcount>1&&itemcount-1== lastpos)
            {
                inRefreshing=true;
                mPullUpListener.pullUpRefresh(recyclerView);
            }
        }
    }
    public void setInRefreshing(boolean refreshing)
    {
        inRefreshing=refreshing;
    }

}
