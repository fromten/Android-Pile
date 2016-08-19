package learn.example.pile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import learn.example.pile.R;
import learn.example.pile.jsonbean.JokeBean;
import learn.example.pile.ui.CircleViewTarget;
import learn.example.pile.ui.NineGridLayout;
import learn.example.pile.util.ActivityLauncher;
import learn.example.pile.util.GsonHelper;

/**
 * Created on 2016/8/11.
 */
public class JokeListAdapter extends GsonSaveStateAdapter<JokeBean.DataBean.DataListBean.GroupBean,JokeListAdapter.JokeViewHolder> implements View.OnClickListener{

    private final int TYPE_MULTI = 2;
    private final int TYPE_SINGLE = 4;
    private final int TYPE_NORMAL = 6;

    private Context mContext;
    private CropSquareTransformation mCropSquareTransformation;

    public JokeListAdapter() {

    }


    @Override
    public Class<JokeBean.DataBean.DataListBean.GroupBean> getActualClass() {
        return JokeBean.DataBean.DataListBean.GroupBean.class;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getIs_multi_image() == 1) {
            return TYPE_MULTI;
        }else if (getItem(position).getImages()!=null){
            return TYPE_SINGLE;
        }
        return TYPE_NORMAL;
    }



    @Override
    public JokeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
            mCropSquareTransformation=new CropSquareTransformation(mContext);
        }
        JokeViewHolder holder;
        if (viewType == TYPE_MULTI) {
            holder= new JokeMultipleViewHolder(parent);
        }else if (viewType== TYPE_SINGLE)
        {
            holder=new JokeSingleViewHolder(parent);
            ((JokeSingleViewHolder)holder).cover.setOnClickListener(this);
        }else {
            View view=LayoutInflater.from(mContext).inflate(R.layout.adapter_joke_normal,parent,false);
            holder=new JokeViewHolder(view);
        }
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(JokeViewHolder holder, int position) {
        JokeBean.DataBean.DataListBean.GroupBean item = getItem(position);
        String avatarUrl = item.getUser().getAvatar_url();
        String name = item.getUser().getName();
        String text=item.getText();
        int commentCount=item.getComment_count();
        int dislikeCount=item.getBury_count();
        int likeCount=item.getDigg_count();
        holder.userName.setText(name);
        holder.contentText.setText(text);
        holder.likeCount.setText(String.valueOf(likeCount));
        holder.dislikeCount.setText(String.valueOf(dislikeCount));
        holder.commentCount.setText("评论:"+String.valueOf(commentCount));
        Glide.with(mContext)
                .load(avatarUrl)
                .asBitmap()
                .error(R.mipmap.ic_def_show_user)
                .into(new CircleViewTarget(holder.avatar));

        holder.itemView.setTag(R.id.view_tag1,item);
        if (holder instanceof JokeMultipleViewHolder) {
            bindMulti((JokeMultipleViewHolder) holder, item);
        }else if (holder instanceof JokeSingleViewHolder)
        {
            bindSingle((JokeSingleViewHolder) holder,item);
        }

    }


    public void bindMulti(JokeMultipleViewHolder holder, JokeBean.DataBean.DataListBean.GroupBean item) {
        int len = item.getThumb_image_list().length;

        String[] urls = new String[len];
        for (int i = 0; i < len; i++) {
            JokeBean.DataBean.DataListBean.GroupBean.ImagesBean u = item.getThumb_image_list()[i];
            urls[i] = u.getUrl_list()[0].getUrl();
        }
        holder.updateViews(urls);
    }

    public void bindSingle(JokeSingleViewHolder holder, JokeBean.DataBean.DataListBean.GroupBean item) {
        String url=item.getImages().getUrl_list()[1].getUrl();
        if (item.getIs_gif()==1)
        {
            holder.hint.setText(" gif ");
        }else {
            holder.hint.setText(null);
        }
        if (item.getIs_video()==1)
        {
            holder.ic_play.setVisibility(View.VISIBLE);
        }else {
            holder.ic_play.setVisibility(View.INVISIBLE);
        }
        holder.cover.setTag(R.id.view_tag1,item);
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .transform(mCropSquareTransformation)
                .into(holder.cover);

    }

    @Override
    public void onClick(View v) {
        JokeBean.DataBean.DataListBean.GroupBean item= (JokeBean.DataBean.DataListBean.GroupBean) v.getTag(R.id.view_tag1);
        if (v.getId()==R.id.cover){
            if (item.getIs_video()==1)
            {
                String url=item.getMp4_url();
                ActivityLauncher.startVideoActivity(mContext,url);
            }else if (item.getIs_gif()==1)
            {  if (item.getGifVideo()!=null)
            {
                String url= GsonHelper.getAsString(item.getGifVideo().get("mp4_url"),null);
                if (url!=null)
                {
                    ActivityLauncher.startShortVideoActivity(mContext,url);
                }
            }
            }else {
                String url=item.getImages().getUrl_list()[0].getUrl();
                ActivityLauncher.startPhotoActivityForSingle(mContext,url);
            }
        }else {
            ActivityLauncher.startDetailActivity(mContext,new Gson().toJson(item));
        }
    }

    public static class JokeViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView userName;
        private TextView contentText;
        private TextView likeCount;
        private TextView dislikeCount;
        private TextView commentCount;

        public JokeViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            contentText = (TextView) itemView.findViewById(R.id.text);
            likeCount= (TextView) itemView.findViewById(R.id.like);
            dislikeCount= (TextView) itemView.findViewById(R.id.dislike);
            commentCount= (TextView) itemView.findViewById(R.id.comment_num);
        }
    }

    public static class JokeSingleViewHolder extends JokeViewHolder {
        private ImageView cover;
        private ImageView ic_play;
        private TextView hint;
        public JokeSingleViewHolder(ViewGroup parent) {
            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_joke_single, parent, false));
        }

        public JokeSingleViewHolder(View itemView) {
            super(itemView);
            cover= (ImageView) itemView.findViewById(R.id.cover);
            ic_play= (ImageView) itemView.findViewById(R.id.ic_play);
            hint= (TextView) itemView.findViewById(R.id.hint);
        }
    }


    public static class JokeMultipleViewHolder extends JokeViewHolder {

        private NineGridLayout mGridLayout;
        private String urls[];

        public JokeMultipleViewHolder(ViewGroup parent) {
            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_joke_multiple, parent, false));
        }

        public JokeMultipleViewHolder(View itemView) {
            super(itemView);
            mGridLayout= (NineGridLayout) itemView.findViewById(R.id.table);
            mGridLayout.setItemClickListener(new NineGridLayout.OnItemClickListener() {
                @Override
                public void onItemClick(View view, ViewGroup parent, int position) {
                    ActivityLauncher.startPhotoActivityForMuliti(view.getContext(),urls,position);
                }
            });
        }

        public void updateViews(String[] urls)
        {
            if (urls==null||urls.length<0)
            {
                return;
            }
            this.urls=urls;
            mGridLayout.updateViews(urls);
        }

    }
}
