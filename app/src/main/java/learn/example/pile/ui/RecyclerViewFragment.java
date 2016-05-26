package learn.example.pile.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import learn.example.joke.R;

/**
 * Created on 2016/5/22.
 */
public class RecyclerViewFragment extends Fragment implements RecyclerPullUPImpl.PullUpRefreshListener,SwipeRefreshLayout.OnRefreshListener {


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
    public void setRefreshing(boolean refreshing){
        mRefreshView.setRefreshing(refreshing);
    }


    @Override
    public void pullUpRefresh(RecyclerView recyclerView) {
          //上拉添加
    }

    @Override
    public void onRefresh() {
         //下拉刷新
    }

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
}
