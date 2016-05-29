package learn.example.pile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import learn.example.joke.R;
import learn.example.pile.jsonobject.BaseNewsData;


/**
 * Created on 2016/5/24.
 */
public class NewsListAdapter extends FooterAdapter<NewsListAdapter.NewsViewHolder> {

    private List<BaseNewsData> mItems;
   private Context mContext;
    public NewsListAdapter(Context context) {
        mItems = new ArrayList<>();
        this.mContext=context;
    }

    @Override
    public int getSelfItemSize() {
        return mItems.size();
    }
    public BaseNewsData getItem(int position)
    {
        return mItems.get(position);
    }
    public void clearItem()
    {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void addAllItem(List<BaseNewsData> datas)
    {
        mItems.addAll(datas);
        notifyItemInserted(mItems.size());
    }
    @Override
    public NewsViewHolder createSelfViewHolder(ViewGroup parent, int type) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.fragment_news_adpter_view,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void bindSelfViewHolder(NewsViewHolder holder, int position) {
        BaseNewsData data=mItems.get(position);
        holder.describe.setText(data.getNewsDesc());
        holder.title.setText(data.getNewsTitle());
        Glide.with(mContext).load(data.getNewsIMgUrl()).into(holder.img);
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView describe;
        public ImageView img;

        public NewsViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.news_title);
            describe= (TextView) itemView.findViewById(R.id.news_descr);
            img= (ImageView) itemView.findViewById(R.id.news_img);
        }
    }
}
