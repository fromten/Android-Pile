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
import learn.example.pile.adapters.base.ParcelableStateAdapter;
import learn.example.pile.fragment.comment.OpenEyeCommentFragment;
import learn.example.pile.provider.OpenEyes;
import learn.example.pile.util.ActivityLauncher;
import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/5/25.
 */
public class VideoListAdapter extends ParcelableStateAdapter<OpenEyes.VideoInfo,VideoListAdapter.VideoViewHolder> implements View.OnClickListener{


    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
        v.setOnClickListener(this);
        return new VideoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        OpenEyes.VideoInfo item=get(position);
        String timeAndTitle=item.getTitle()+"\n"+TimeUtil.formatMS(item.getDuration());
        holder.title.setText(timeAndTitle);
        Glide.with(holder.itemView.getContext())
                .load(item.getImgUrl())
                .fitCenter()
                .into(holder.videoImg);
        holder.itemView.setTag(position);
    }

    @Override
    public void onClick(View v) {
        int pos= (int) v.getTag();

        OpenEyes.VideoInfo info=get(pos);
        if (info!=null)
        {
            String uri=info.getPlayUrl();
            Bundle argus=new Bundle();
            argus.putInt(OpenEyeCommentFragment.ARGUMENT_VIDEO_ID,info.getVideoId());
            String fragmentClassName=OpenEyeCommentFragment.class.getName();
            ActivityLauncher.startVideoActivitySupportCommentMenu(v.getContext(),uri,info.getTitle(),fragmentClassName,argus);
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
