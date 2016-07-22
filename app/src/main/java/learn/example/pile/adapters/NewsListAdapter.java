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
import learn.example.pile.util.TextUtil;


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
        String shortDesc=item.getDigest()!=null?TextUtil.firstSentence(item.getDigest()):null;
        holder.describe.setText(shortDesc);
        holder.title.setText(item.getTitle());
        holder.commentNum.setText(formatNumber(item.getReplyCount()));
        holder.docSource.setText(item.getSource());
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
        int tenThousand=num/10000;
        int thousand=num%10000/1000;
        return tenThousand+"."+thousand+"万评论";
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView describe;
        public ImageView img;
        public TextView commentNum;
        public TextView docSource;
        public NewsViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.news_title);
            describe = (TextView) itemView.findViewById(R.id.news_descr);
            img = (ImageView) itemView.findViewById(R.id.news_img);
            commentNum= (TextView) itemView.findViewById(R.id.comment_num);
            docSource= (TextView) itemView.findViewById(R.id.doc_source);
        }
    }
}
