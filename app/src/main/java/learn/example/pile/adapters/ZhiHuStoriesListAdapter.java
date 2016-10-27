package learn.example.pile.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import learn.example.pile.R;
import learn.example.pile.adapters.base.ParcelableStateAdapter;
import learn.example.pile.jsonbean.ZhihuStories;
import learn.example.pile.util.ActivityLauncher;

/**
 * Created on 2016/7/9.
 */
public class ZhiHuStoriesListAdapter extends ParcelableStateAdapter<ZhihuStories.StoriesBean,ZhiHuStoriesListAdapter.ZhihuListViewHolder> {

    private String date;
    private View.OnClickListener viewClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id= (int) v.getTag();
            ActivityLauncher.startZhihuReaderActivity(v.getContext(),id);
        }
    };

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public ZhihuListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_read,parent,false);
        ZhihuListViewHolder holder=new ZhihuListViewHolder(view);
        view.setOnClickListener(viewClick);
        return holder;
    }

    @Override
    public void onBindViewHolder(ZhihuListViewHolder holder, int position) {
        ZhihuStories.StoriesBean stories=get(position);

        String url = stories.getImage();
        if (url==null)
        {
            url=stories.getImages()[0];
        }

        Glide.with(holder.itemView.getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter().into(holder.image);
        holder.title.setText(stories.getTitle());
        holder.pushDate.setText(date);
        holder.itemView.setTag(stories.getId());
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
