package learn.example.pile.ui;

import android.content.Context;
import android.content.res.Configuration;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import learn.example.joke.R;

/**
 * Created on 2016/5/22.
 */
public abstract class  RecyclerViewFragment extends Fragment implements RecyclerPullUPImpl.PullUpRefreshListener,SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private SwipeRefreshLayout mRefreshView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_root,container,false);
        mRefreshView= (SwipeRefreshLayout) v.findViewById(R.id.swiper_refresh_view);
        mRecyclerView= (RecyclerView) v.findViewById(R.id.recycler_view);
        mEmptyView= (TextView) v.findViewById(R.id.empty_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addOnScrollListener(new RecyclerPullUPImpl(this));//添加上拉刷新监听
        mRefreshView.setOnRefreshListener(this);//设置下拉监听
        mRecyclerView.setOnTouchListener(new OnItemClickImpl());//View点击监听
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        return v;
    }
    public final void setRecyclerAdapter(RecyclerView.Adapter adapter)
    {
        mRecyclerView.setAdapter(adapter);
    }
    public final RecyclerView getRecyclerView()
    {
        return mRecyclerView;
    }


    public void setEmptyViewText(CharSequence charSequence)
    {
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.setText(charSequence);
    }


    public final SwipeRefreshLayout getRefreshView() {
        return mRefreshView;
    }


    //设置是否刷新
    public void setRefreshing(final boolean refreshing){
        //解决无法显示刷新进度栏的问题
        mRefreshView.post(new Runnable() {
            @Override
            public void run() {
                if(mRefreshView!=null)
                mRefreshView.setRefreshing(refreshing);
            }
        });
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
    //元素点击
    public void onItemClick(RecyclerView recyclerView,View view,int position)
    {

    }
    @Override
    public void onDestroy() {
        mRecyclerView=null;
        mEmptyView=null;
        if(mRefreshView.isRefreshing())
            mRefreshView.setRefreshing(false);
        mRefreshView=null;
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
    public  class OnItemClickImpl implements View.OnTouchListener{
        private float mDownX;
        private float mDownY;
        private boolean isPress;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action=event.getAction();
            if(action==MotionEvent.ACTION_DOWN)
            {
                isPress=true;
                mDownX=event.getX();
                mDownY=event.getY();
            }else if(action==MotionEvent.ACTION_MOVE)
            {
                isPress=false;
            }else if(action==MotionEvent.ACTION_UP)
            {
                if(isPress)
                {
                   View view= mRecyclerView.findChildViewUnder(mDownX,mDownY);
                    int position=mRecyclerView.getChildAdapterPosition(view);
                    onItemClick(mRecyclerView,view,position);
                }
            }
            return false;
        }
    }

    public static class  DividerItemDecoration extends RecyclerView.ItemDecoration {

            private static final int[] ATTRS = new int[]{
                    android.R.attr.listDivider
            };

            public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

            public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

            private Drawable mDivider;

            private int mOrientation;

            public DividerItemDecoration(Context context, int orientation) {
                final TypedArray a = context.obtainStyledAttributes(ATTRS);
                mDivider = a.getDrawable(0);
                a.recycle();
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
