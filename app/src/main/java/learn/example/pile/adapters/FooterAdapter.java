package learn.example.pile.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created on 2016/5/23.
 */
public abstract class FooterAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter{
        //封装RecyclerView添加FooterView;
        private int TYPE_ITEM=1;

        private int TYPE_FOOTER=2;
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==TYPE_FOOTER)
            {
                ProgressBar progressBar=new ProgressBar(parent.getContext());
                progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new FooterViewHolder(progressBar);
            }else if(viewType==TYPE_ITEM)
            {
                return onCreateItemHolder(parent);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
            if(holder instanceof FooterViewHolder){
                if(getItemSize()==0)
                { holder.itemView.setVisibility(View.INVISIBLE);//当前没有数据时隐藏

                } else
                {
                    holder.itemView.setVisibility(View.VISIBLE);
                }
                return;
            }
            T t=(T) holder;
            onBindItemHolder(t,position);
        };

        @Override
        public int getItemCount() {
            return getItemSize()+1;
        }

        @Override
        public int getItemViewType(int position) {
            if(position==getItemCount()-1)
                return TYPE_FOOTER;
            return TYPE_ITEM;
        }

        public abstract int getItemSize();
        public abstract T onCreateItemHolder(ViewGroup parent);
        public abstract void onBindItemHolder(T t,int position);


    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View view) {
            super(view);
        }
    }
}


