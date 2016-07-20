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
import learn.example.pile.jsonbean.NetEaseNews;
import learn.example.pile.jsonbean.NewsJsonData;
import learn.example.pile.object.NetEase;
import learn.example.pile.util.ActivityLauncher;


/**
 * Created on 2016/5/24.
 */
public class NewsListAdapter extends SaveStateAbleAdapter<NewsListAdapter.NewsViewHolder, NetEaseNews.T1348647909107Bean> {

    private View.OnClickListener mItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] tag= (String[]) v.getTag();
            Bundle anim= ActivityLauncher.slideAnimation(v.getContext());
            ActivityLauncher.startReaderActivityFromNetEase(v.getContext(),tag,anim);
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
        NetEaseNews.T1348647909107Bean  item = getItem(position);
        holder.describe.setText(item.getDigest());
        holder.title.setText(item.getTitle());
        holder.comment.setText(formatNumber(item.getReplyCount()));
        Glide.with(holder.itemView.getContext()).load(item.getImgsrc()).into(holder.img);

        String[] arry={item.getBoardid(),item.getDocid()};
        holder.itemView.setTag(arry);
    }

    private String formatNumber(int num)
    {
        if (num<10000)
        {
            return num+"评论";
        }
        return (num/10000)+"万评论";
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView describe;
        public ImageView img;
        public TextView comment;
        public NewsViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.news_title);
            describe = (TextView) itemView.findViewById(R.id.news_descr);
            img = (ImageView) itemView.findViewById(R.id.news_img);
            comment= (TextView) itemView.findViewById(R.id.comment);
        }
    }
}
