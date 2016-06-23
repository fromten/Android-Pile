package learn.example.pile.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import learn.example.joke.R;

/**
 * Created on 2016/5/23.
 */
public abstract class FooterAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    //封装RecyclerView添加FooterView;
    private int TYPE_FOOTER = 99;

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_footer,parent,false);
            return new FooterViewHolder(view);
        } else {
            return createSelfViewHolder(parent, viewType);
        }
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
           if (getItemSize()==0)
           {
               holder.itemView.setVisibility(View.GONE);
           }
        } else {
            T t = (T) holder;
            bindSelfViewHolder(t, position);
        }
    }

    ;

    @Override
    public final int getItemCount() {
        return getItemSize() + 1;
    }

    @Override
    public final int getItemViewType(int position) {
        if (position == getItemCount() - 1)
            return TYPE_FOOTER;
        return getSelfViewType(position);
    }

    public int getSelfViewType(int position) {
        return 0;
    }

    public abstract int getItemSize();

    public abstract T createSelfViewHolder(ViewGroup parent, int type);

    public abstract void bindSelfViewHolder(T holder, int position);

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar mProgressBar;
        public TextView mTextView;
        public FooterViewHolder(View itemView) {
            super(itemView);
            mProgressBar= (ProgressBar) itemView.findViewById(R.id.footer_progress);
            mTextView= (TextView) itemView.findViewById(R.id.footer_text);
        }
    }
}


