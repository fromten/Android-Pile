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
import learn.example.pile.jsonobject.BaseVideoData;

/**
 * Created on 2016/5/25.
 */
public class VideoListAdapter extends FooterAdapter<VideoListAdapter.VideoViewHolder> implements View.OnClickListener {

    private List<BaseVideoData> mList;

    public VideoListAdapter() {
        mList=new ArrayList<>();

    }
    public void addItemAll(List<BaseVideoData> items)
    {
        mList.addAll(items);
        notifyItemInserted(mList.size());
    }
    public void clear()
    {
        mList.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemSize() {
        return mList.size();
    }

    @Override
    public VideoViewHolder onCreateItemHolder(ViewGroup parent) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_video_adpter_view,parent,false);
        return new VideoViewHolder(v);
    }

    @Override
    public void onBindItemHolder(VideoViewHolder holder, int position) {
          BaseVideoData item=mList.get(position);
          holder.desc.setText(item.getDesc());
          if(item.getImgUrl()!=null)
          {
              Glide.with(holder.itemView.getContext()).load(item.getImgUrl()).into(holder.videoImg);
          }else
          {
              holder.videoImg.setImageBitmap(null);
          }


          holder.videoPlay.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

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
