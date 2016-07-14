package learn.example.uidesign;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * Created on 2016/6/28.
 */
public class CommonRecyclerView extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener   {
    private static final String TAG = "CommonRecyclerView";

    private RecyclerView.OnScrollListener mScrollListener;
    private AdapterDataObserver mAdapterDataObserver;
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean loadMoreInRuning=false;
    private ActionHandle mActionHandle;
    public CommonRecyclerView(Context context) {
        super(context);
        initViews();
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public void initViews()
    {
        Context context=getContext();
        LayoutInflater.from(context).inflate(R.layout.recyclerview_common,this);
        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mRecyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mScrollListener=new ScrollListenerImpl();
        mRecyclerView.addOnScrollListener(mScrollListener);

    }

    public void setSwipeRefreshColorRes(int... colorRes)
    {
        mSwipeRefreshLayout.setColorSchemeResources(colorRes);
    }

    public void setSwipeRefreshColor(int... color)
    {
        mSwipeRefreshLayout.setColorSchemeColors(color);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration)
    {
        mRecyclerView.addItemDecoration(itemDecoration);
    }


    public View getEmptyView()
    {
        return mEmptyView;
    }

    public void setEmptyView(View view,LayoutParams params)
    {
        if (mEmptyView!=null)
        {
            removeView(mEmptyView);
        }
        mEmptyView=view;
        addView(mEmptyView,params);
    }

    public void setAdapter(FooterViewAdapter adapter)
    {
        mRecyclerView.setAdapter(adapter);
        mAdapterDataObserver=new AdapterDataObserver();
        adapter.registerAdapterDataObserver(mAdapterDataObserver);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager)
    {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public View getFooterView()
    {
        int count=mRecyclerView.getAdapter().getItemCount();
        for (int i = count-1; i>=0; i--) {
            RecyclerView.ViewHolder holder=mRecyclerView.findViewHolderForAdapterPosition(i);
            if (holder!=null)
            {
                if (FooterViewAdapter.TYPE_FOOTER_VIEW== holder.getItemViewType())
                {
                    return holder.itemView;
                }
            }
        }
        return null;
    }

    public void cancelLoadMore()
    {
        loadMoreInRuning=false;
    }

    public boolean isRefreshing()
    {
        return mSwipeRefreshLayout.isRefreshing();
    }

    @Override
    public void onRefresh() {
        if (mActionHandle!=null)
        {
            mActionHandle.refresh(this);
        }
    }

    /**
     * 设置拉下刷新是否显示
     * @param refreshing true 显示,false 隐藏
     */
    public void setSwipeRefreshing(final boolean refreshing)
    {

        post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(refreshing);
            }
        });
    }

    public void setActionHandler(ActionHandle handler)
    {
        mActionHandle=handler;
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
        setSwipeRefreshing(false);
        mScrollListener=null;
        mAdapterDataObserver=null;
        mActionHandle=null;
        mRecyclerView=null;
    }

    public class ScrollListenerImpl extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
             boolean unInvalidState=newState==RecyclerView.SCROLL_STATE_DRAGGING;
             int count=mRecyclerView.getAdapter().getItemCount();
             if (unInvalidState||count==1||mSwipeRefreshLayout.isRefreshing()||loadMoreInRuning)
                 return;
             LinearLayoutManager manager= (LinearLayoutManager) recyclerView.getLayoutManager();
             int lastPosition= manager.findLastVisibleItemPosition();
             if (lastPosition==count-1)
             {
                 if (mActionHandle!=null)
                 {
                     Log.i(TAG, "load more listener invoke");
                     loadMoreInRuning=true;
                     mActionHandle.loadMore(CommonRecyclerView.this);
                 }
             }
        }
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        }
    }


    public class AdapterDataObserver extends RecyclerView.AdapterDataObserver{
        @Override
        public void onChanged() {
            loadMoreInRuning=false;
            if (mActionHandle!=null)
            {
                mActionHandle.adapterDataChanged(CommonRecyclerView.this,mRecyclerView.getAdapter().getItemCount());
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
           onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onChanged();
        }
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            onChanged();
        }
        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            onChanged();
        }
        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            onChanged();
        }
    }

    //有底部加载视图的Adapter
    public static abstract class FooterViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter{

        public static final int TYPE_FOOTER_VIEW=88;

        @Override
        public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (TYPE_FOOTER_VIEW==viewType)
            {
                RecyclerView.ViewHolder footerViewHolder=new RecyclerView.ViewHolder(onCreateFooterView(parent)){};
                footerViewHolder.setIsRecyclable(false);
                return footerViewHolder;
            }else {
                return getItemViewHolder(parent,viewType);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
              if (position!=getItemSize())
              {
                  updaterItemView((VH) holder,position);
              }else {
                  updateFooterView(holder,position);
              }
        }

        @Override
        public final  int getItemCount() {
            return getItemSize()+1;
        }

        @Override
        public final int getItemViewType(int position) {
            if (position==getItemSize())
                return TYPE_FOOTER_VIEW;
            if (getViewType(position)==TYPE_FOOTER_VIEW)
            {
                throw new RuntimeException("Adapter no can use that view type because by FooterViewAdapter.TYPE_FOOTER_VIEW used");
            }
            return getViewType(position);
        }

        public int getViewType(int position)
        {
            return 0;
        }

        protected View onCreateFooterView(ViewGroup parent)
        {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.footerview,parent,false);
        }

        public void updateFooterView(RecyclerView.ViewHolder holder,int position){
            if (getItemSize()<=0)
            {
                holder.itemView.setVisibility(View.INVISIBLE);
            }else {
                View view= holder.itemView.findViewById(R.id.footer_text);
                if (view!=null)
                {
                    TextView textView= (TextView) view;
                    textView.setText(null);
                }
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }



        public abstract int getItemSize();
        public abstract void updaterItemView(VH  holder, int position);
        public abstract VH getItemViewHolder(ViewGroup parent,int type);

    }


    /**
     * 监听,刷新,加载,数据改变的接口
     */
    public interface ActionHandle{
        void refresh(CommonRecyclerView recyclerView);
        void loadMore(CommonRecyclerView recyclerView);
        //数据改变时调用,itemCount 现在存在的元素个数
        void adapterDataChanged(CommonRecyclerView recyclerView, int itemCount);
    }



}
