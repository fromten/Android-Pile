package learn.example.pile.adapters;

import android.content.Context;
import android.content.Intent;
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
import learn.example.pile.MainActivity;
import learn.example.pile.WebViewActivity;
import learn.example.pile.jsonobject.NewsJsonData;


/**
 * Created on 2016/5/24.
 */
public class NewsListAdapter extends FooterAdapter<NewsListAdapter.NewsViewHolder> {

    private List<NewsJsonData.NewsItem> mItems;
    private Context mContext;

    public NewsListAdapter(Context context) {
        mItems = new ArrayList<>();
        this.mContext=context;
    }

    @Override
    public int getSelfItemSize() {
        return mItems.size();
    }
    public NewsJsonData.NewsItem getItem(int position)
    {
        return mItems.get(position);
    }
    public List<NewsJsonData.NewsItem> getAllItem()
    {
        return mItems;
    }
    public void clearItem()
    {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void addAllItem(List<NewsJsonData.NewsItem> datas)
    {
        mItems.addAll(datas);
        notifyDataSetChanged();
    }
    @Override
    public NewsViewHolder createSelfViewHolder(ViewGroup parent, int type) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.fragment_news_adpter_view,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void bindSelfViewHolder(final NewsViewHolder holder, final int position) {
        NewsJsonData.NewsItem item=mItems.get(position);
        holder.describe.setText(item.getNewsDes());
        holder.title.setText(item.getTitle());
        Glide.with(mContext).load(item.getImageUrl()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,WebViewActivity.class);
                intent.putExtra(WebViewActivity.KEY_URL,mItems.get(position).getNewsUrl());
                mContext.startActivity(intent);
            }
        });
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
