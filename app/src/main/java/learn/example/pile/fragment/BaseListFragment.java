package learn.example.pile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.R;
import learn.example.pile.adapters.SaveStateAbleAdapter;
import learn.example.uidesign.DividerItemDecoration;

/**
 * Created on 2016/7/17.
 */
public class BaseListFragment extends  RVListFragment {

    private static final String TAG = "BaseListFragment";
    private CommonFooterHolder mFooterHolder;
    private EmptyViewHolder mEmptyViewHolder;
    private RecyclerView.Adapter mAdapter;


    //子类必须调用此方法
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLayout();
        addEmptyView();
    }

    protected void setLayout()
    {
        setLayoutManager(new LinearLayoutManager(getContext()));
        addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        setRefreshSchemeColors(ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null));
    }

    protected void addEmptyView()
    {
        mEmptyViewHolder=new EmptyViewHolder(getContext());
        setEmptyView(mEmptyViewHolder.mEmptyView);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState!=null&&mAdapter instanceof SaveStateAbleAdapter)
        {
           List<? extends Parcelable> list=savedInstanceState.getParcelableArrayList(TAG);
            ((SaveStateAbleAdapter) mAdapter).addAll(list);
            checkItemCount();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mAdapter instanceof SaveStateAbleAdapter)
        {
            outState.putParcelableArrayList(TAG, (ArrayList<? extends Parcelable>) ((SaveStateAbleAdapter) mAdapter).saveState());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        mAdapter=adapter;
        setFooterHolder();
    }

    protected void setFooterHolder()
    {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.footerview,getRecyclerView(),false);
        mFooterHolder=new CommonFooterHolder(view);
        addFooterHolder(mFooterHolder);
    }

    public void setAdapter(SaveStateAbleAdapter saveStateAdapter) {
         this.setAdapter((RecyclerView.Adapter) saveStateAdapter);
    }

    @Override
    public void onRefresh() {
         //TODO
    };

    @Override
    public  void onLoadMore(){
        //TODO
    };


    /**
     *  通知父类管理网络请求失败逻辑,此方法应该在Adapter改变元素集合后调用
     *  @see #notifySuccess()
     */
    public void notifyError()
    {
        handleRequestError();
    }

    /**
     *  通知父类管理网络请求成功逻辑,此方法应该在Adapter改变元素集合后调用
     *  @see #notifyError()
     */
    public void notifySuccess()
    {
        handleRequestSuccess();
    }


    protected void handleRequestError()
    {
        if (isRemoving())
        {
            return;
        }
        setRefreshing(false);
        cancelLoadMore();
        if (mAdapter.getItemCount()<=0)
        {
            mEmptyViewHolder.onStateChanged(EmptyViewHolder.STATE_NO_ITEM);
        }else {
            mEmptyViewHolder.onStateChanged(EmptyViewHolder.STATE_EXIST_ITEM);
            mFooterHolder.mFooterText.setText("请求失败,点击重试");
        }
    }


    protected void handleRequestSuccess()
    {
        if (isRemoving())
        {
            return;
        }
        mEmptyViewHolder.onStateChanged(mAdapter.getItemCount()==0?EmptyViewHolder.STATE_NO_ITEM:EmptyViewHolder.STATE_EXIST_ITEM);
        setRefreshing(false);
        cancelLoadMore();
    }

    private void checkItemCount()
    {
        mEmptyViewHolder.onStateChanged(mAdapter.getItemCount()==0?EmptyViewHolder.STATE_NO_ITEM:EmptyViewHolder.STATE_EXIST_ITEM);
    }


    private  class CommonFooterHolder extends FooterHolder implements View.OnClickListener{
        public ProgressBar mFooterProgress;
        public TextView mFooterText;
        public CommonFooterHolder(View itemView) {
            super(itemView);
            mFooterProgress= (ProgressBar) itemView.findViewById(R.id.footer_progress);
            mFooterText= (TextView) itemView.findViewById(R.id.footer_text);
            mFooterText.setOnClickListener(this);
        }
        @Override
        public void onBindHolder(RecyclerView.Adapter adapter) {
            if (adapter.getItemCount()==0)
            {
                this.itemView.setVisibility(View.INVISIBLE);
            }else {
                this.itemView.setVisibility(View.VISIBLE);
            }
            mFooterText.setText(null);
        }

        @Override
        public void onClick(View v) {
            mFooterText.setText("重新请求...");
            onLoadMore();
        }
    }

    public static class EmptyViewHolder{
        public static final int STATE_NO_ITEM=1;
        public static final int STATE_EXIST_ITEM=2;

        public TextView mEmptyView;

        public EmptyViewHolder(Context context) {
            mEmptyView= (TextView) generateEmptyView(context);
        }

        protected View generateEmptyView(Context context)
        {
            TextView view =new TextView(context);
            view.setGravity(Gravity.CENTER);
            FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity=Gravity.CENTER;
            view.setLayoutParams(params);
            view.setClickable(false);
            view.setFocusable(false);
            return view;
        }
        protected void onStateChanged(int newState)
        {
            if (newState==STATE_NO_ITEM)
            {
                mEmptyView.setText("请求失败,下拉重试");
            }else {
                mEmptyView.setText(null);
            }
        }
    }
}
