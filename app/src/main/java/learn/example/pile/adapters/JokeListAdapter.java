package learn.example.pile.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import learn.example.pile.R;
import learn.example.pile.adapters.base.GsonStateAdapter;
import learn.example.pile.jsonbean.JokeBean;
import learn.example.pile.ui.CircleViewTarget;
import learn.example.pile.ui.NineGridLayout;
import learn.example.pile.util.ActivityLauncher;
import learn.example.pile.util.DeviceInfo;
import learn.example.pile.util.GlideUtil;
import learn.example.pile.util.GsonHelper;

/**
 * Created on 2016/8/11.
 */
public class JokeListAdapter extends GsonStateAdapter<JokeBean.DataBean.DataListBean.GroupBean,JokeListAdapter.JokeViewHolder> implements View.OnClickListener{

    public static final int TYPE_MULTI = 2;
    public static final int TYPE_SINGLE = 4;
    public static final int TYPE_NORMAL = 6;

    private Context mContext;
    private GlideUtil.CropSquareTransformation mCropSquareTransformation;
    private GlideUtil.MatchWidthTransformation mMatchWidthTransformation;
    private int coverHeight;
    public JokeListAdapter(Context context) {
         mContext=context;
         mCropSquareTransformation=new GlideUtil.CropSquareTransformation(mContext);

         coverHeight = (int) (new DeviceInfo((Activity) mContext).SCREEN_HEIGHT/3.5f);
         mMatchWidthTransformation=new GlideUtil.MatchWidthTransformation(context,coverHeight);
    }


    @Override
    public Class<JokeBean.DataBean.DataListBean.GroupBean> getActualClass() {
        return JokeBean.DataBean.DataListBean.GroupBean.class;
    }



    @Override
    public int getItemViewType(int position) {
        if (get(position).is_multi_image()) {
            return TYPE_MULTI;
        }else if (get(position).getImages()!=null){
            return TYPE_SINGLE;
        }
        return TYPE_NORMAL;
    }



    @Override
    public JokeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        JokeBean.DataBean.DataListBean.GroupBean item = get(position);
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
        int len = item.getLarge_image_list().length;

        String[] urls = new String[len];
        boolean[] isGif=new boolean[len];
        for (int i = 0; i < len; i++) {
            JokeBean.DataBean.DataListBean.GroupBean.ImagesBean u = item.getLarge_image_list()[i];
            urls[i] = u.getUrl()!=null?u.getUrl():u.getFirstUrl();
            isGif[i]=u.is_gif();
        }
        holder.mGridLayout.updateViews(urls,isGif);
    }

    public void bindSingle(JokeSingleViewHolder holder, JokeBean.DataBean.DataListBean.GroupBean item) {
        String url=item.getImages().getFirstUrl();
        if (item.is_gif())
        {
            holder.hint.setText(" gif ");
        }else {
            holder.hint.setText(null);
        }
        holder.cover.setTag(R.id.view_tag1,item);
        BitmapRequestBuilder b=Glide.with(mContext)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        if (item.is_video())
        {
            holder.ic_play.setVisibility(View.VISIBLE);
            //让图片充满宽度
            b.transform(mMatchWidthTransformation);
        }else {
            holder.ic_play.setVisibility(View.INVISIBLE);
            if (item.is_gif())
            {
                //gif图片通常过于太小
                b.transform(new GlideUtil.FitGifTransform(mContext));
            }else {
                //截取中央部分
                b.transform(mCropSquareTransformation);
            }
        }
        b.into(holder.cover);
    }

    @Override
    public void onClick(View v) {
        JokeBean.DataBean.DataListBean.GroupBean item= (JokeBean.DataBean.DataListBean.GroupBean) v.getTag(R.id.view_tag1);
        if (v.getId()==R.id.cover){
            if (item.is_video())
            {
                String url=item.getMp4_url();
                ActivityLauncher.startVideoActivity(mContext,url);
            }else if (item.is_gif())
            {  if (item.getGifVideo()!=null)
            {
                String url= GsonHelper.getAsString(item.getGifVideo().get("mp4_url"),null);
                if (url==null)
                {
                    url=item.getGifUrl();
                }
                ActivityLauncher.startShortVideoActivity(mContext,url);
            }
            }else {
                String url=item.getImages().getFirstUrl();
                ActivityLauncher.startPhotoActivityForSingle(mContext,url);
            }
        }else {
            ActivityLauncher.startDetailActivity(mContext,new Gson().toJson(item));
        }
    }

    public static class JokeViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatar;
        public TextView userName;
        public TextView contentText;
        public TextView likeCount;
        public TextView dislikeCount;
        public TextView commentCount;

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
        public ImageView cover;
        public ImageView ic_play;
        public TextView hint;
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

        public NineGridLayout mGridLayout;


        public JokeMultipleViewHolder(ViewGroup parent) {
            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_joke_multiple, parent, false));
        }

        public JokeMultipleViewHolder(View itemView) {
            super(itemView);
            mGridLayout= (NineGridLayout) itemView.findViewById(R.id.table);
            mGridLayout.setItemClickListener(new NineGridLayout.OnItemClickListener() {
                @Override
                public void onItemClick(View view, ViewGroup parent, int position, String[] url) {
                    ActivityLauncher.startPhotoActivityForMulti(view.getContext(),url,position);
                }
            });
        }


    }
}
