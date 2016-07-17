package learn.example.pile.adapters;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import learn.example.pile.R;
import learn.example.pile.jsonbean.NewsJsonData;
import learn.example.pile.util.ActivityLauncher;


/**
 * Created on 2016/5/24.
 */
public class NewsListAdapter extends SaveStateAbleAdapter<NewsListAdapter.NewsViewHolder, NewsJsonData.NewsItem> {

    private View.OnClickListener mItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = (String) v.getTag();
            if (url != null) {
                Bundle anim = ActivityLauncher.slideAnimation(v.getContext());
                ActivityLauncher.startInternalWebActivity(v.getContext(), url, anim);
            }
        }
    };


    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_news, parent, false);
        view.setOnClickListener(mItemClick);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsJsonData.NewsItem item = getItem(position);
        holder.describe.setText(item.getNewsDes());
        holder.title.setText(item.getTitle());
        Glide.with(holder.itemView.getContext()).load(item.getImageUrl()).into(holder.img);
        holder.itemView.setTag(item.getNewsUrl());
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView describe;
        public ImageView img;

        public NewsViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.news_title);
            describe = (TextView) itemView.findViewById(R.id.news_descr);
            img = (ImageView) itemView.findViewById(R.id.news_img);
        }
    }
}
