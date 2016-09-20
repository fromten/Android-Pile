package learn.example.pile.fragment.base;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import learn.example.pile.R;
import learn.example.pile.ui.RecyclerViewImprove;
import learn.example.pile.ui.decoration.DividerItemDecoration;

/**
 * Created on 2016/7/17.
 */
public class BaseListFragment extends  SaveAdapterStateFragment {



    private TextView mTopLogView;
    private Animator mTopAniamtor;


    private CommonFooterHolder mFooterHolder;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private SimpleEmptyViewHolder mSimpleEmptyViewHolder;
    //子类必须调用此方法
    @OverridingMethodsMustInvokeSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        setEmptyView(createEmptyView());

        View topView=createTopView();
        if (topView!=null)
        {
            ViewGroup root= (ViewGroup) view;
            root.addView(topView);
        }
    }

    protected void initView()
    {
        mLinearLayoutManager=new LinearLayoutManager(getContext());
        setLayoutManager(mLinearLayoutManager);
        addItemDecoration(new DividerItemDecoration(getContext()));
        setRefreshSchemeColors(ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null));
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        mAdapter=adapter;
        addFooterHolder(createAdapterFooterHolder());
    }


    @Override
    public void onDestroy() {
        if (mTopAniamtor!=null)
        {
            mTopAniamtor.cancel();
            mTopAniamtor.removeAllListeners();
        }
        super.onDestroy();
    }


    /**
     * 添加布局EmptyView,子类可以覆盖此方法,使用自己的EmptyView
     * 方法会在initView()中调用
     * 调用此方法后自动调用setEmptyView();
     */
    protected View createEmptyView()
    {
        mSimpleEmptyViewHolder=new SimpleEmptyViewHolder();
        return mSimpleEmptyViewHolder.getView();
    }


    /**
     * 创建适配器底部视图,子类可以覆盖此方法,创建自己的底部视图
     * 调用方法后自动调用addFooterHolder();
     * @see #setAdapter(RecyclerView.Adapter)  方法中调用此方法
     * @return FooterHolder
     */
    protected RecyclerViewImprove.FooterHolder createAdapterFooterHolder()
    {
        mFooterHolder=new CommonFooterHolder();
        return mFooterHolder;
    }

    /**
     * 添加一个顶部视图,这不是添加在Adapter中,而是直接在Fragment Root布局中添加
     * 在onViewCreated()中调用
     */
    protected View createTopView()
    {
        mTopLogView=new TextView(getContext());
        mTopLogView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTopLogView.setGravity(Gravity.CENTER);
        mTopLogView.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.blue_light,null));
        mTopLogView.setPadding(20,10,10,20);
        mTopLogView.setTextColor(Color.WHITE);
        mTopLogView.setClickable(false);
        mTopLogView.setFocusable(false);
        mTopLogView.setVisibility(View.INVISIBLE);
        return mTopLogView;
    }


    /**
     * 动画显示顶部视图
     * 如果覆盖createTopView()方法,此方法无效
     * @param text 显示的文本
     */
    public void showTopView(String text)
    {
        if (mTopLogView==null)
        {
            return;
        }
        mTopLogView.setVisibility(View.VISIBLE);
        mTopAniamtor=AnimatorInflater.loadAnimator(getContext(),R.animator.expand);
        mTopAniamtor.setTarget(mTopLogView);
        mTopAniamtor.start();
        mTopAniamtor.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //结束动画2秒后隐藏view
                mTopAniamtor=AnimatorInflater.loadAnimator(getContext(),R.animator.collaspe);
                mTopAniamtor.setStartDelay(2000);
                mTopAniamtor.setTarget(mTopLogView);
                mTopAniamtor.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mTopLogView.setText(text);
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


    /**
     * 清除刷新和加载更多状态
     */
    public void clearRequestState()
    {
        //确保正确关闭
        getView().post(new Runnable() {
            @Override
            public void run() {
                if (isRefreshing())
                {
                    setRefreshing(false);
                }
            }
        });
        cancelLoadMore();
    }

    protected void handleRequestError()
    {
        if (isRemoving())
        {
            return;
        }
        clearRequestState();

        int adapterItemCount=mAdapter.getItemCount();
        if (mSimpleEmptyViewHolder!=null)
        {
            mSimpleEmptyViewHolder.isClicked=false;

            if (adapterItemCount<=0)
            {
                mSimpleEmptyViewHolder.showErrorText();
            }
        }
        if (mFooterHolder!=null)
        {
            if (adapterItemCount>0)
                mFooterHolder.mFooterText.setText("请求失败,点击重试");
        }
    }



    protected void handleRequestSuccess()
    {
        if (isRemoving())
        {
            return;
        }

        if (isRefreshing())
        {
            showTopView("成功更新"+mAdapter.getItemCount()+"内容");
        }
        clearRequestState();

        if (mSimpleEmptyViewHolder!=null)
        {
            mSimpleEmptyViewHolder.isClicked=false;
        }
    }


    /**
     * 使用此方法表示网络请求到头,将会应用叁数去显示底部文本
     * onLoadMore 不会继续调用
     * @param footerText 底部显示文本
     */
    public void notifyRequestEnd(CharSequence footerText)
    {
        if (mFooterHolder!=null)
        {
            mFooterHolder.end(footerText);
        }
        disableLoadMore();
    }


    public void notifyRequestEnd()
    {
        notifyRequestEnd("~~~到底了~~~");
    }


    private class CommonFooterHolder extends RecyclerViewImprove.FooterHolder implements View.OnClickListener{
        public ProgressBar mFooterProgress;
        public TextView mFooterText;
        private boolean inEnd=false;

        public CommonFooterHolder()
        {
            this(LayoutInflater.from(getContext()).inflate(R.layout.rv_footerview,getRecyclerView(),false));
        }

        private CommonFooterHolder(View itemView) {
            super(itemView);
            mFooterProgress= (ProgressBar) itemView.findViewById(R.id.footer_progress);
            mFooterText= (TextView) itemView.findViewById(R.id.footer_text);
            mFooterText.setOnClickListener(this);
        }

        @Override
        public void viewAppear(RecyclerView.Adapter adapter) {
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


    public class SimpleEmptyViewHolder implements View.OnClickListener{

        public TextView mEmptyView;
        public boolean isClicked;//是否点击过,防止重复点击
        public View getView() {
            if (mEmptyView!=null)
            {
                return mEmptyView;
            }
            mEmptyView =new TextView(getContext());
            mEmptyView.setGravity(Gravity.CENTER);
            mEmptyView.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_refresh,0,0);
            mEmptyView.setOnClickListener(this);
            //刚创建时隐藏View,并设置文本
            mEmptyView.setVisibility(View.INVISIBLE);
            mEmptyView.setText("加载错误,点击重新加载...");
            return mEmptyView;
        }

        public void showErrorText()
        {
            if (mEmptyView.getVisibility()!=View.VISIBLE)
            {
                mEmptyView.setVisibility(View.VISIBLE);
            }
            mEmptyView.setText("加载错误,点击重新加载...");
        }

        public void onClick(View v) {
            if (!isClicked)
            {
                mEmptyView.setText("加载中...");
                onRefresh();
                isClicked=true;
            }
        }

    }
}
