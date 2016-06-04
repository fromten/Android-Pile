package learn.example.pile.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created on 2016/5/23.
 */
public abstract class FooterAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter{
        //封装RecyclerView添加FooterView;
        private int TYPE_FOOTER=99;
        @Override
        public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==TYPE_FOOTER)
            {
                TextView textView=new TextView(parent.getContext());
                textView.setText("加载数据中...");
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new FooterViewHolder(textView);
            }else
            {
                return createSelfViewHolder(parent,viewType);
            }
        }

        @Override
        public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
            if(holder instanceof FooterViewHolder){
                if(getSelfItemSize()==0)
                { holder.itemView.setVisibility(View.INVISIBLE);//当前没有数据时隐藏
                } else
                {
                    holder.itemView.setVisibility(View.VISIBLE);
                }
                return;
            }
            T t=(T) holder;
            bindSelfViewHolder(t,position);
        };

        @Override
        public final int getItemCount() {
            return getSelfItemSize()+1;
        }

        @Override
        public final int getItemViewType(int position) {
            if(position==getItemCount()-1)
                return TYPE_FOOTER;
            return getSelfViewType(position);
        }
        public int getSelfViewType(int position)
        {
            return 0;
        }
        public abstract int getSelfItemSize();
        public abstract T createSelfViewHolder(ViewGroup parent,int type);
        public abstract void bindSelfViewHolder(T holder,int position);

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View view) {
            super(view);
        }
    }
}


