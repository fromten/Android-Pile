package learn.example.pile.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created on 2016/8/27.
 */
public class RecyclerViewImprove extends RecyclerView {

    private AdapterWrapper adapterWrap;
    private onItemClickListener mItemClickListener;

    public RecyclerViewImprove(Context context) {
        super(context);
    }

    public RecyclerViewImprove(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        adapterWrap =new AdapterWrapper(adapter);
        super.setAdapter(adapterWrap);
    }

    public Adapter getInnerAdapter()
    {
        return adapterWrap==null?null:adapterWrap.mInnerAdapter;
    }

    @Override
    public AdapterWrapper getAdapter() {
        return adapterWrap;
    }

    public static class AdapterWrapper extends RecyclerView.Adapter{

        private final int TYPE_FOOTER=66;
        private final int TYPE_HEAD=88;

        private RecyclerView.Adapter mInnerAdapter;
        private FooterHolder mFooterHolder;
        private HeadHolder mHeadHolder;

        public AdapterWrapper(RecyclerView.Adapter adapter) {
            if (adapter==null)
            {
                throw new NullPointerException("Adapter must not be null");
            }
            mInnerAdapter = adapter;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType==TYPE_FOOTER)
            {
                return mFooterHolder;
            }
            if (viewType==TYPE_HEAD)
            {
                return mHeadHolder;
            }
            return mInnerAdapter.onCreateViewHolder(parent,viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeadHolder)
            {
                mHeadHolder.viewAppear(mInnerAdapter);
            }else if (holder instanceof FooterHolder)
            {
                mFooterHolder.viewAppear(mInnerAdapter);
            }else{
                //在有头部的情况下,实际的位置要减一
                int realPos=mHeadHolder==null?position:position-1;

                mInnerAdapter.onBindViewHolder(holder,realPos);
            }
        }

        @Override
        public int getItemCount() {
            int footerNum=mFooterHolder==null?0:1;
            int headNum=mHeadHolder==null?0:1;
            return mInnerAdapter.getItemCount()+footerNum+headNum;
        }

        public int getInnerAdapterItemCount() {
            return mInnerAdapter.getItemCount();
        }

        @Override
        public int getItemViewType(int position) {
            if (mHeadHolder!=null&&position==0){
                return TYPE_HEAD;
            }else if (position==getItemCount()-1&&mFooterHolder!=null)
            {
                return TYPE_FOOTER;
            }
            return mInnerAdapter.getItemViewType(position);
        }

        public void setFooterHolder(FooterHolder holder)
        {
            mFooterHolder=holder;
            notifyDataSetChanged();
        }

        public void setHeadHolder(HeadHolder holder)
        {
            mHeadHolder=holder;
            notifyItemRangeChanged(0,1);
        }

        public FooterHolder getFooterHolder() {
            return mFooterHolder;
        }

        public HeadHolder getHeadHolder() {
            return mHeadHolder;
        }
    }


    public interface onItemClickListener{
        void onItemClick(int position,View view,Adapter adapter,ViewGroup parent);
    }

    public static abstract class FooterHolder extends RecyclerView.ViewHolder{
        public FooterHolder(View itemView) {
            super(itemView);
        }
        //显示时调用
        public abstract void viewAppear(RecyclerView.Adapter adapter);
    }
    public static abstract class HeadHolder extends RecyclerView.ViewHolder{

        public HeadHolder(View itemView) {
            super(itemView);
        }
        //显示时调用
        public abstract void viewAppear(RecyclerView.Adapter adapter);
    }

}
