package learn.example.pile.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.AppLaunchChecker;
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
import learn.example.pile.WebViewActivity;
import learn.example.pile.jsonobject.NewsJsonData;
import learn.example.pile.util.ActivityLauncher;


/**
 * Created on 2016/5/24.
 */
public class NewsListAdapter extends SaveStateAbleAdapter<NewsListAdapter.NewsViewHolder,NewsJsonData.NewsItem> {

    private View.OnClickListener mItemClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url= (String) v.getTag();
            if (url!=null)
            {
                Bundle anim=ActivityLauncher.slideAnimation(v.getContext());
                ActivityLauncher.startInternalWebActivity(v.getContext(),url,anim);
            }
        }
    };

    @Override
    public NewsViewHolder getItemViewHolder(ViewGroup parent, int type) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_adpter_view,parent,false);
        view.setOnClickListener(mItemClick);
        return new NewsViewHolder(view);
    }

    @Override
    public void updaterItemView(NewsViewHolder holder, int position) {
        NewsJsonData.NewsItem item=getItem(position);
        holder.describe.setText(item.getNewsDes());
        holder.title.setText(item.getTitle());
        Glide.with(holder.itemView.getContext()).load(item.getImageUrl()).into(holder.img);
        holder.itemView.setTag(item.getNewsUrl());
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
