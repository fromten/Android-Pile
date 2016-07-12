package learn.example.pile.adapters;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.nio.charset.Charset;

import learn.example.joke.R;
import learn.example.pile.object.Zhihu;
import learn.example.pile.util.ActivityLauncher;

/**
 * Created on 2016/7/9.
 */
public class ZhiHuMsgListAdapter extends SaveStateAbleAdapter<ZhiHuMsgListAdapter.ZhihuListViewHolder,Zhihu.Story> {

    private String formatDate;

    private View.OnClickListener viewClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id= (int) v.getTag();
            Bundle anim= ActivityLauncher.slideAnimation(v.getContext());
            ActivityLauncher.startReaderActivity(v.getContext(),id,anim);
        }
    };



    @Override
    public void updaterItemView(ZhihuListViewHolder holder, int position) {
        Zhihu.Story story=getItem(position);

        Glide.with(holder.itemView.getContext()).load(story.getImageUrls()[0]).diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter().into(holder.image);
        holder.title.setText(story.getTitle());
        holder.pushDate.setText(story.getDate());
        holder.itemView.setTag(story.getId());
    }

    @Override
    public ZhihuListViewHolder getItemViewHolder(ViewGroup parent, int type) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_read_adpter_view,parent,false);
        ZhihuListViewHolder holder=new ZhihuListViewHolder(view);
        view.setOnClickListener(viewClick);
        return holder;
    }

    public static class ZhihuListViewHolder extends RecyclerView.ViewHolder{
        public TextView pushDate;
        public TextView title;
        public ImageView image;
        public ZhihuListViewHolder(View itemView) {
            super(itemView);
            pushDate= (TextView) itemView.findViewById(R.id.push_date);
            title= (TextView) itemView.findViewById(R.id.title);
            image= (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
