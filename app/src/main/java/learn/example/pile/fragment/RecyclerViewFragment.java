package learn.example.pile.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import learn.example.joke.R;
import learn.example.pile.adapters.FooterAdapter;
import learn.example.pile.ui.RecyclerPullUPImpl;

/**
 * Created on 2016/5/22.
 */
public abstract class  RecyclerViewFragment extends Fragment implements RecyclerPullUPImpl.PullUpRefreshListener,SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private SwipeRefreshLayout mPullDownRefresh;
    private RecyclerPullUPImpl mPullUpRefresh;

    private Runnable startRefreshRunnable =new Runnable() {
        @Override
        public void run() {
            mPullDownRefresh.setRefreshing(true);
        }
    };
    private Runnable stopRefreshRunnable =new Runnable() {
        @Override
        public void run() {
            mPullDownRefresh.setRefreshing(false);
        }
    };

    private View.OnClickListener footerClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((TextView)v).setText("重新加载中...");
            mPullUpRefresh.setInRefreshing(false);
            mPullUpRefresh.onScrollStateChanged(mRecyclerView, RecyclerView.SCROLL_STATE_IDLE);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_root,container,false);
        mPullDownRefresh= (SwipeRefreshLayout) v.findViewById(R.id.swiper_refresh_view);
        mRecyclerView= (RecyclerView) v.findViewById(R.id.recycler_view);
        mEmptyView= (TextView) v.findViewById(R.id.empty_view);
        initView();
        return v;
    }
    public void  initView()
    {
        mPullUpRefresh=new RecyclerPullUPImpl(this);
        mRecyclerView.setLayoutManager(getLayoutManager());//设置布局
        mRecyclerView.addOnScrollListener(mPullUpRefresh);//添加上拉刷新监听
        mRecyclerView.addItemDecoration(getDividerItemDecoration());//设置装饰器
        mPullDownRefresh.setOnRefreshListener(this);//设置下拉监听
    }


    /**
     *
     * @param adapter 设置RecyclerViewAdapter
     */
    public final void setRecyclerAdapter(RecyclerView.Adapter adapter)
    {
        mRecyclerView.setAdapter(adapter);
    }


    //RecyclerView分割线装饰器,默认使用 android.R.attr.listDivider
    public DividerItemDecoration getDividerItemDecoration()
    {
        return new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST);
    }


    //RecyclerView布局,默认是垂直线性布局
    public RecyclerView.LayoutManager getLayoutManager()
    {
        return new LinearLayoutManager(getContext());
    }


    public final RecyclerView getRecyclerView()
    {
        return mRecyclerView;
    }


    /**
     *
     * @param charSequence:设置空文本显示,可以设置Null显示文本
     */
    public void setEmptyViewText(CharSequence charSequence)
    {
        mEmptyView.setText(charSequence);
    }



    public final SwipeRefreshLayout getPullDownRefreshView() {
        return mPullDownRefresh;
    }

    //刷新完成
    public void refreshComplete(){
         stopPullDownRefreshing();
         mPullUpRefresh.setInRefreshing(false);
    }

    //刷新失败
    public void refreshFail()
    {
        stopPullDownRefreshing();
        int count=mRecyclerView.getAdapter().getItemCount();
        FooterAdapter.FooterViewHolder holder= (FooterAdapter.FooterViewHolder) mRecyclerView.findViewHolderForAdapterPosition(count-1);
        final TextView footerView= (TextView) holder.itemView;
        footerView.setText("加载失败,点击重新加载");
        footerView.setOnClickListener(footerClick);
    }
    //开始刷新
    public void startRefresh(){
        startPullDownRefreshing();
        mPullUpRefresh.setInRefreshing(true);
    }
    private void startPullDownRefreshing()
    {
        mPullDownRefresh.post(startRefreshRunnable);
    }
    private void stopPullDownRefreshing()
    {
        mPullDownRefresh.postDelayed(stopRefreshRunnable,800);
    }

    @Override
    public final void pullUpRefresh(RecyclerView recyclerView) {
          //上拉添加
        pullUpRefresh();
    }

    @Override
    public final void onRefresh() {
         //下拉刷新
        pullDownRefresh();
    }
    public abstract void pullUpRefresh();

    public abstract void pullDownRefresh();

    @Override
    public void onDestroy() {
        mRecyclerView=null;
        mEmptyView=null;
        if(mPullDownRefresh.isRefreshing())
            mPullDownRefresh.setRefreshing(false);
        mPullDownRefresh=null;
        startRefreshRunnable =null;
        stopRefreshRunnable =null;
        footerClick=null;
        super.onDestroy();
    }



    //检查网络是否可用
    public boolean checkNetState()
    {
        Context context=getActivity();
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        return info!=null&&info.isConnected();
    }

    public static class  DividerItemDecoration extends RecyclerView.ItemDecoration {


            public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

            public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

            private Drawable mDivider;

            private int mOrientation;

            public DividerItemDecoration(Context context, int orientation) {
                final TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
                mDivider = a.getDrawable(0);
                a.recycle();
                setOrientation(orientation);
            }

            public DividerItemDecoration(Context context, int orientation,Drawable drawable) {
                    mDivider =drawable;
                    setOrientation(orientation);
           }

            public void setOrientation(int orientation) {
                if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
                    throw new IllegalArgumentException("invalid orientation");
                }
                mOrientation = orientation;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent,RecyclerView.State state) {
                super.onDraw(c,parent,state);
                if (mOrientation == VERTICAL_LIST) {
                    drawVertical(c, parent);
                } else {
                    drawHorizontal(c, parent);
                }

            }


            public void drawVertical(Canvas c, RecyclerView parent) {
                final int left = parent.getPaddingLeft();
                final int right = parent.getWidth() - parent.getPaddingRight();
                final int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                            .getLayoutParams();
                    final int top = child.getBottom() + params.bottomMargin;
                    final int bottom = top + mDivider.getIntrinsicHeight();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }

            public void drawHorizontal(Canvas c, RecyclerView parent) {
                final int top = parent.getPaddingTop();
                final int bottom = parent.getHeight() - parent.getPaddingBottom();

                final int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                            .getLayoutParams();
                    final int left = child.getRight() + params.rightMargin;
                    final int right = left + mDivider.getIntrinsicHeight();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }
    }
}
