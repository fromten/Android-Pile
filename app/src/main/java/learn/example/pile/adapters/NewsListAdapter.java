package learn.example.pile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collection;

import learn.example.pile.R;
import learn.example.pile.adapters.base.ParcelableStateAdapter;
import learn.example.pile.jsonbean.NetEaseNews;
import learn.example.pile.util.ActivityLauncher;


/**
 * Created on 2016/5/24.
 */
public class NewsListAdapter extends ParcelableStateAdapter<NetEaseNews.NewsItem,NewsListAdapter.BaseNewsViewHolder> {

    private static final int TYPE_NORMAL=5;
    private static final int TYPE_IMAGES=6;

    private View.OnClickListener mItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position= (int) v.getTag();
            NetEaseNews.NewsItem item=get(position);

            if (item.getSkipType()!=null&&item.getSkipType().equals("photoset"))
            {
                //启动图片查看器
                ActivityLauncher.startPhotoActivityForNetEase(v.getContext(),item.getSkipID());
            }else {
                //启动新闻阅读器
                String[] array=new String[]{item.getBoardid(),item.getDocid()};
                ActivityLauncher.startReaderActivityForNetEase(v.getContext(),array);
            }

        }
    };


    @Override
    public boolean addAll(Collection<? extends NetEaseNews.NewsItem> collection) {
        if (collection!=null)
        {
            for (NetEaseNews.NewsItem item:collection)
            {
                int replyCount=item.getReplyCount();
                item.setReplayString(formatNumber(replyCount));
            }
        }
        return super.addAll(collection);
    }


    //格式化数字
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

    @Override
    public BaseNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_IMAGES)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_news_images, parent, false);
            view.setOnClickListener(mItemClick);
            return new NewsImagesViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_news_normal, parent, false);
        view.setOnClickListener(mItemClick);
        return new NewsNormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseNewsViewHolder holder, int position) {
        NetEaseNews.NewsItem item = get(position);
        holder.title.setText(item.getTitle());
        holder.commentNum.setText(item.getReplayString());
        holder.docSource.setText(item.getSource());

        holder.itemView.setTag(position);

        if (holder instanceof NewsNormalViewHolder)
        {
            setNormalViews((NewsNormalViewHolder) holder,position);
        }else if (holder instanceof NewsImagesViewHolder){
            setImageViews((NewsImagesViewHolder) holder,position);
        }

    }

    private void setNormalViews(NewsNormalViewHolder holder,int position)
    {
        NetEaseNews.NewsItem item=get(position);
        Glide.with(holder.itemView.getContext())
                .load(item.getImgsrc())
                .asBitmap()
                .centerCrop()
                .into(holder.img);
    }



    private void setImageViews(NewsImagesViewHolder holder,int position)
    {
        NetEaseNews.NewsItem item=get(position);
        Context context=holder.itemView.getContext();
        Glide.with(context).load(item.getImgsrc())
                .asBitmap()
                .centerCrop()
                .into(holder.mImageView1);
        NetEaseNews.NewsItem.ImageExtraBean[] images=item.getImgnewextra();
        if (images!=null)
        {
            int size=images.length;
            for (int i = 0; i < size; i++) {
                if (i==0)
                {
                    Glide.with(context).load(images[i]
                            .getImgsrc())
                            .asBitmap()
                            .centerCrop()
                            .into(holder.mImageView2);
                }else if (i==1)
                {
                    Glide.with(context).load(images[i].getImgsrc())
                            .asBitmap()
                            .centerCrop()
                            .into(holder.mImageView3);
                }
            }
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (get(position).getImgnewextra()!=null)
        {
            return TYPE_IMAGES;
        }
        return TYPE_NORMAL;
    }




    public static class BaseNewsViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView commentNum;
        public TextView docSource;

        public BaseNewsViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.news_title);
            commentNum= (TextView) itemView.findViewById(R.id.comment_num);
            docSource= (TextView) itemView.findViewById(R.id.doc_source);
        }
    }

    public static class NewsNormalViewHolder extends BaseNewsViewHolder {
      //  public TextView describe;
        public ImageView img;
        public NewsNormalViewHolder(View itemView) {
            super(itemView);
           // describe = (TextView) itemView.findViewById(R.id.news_descr);
            img = (ImageView) itemView.findViewById(R.id.news_img);
        }
    }

    public static class NewsImagesViewHolder extends BaseNewsViewHolder {

        public ImageView mImageView1;
        public ImageView mImageView2;
        public ImageView mImageView3;
        public NewsImagesViewHolder(View itemView) {
            super(itemView);
            mImageView1= (ImageView) itemView.findViewById(R.id.image_extra1);
            mImageView2= (ImageView) itemView.findViewById(R.id.image_extra2);
            mImageView3= (ImageView) itemView.findViewById(R.id.image_extra3);
        }
    }
}
