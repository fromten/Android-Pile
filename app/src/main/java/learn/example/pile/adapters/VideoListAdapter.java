package learn.example.pile.adapters;

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
import learn.example.pile.WebViewActivity;
import learn.example.pile.VideoActivity;
import learn.example.pile.jsonobject.VideoJsonData;

/**
 * Created on 2016/5/25.
 */
public class VideoListAdapter extends FooterAdapter<VideoListAdapter.VideoViewHolder> implements View.OnClickListener{

    private List<VideoJsonData.VideoItem> mItemList;


    public VideoListAdapter()
    {
        mItemList=new ArrayList<>();
    }

    public List<VideoJsonData.VideoItem> getItemList()
    {
        return mItemList;
    }
    public VideoJsonData.VideoItem getItem(int position)
    {
        return mItemList.get(position);
    }
    public void addAllItem(List<VideoJsonData.VideoItem> list)
    {
        if (list==null||list.isEmpty())
        {
            return;
        }
        mItemList.addAll(list);
        notifyItemInserted(getItemSize());
    }

    public void clearAll()
    {
        mItemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemSize() {
        return mItemList.size();
    }

    @Override
    public VideoViewHolder createSelfViewHolder(ViewGroup parent, int type) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_video_adpter_view,parent,false);
        return new VideoViewHolder(v);
    }

    @Override
    public void bindSelfViewHolder(VideoViewHolder holder, int position) {
        VideoJsonData.VideoItem item=mItemList.get(position);
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
    public void onClick(View v) {
         int position = (int) v.getTag();
        if(position>=0)
        {   Intent intent;
            VideoJsonData.VideoItem item=mItemList.get(position);
            boolean haveFileUrl=item.getFileUrl()!=null;//是否存在视频文件
            //根据haveFileUrl去打开一个网页播放或启动视频播放
            intent=haveFileUrl?new Intent(v.getContext(),VideoActivity.class):new Intent(v.getContext(),WebViewActivity.class);
            String url=haveFileUrl?item.getFileUrl():item.getSrcUrl();
            String key=haveFileUrl?VideoActivity.KEY_VIDEO_URL:WebViewActivity.KEY_URL;
            intent.putExtra(key,url);
            v.getContext().startActivity(intent);
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
