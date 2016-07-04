package learn.example.pile.adapters;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import learn.example.pile.VideoActivity;
import learn.example.pile.jsonobject.VideoJsonData;
import learn.example.pile.util.ActivityLauncher;

/**
 * Created on 2016/5/25.
 */
public class VideoListAdapter extends SaveStateAbleAdapter<VideoListAdapter.VideoViewHolder,VideoJsonData.VideoItem> implements View.OnClickListener{


    @Override
    public void updaterItemView(VideoViewHolder holder, int position) {
        VideoJsonData.VideoItem item=getItem(position);
        holder.desc.setText(item.getDesc());
        if(item.getImgUrl()!=null)
        {
            Glide.with(holder.itemView.getContext()).load(item.getImgUrl()).dontAnimate().centerCrop().into(holder.videoImg);
        }else
        {
            holder.videoImg.setImageBitmap(null);
        }
        holder.videoPlay.setTag(position);
        holder.videoPlay.setOnClickListener(this);
    }

    @Override
    public VideoViewHolder getItemViewHolder(ViewGroup parent, int type) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_video_adpter_view,parent,false);
        return new VideoViewHolder(v);
    }

    @Override
    public void onClick(View v) {
         int position = (int) v.getTag();
        if(position>=0)
        {
            VideoJsonData.VideoItem item=getItem(position);
            String fileUrl=item.getFileUrl();//是否存在视频文件
            Bundle anim=ActivityLauncher.slideAnimation(v.getContext());
            if (fileUrl!=null&&!fileUrl.isEmpty())
            {   //如果有视频实际地址,打开视频播放器
                ActivityLauncher.startVideoActivity(v.getContext(),fileUrl,anim);
            }else {
                //没有则,打开web浏览器
                ActivityLauncher.startInternalWebActivity(v.getContext(),item.getSrcUrl(),anim);
            }
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder
    {
        public TextView desc;
        public ImageView videoImg;
        public ImageView videoPlay;
        public VideoViewHolder(View view) {
            super(view);
            this.desc = (TextView) view.findViewById(R.id.video_desc);
            this.videoImg = (ImageView) view.findViewById(R.id.video_img);;
            this.videoPlay = (ImageView) view.findViewById(R.id.video_play);;
        }
    }
}
