package learn.example.pile.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
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
    private LinearLayoutManager mLinearLayoutManager;

    //子类必须调用此方法
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        setEmptyViewHolder(createEmptyViewHolder());
    }

    protected void initView()
    {
        mLinearLayoutManager=new LinearLayoutManager(getContext());
        setLayoutManager(mLinearLayoutManager);
        addItemDecoration(new DividerItemDecoration(getContext()));
        setRefreshSchemeColors(ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null));
    }

    private void setEmptyViewHolder(EmptyViewHolder emptyViewHolder)
    {
        mEmptyViewHolder=emptyViewHolder;
        setEmptyView(mEmptyViewHolder.getView());
    }

    /**
     * 获得默认的EmptyViewHolder,子类可以覆盖此方法
     * @return 默认实现的EmptyViewHolder
     */
    protected EmptyViewHolder createEmptyViewHolder()
    {
        return new SimpleEmptyViewHolder();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!=null&&mAdapter instanceof SaveStateAbleAdapter)
        {
           List<? extends Parcelable> list=savedInstanceState.getParcelableArrayList(TAG);
            ((SaveStateAbleAdapter) mAdapter).addAll(list);
        }
        if (savedInstanceState!=null)//回复状态后,如果数据为0,确定上一个请求失败
        mEmptyViewHolder.onAdapterSetChanged(mAdapter.getItemCount());
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
    }

    @Override
    public  void onLoadMore(){
        //TODO
    }


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
        int count=mAdapter.getItemCount();
        if (count>0)
            mFooterHolder.mFooterText.setText("请求失败,点击重试");

        mEmptyViewHolder.onAdapterSetChanged(count);
    }


    protected void handleRequestSuccess()
    {
        if (isRemoving())
        {
            return;
        }
        mEmptyViewHolder.onAdapterSetChanged(mAdapter.getItemCount());
        setRefreshing(false);
        cancelLoadMore();
    }


    /**
     * 使用此方法表示网络请求到头,将会应用叁数去显示底部文本
     * onLoadMore 不会继续调用
     * @param footerText 底部显示文本
     */
    public void notifyRequestEnd(CharSequence footerText)
    {
        mFooterHolder.end(footerText);
        disableLoadMore();
    }


    public void notifyRequestEnd()
    {
        notifyRequestEnd("~~~到底了~~~");
    }


    private class CommonFooterHolder extends FooterHolder implements View.OnClickListener{
        public ProgressBar mFooterProgress;
        public TextView mFooterText;
        private boolean inEnd=false;

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
                hide();
            }else {

                int firstPos=mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                //第一个元素可见,隐藏Footer
                if (firstPos==0)
                {
                    hide();
                }else {
                    show();
                }
            }
            if (!inEnd)
            setFooterText(null);
        }

        @Override
        public void onClick(View v) {
            setFooterText("重新请求...");
            onLoadMore();
        }

        public void setFooterText(CharSequence charSequence)
        {
            mFooterText.setText(charSequence);
        }

        public void end(CharSequence charSequence)
        {
            inEnd=true;
            mFooterText.setText(charSequence);
            mFooterProgress.setVisibility(View.GONE);
        }

        public void show()
        {
            this.itemView.setVisibility(View.VISIBLE);
        }
        public void hide()
        {
            this.itemView.setVisibility(View.INVISIBLE);
        }
    }

    public interface EmptyViewHolder
    {
        View getView();
        void onAdapterSetChanged(int itemCount);
    }



    public class SimpleEmptyViewHolder implements EmptyViewHolder,View.OnClickListener{

        public TextView mEmptyView;

        @Override
        public View getView() {
            mEmptyView =new TextView(getContext());
            mEmptyView.setGravity(Gravity.CENTER);
            FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity=Gravity.CENTER;
            mEmptyView.setLayoutParams(params);
            mEmptyView.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_refresh,0,0);
            mEmptyView.setOnClickListener(this);
            mEmptyView.setVisibility(View.INVISIBLE);
            return mEmptyView;
        }


        @Override
        public void onAdapterSetChanged(int itemCount) {
            if (itemCount==0)
            {
                mEmptyView.setVisibility(View.VISIBLE);
                mEmptyView.setText("请求失败,点击重试");
            }else {
                mEmptyView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
              mEmptyView.setText("重新加载中...");
              onRefresh();
        }

    }
}
