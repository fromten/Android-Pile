package learn.example.pile.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import learn.example.joke.R;
import learn.example.pile.adapters.SaveStateAbleAdapter;
import learn.example.uidesign.CommonRecyclerView;

/**
 * Created on 2016/6/29.
 */
public abstract class BaseListFragment extends Fragment implements CommonRecyclerView.ActionHandle {


     private static final String KEY_ADAPTER_SAVE_STATE = "Key_Adapter_Save_State";
     private CommonRecyclerView mCommonRecyclerView;
     private TextView mEmptyView;
     private View mLoadMoreView;
     private RecyclerView.Adapter mAdapter;
     private View.OnClickListener mFooterTextClick=new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             TextView text= (TextView) mLoadMoreView.findViewById(R.id.footer_text);
             text.setText("重新加载...");
             loadMore(mCommonRecyclerView);
         }
     };
    private Runnable mErrorRunnable=new Runnable() {
        @Override
        public void run() {
               loadError();
        }
    };
    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.fragment_base,container,false);
         mCommonRecyclerView= (CommonRecyclerView) view;
         mCommonRecyclerView.setActionHandler(this);
         return mCommonRecyclerView;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (mAdapter!=null&&savedInstanceState!=null)
        {
            if (mAdapter instanceof SaveStateAbleAdapter)
            {
                List<? extends Parcelable> list=savedInstanceState.getParcelableArrayList(KEY_ADAPTER_SAVE_STATE);
                adapterDataChanged(mCommonRecyclerView,list==null?0:list.size());
                ((SaveStateAbleAdapter) mAdapter).addAll(list);
            }
        }
        super.onViewStateRestored(savedInstanceState);
    }

    public void setAdapter(CommonRecyclerView.FooterViewAdapter adapter)
    {
         setEmptyViewText("加载中...");
         mAdapter=adapter;
         mCommonRecyclerView.setAdapter(adapter);
    }

    public void setAdapter(SaveStateAbleAdapter adapter)
    {
        setEmptyViewText("加载中...");
        mAdapter=adapter;
        mCommonRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mAdapter instanceof SaveStateAbleAdapter)
        {
            outState.putParcelableArrayList(KEY_ADAPTER_SAVE_STATE, (ArrayList<? extends Parcelable>) ((SaveStateAbleAdapter) mAdapter).saveState());
        }
        super.onSaveInstanceState(outState);
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager)
    {
        mCommonRecyclerView.setLayoutManager(manager);
    }


    public void showRefreshProgressbar(){
        mCommonRecyclerView.setSwipeRefreshing(true);
    }

    public void hideRefreshProgressbar(){
        mCommonRecyclerView.setSwipeRefreshing(false);
    }

    private void setEmptyViewText(CharSequence charSequence)
    {
        if (mEmptyView==null)
        {
            mEmptyView=new TextView(getContext());
            FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity= Gravity.CENTER;
            mCommonRecyclerView.setEmptyView(mEmptyView,params);
        }
        mEmptyView.setText(charSequence);
    }

    @Override
    public abstract void refresh(CommonRecyclerView recyclerView);

    @Override
    public abstract void loadMore(CommonRecyclerView recyclerView);



    public void notifyLoadError(){
         mCommonRecyclerView.post(mErrorRunnable);
    }

    /**
     * 通知父类处理加载错误事件,如果Adapter 继承 FooterAdapter并覆盖onCreateFooterView方法
     * 创建不同的Footer布局,子类必须覆盖此方法,否则可能抛出RuntimeException
     */
    private void loadError()
    {
        mCommonRecyclerView.cancelLoadMore();
        mLoadMoreView=mCommonRecyclerView.getFooterView();
        View view= mLoadMoreView.findViewById(R.id.footer_text);
        if (view!=null)
        {
            ((View)view.getParent()).setVisibility(View.VISIBLE);
            TextView textView= (TextView) view;
            textView.setText("加载错误点击重新加载");
            textView.setOnClickListener(mFooterTextClick);
        }else {
            throw new RuntimeException("Adapter have self footer layout," +
                    "you must override super class notifyLoadError method " +
                    "to handler self load error event");
        }
    }

    @Override
    public void onDestroy() {
        mFooterTextClick=null;
        mCommonRecyclerView.removeAllViews();
        mErrorRunnable=null;
        mAdapter=null;
        super.onDestroy();
    }

    @Override
    public void adapterDataChanged(CommonRecyclerView recyclerView, int itemCount) {
        if (itemCount<=1&&!mCommonRecyclerView.isRefreshing())//如果当前数据不大于一个,说明是空数据
        {
            setEmptyViewText("连接失败!");
        }else {
            setEmptyViewText(null);
        }
    }
}
