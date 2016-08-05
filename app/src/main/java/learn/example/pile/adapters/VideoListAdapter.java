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
import learn.example.pile.object.OpenEyes;
import learn.example.pile.util.ActivityLauncher;
import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/5/25.
 */
public class VideoListAdapter extends SaveStateAbleAdapter<VideoListAdapter.VideoViewHolder,OpenEyes.VideoInfo> implements View.OnClickListener{


    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video,parent,false);
        v.setOnClickListener(this);
        return new VideoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        OpenEyes.VideoInfo item=getItem(position);
        String timeAndTitle=item.getTitle()+"\n"+TimeUtil.formatMS(item.getDuration());
        holder.title.setText(timeAndTitle);
        Glide.with(holder.itemView.getContext()).load(item.getImgUrl()).dontAnimate().fitCenter().into(holder.videoImg);
        holder.itemView.setTag(position);
    }

    @Override
    public void onClick(View v) {
        int pos= (int) v.getTag();

        OpenEyes.VideoInfo info=getItem(pos);
        if (info!=null)
        {
            Bundle anim= ActivityLauncher.slideAnimation(v.getContext());
            ActivityLauncher.startVideoActivityForOpenEye(v.getContext(),info,anim);
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public ImageView videoImg;
        public VideoViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title);
            this.videoImg = (ImageView) view.findViewById(R.id.video_img);
        }
    }
}
