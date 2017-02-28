package learn.example.pile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import learn.example.pile.R;
import learn.example.pile.adapters.base.GsonStateAdapter;
import learn.example.pile.pojo.Joke;
import learn.example.pile.ui.CircleViewTarget;
import learn.example.pile.ui.NineGridLayout;
import learn.example.pile.util.ActivityLauncher;
import learn.example.pile.util.DeviceInfo;
import learn.example.pile.util.GlideUtil;

/**
 * Created on 2016/8/11.
 */
public class JokeListAdapter extends GsonStateAdapter<Joke.Item, JokeListAdapter.JokeViewHolder> implements View.OnClickListener {

    public static final int TYPE_MULTI_IMAGE = 2;
    public static final int TYPE_SINGLE_VIDEO = 4;
    public static final int TYPE_SINGLE_IMAGE = 6;
    public static final int TYPE_NORMAL = 8;

    private Context mContext;
    private GlideUtil.CropSquareTransformation mCropSquareTransformation;
    private GlideUtil.FitGifTransform mFitGifTransform;
    private final int VIDEO_COVER_HEIGHT;

    public JokeListAdapter(Context context) {
        mContext = context;
        mCropSquareTransformation = new GlideUtil.CropSquareTransformation(mContext);
        mFitGifTransform = new GlideUtil.FitGifTransform(context);

        DeviceInfo info = new DeviceInfo(context);
        if (info.SCREEN_HEIGHT > info.SCREEN_WIDTH)//竖屏
        {
            VIDEO_COVER_HEIGHT = (int) (info.SCREEN_HEIGHT / 3.5f);
        } else {
            VIDEO_COVER_HEIGHT = (int) (info.SCREEN_HEIGHT / 1.5f);
        }
    }


    @Override
    public Class<Joke.Item> getActualClass() {
        return Joke.Item.class;
    }

    private void resetLayoutViewState(JokeSingleViewHolder holder) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT,
                        VIDEO_COVER_HEIGHT);
        holder.cover.setLayoutParams(params);
        holder.cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.ic_play.setVisibility(View.VISIBLE);
    }


    @Override
    public int getItemViewType(int position) {
        if (get(position).isMultiImages()) {
            return TYPE_MULTI_IMAGE;
        } else if (get(position).isVideo()) {
            return TYPE_SINGLE_VIDEO;
        } else if (get(position).getImage() != null) {
            return TYPE_SINGLE_IMAGE;
        }
        return TYPE_NORMAL;
    }


    @Override
    public JokeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        JokeViewHolder holder;
        switch (viewType) {
            case TYPE_MULTI_IMAGE:
                holder = new JokeMultipleViewHolder(parent);
                break;
            case TYPE_SINGLE_IMAGE:
            case TYPE_SINGLE_VIDEO:
                holder = new JokeSingleViewHolder(parent);
                break;
            case TYPE_NORMAL:
            default:
                View view = LayoutInflater.from(mContext)
                        .inflate(R.layout.item_joke_normal, parent, false);
                holder = new JokeViewHolder(view);
                break;
        }
        if (viewType == TYPE_SINGLE_IMAGE || viewType == TYPE_SINGLE_VIDEO) {
            ((JokeSingleViewHolder) holder).cover.setOnClickListener(this);
        }
        if (viewType == TYPE_SINGLE_VIDEO) {
            resetLayoutViewState((JokeSingleViewHolder) holder);
        }
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(JokeViewHolder holder, int position) {
        Joke.Item item = get(position);
        holder.userName.setText(item.getUserName());
        holder.contentText.setText(item.getText());
        holder.likeCount.setText(String.valueOf(item.getLikeCount()));
        holder.dislikeCount.setText(String.valueOf(item.getUnLikeCount()));
        holder.commentCount.setText("评论:".concat(String.valueOf(item.getCommentCount())));
        Glide.with(mContext)
                .load(item.getAvatar())
                .asBitmap()
                .error(R.mipmap.ic_def_show_user)
                .into(new CircleViewTarget(holder.avatar));

        holder.itemView.setTag(R.id.view_tag1, item);
        if (holder instanceof JokeMultipleViewHolder) {
            bindMulti((JokeMultipleViewHolder) holder, item);
        } else if (holder instanceof JokeSingleViewHolder) {
            bindSingle((JokeSingleViewHolder) holder, item);
        }
    }


    public void bindMulti(JokeMultipleViewHolder holder, Joke.Item item) {
        Joke.Image[] images = item.getImageUrls();
        String[] urls = new String[images.length];
        boolean[] isGif = new boolean[images.length];
        for (int i = 0; i < images.length; i++) {
            urls[i] = images[i].getUrl();
            isGif[i] = images[i].isGif();
        }
        holder.mGridLayout.updateViews(urls, isGif);
    }

    public void bindSingle(JokeSingleViewHolder holder, Joke.Item item) {
        holder.cover.setTag(R.id.view_tag1, item);
        if (item.isGif()) {
            holder.hint.setText(" gif ");
        } else {
            holder.hint.setText(null);
        }

        String url = item.getImage().getUrl();
        BitmapRequestBuilder b = Glide.with(mContext)
                .load(url).asBitmap()
                .dontAnimate()
                .dontTransform();
        if (item.isGif()) {   //gif图片通常过于太小
            b.transform(mFitGifTransform);
        } else {
            b.transform(mCropSquareTransformation);
            b.diskCacheStrategy(DiskCacheStrategy.ALL);
        }
        b.into(holder.cover);
    }

    @Override
    public void onClick(View v) {
        Joke.Item item = (Joke.Item) v.getTag(R.id.view_tag1);
        if (v.getId() == R.id.cover) {
            if (item.isVideo() || item.isGif()) {
                Joke.Video video = item.getVideo();
                if (video != null) {
                    String url = video.getUrl();
                    if (item.isVideo())
                        ActivityLauncher.startVideoActivity(mContext, url, true, false);
                    else ActivityLauncher.startVideoActivity(mContext, url, false, true);
                }
            } else {
                String url = item.getImage().getUrl();
                ActivityLauncher.startPhotoActivityForSingle(mContext, url);
            }
        } else {
            ActivityLauncher.startJokeCommentActivity(mContext, item.getId_str());
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
            likeCount = (TextView) itemView.findViewById(R.id.like);
            dislikeCount = (TextView) itemView.findViewById(R.id.dislike);
            commentCount = (TextView) itemView.findViewById(R.id.comment_num);
        }
    }

    public static class JokeSingleViewHolder extends JokeViewHolder {
        public ImageView cover;
        public ImageView ic_play;
        public TextView hint;

        public JokeSingleViewHolder(ViewGroup parent) {
            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joke_single, parent, false));
        }

        public JokeSingleViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            ic_play = (ImageView) itemView.findViewById(R.id.ic_play);
            hint = (TextView) itemView.findViewById(R.id.hint);
        }
    }


    public static class JokeMultipleViewHolder extends JokeViewHolder {

        public NineGridLayout mGridLayout;


        public JokeMultipleViewHolder(ViewGroup parent) {
            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joke_multiple, parent, false));
        }

        public JokeMultipleViewHolder(View itemView) {
            super(itemView);
            mGridLayout = (NineGridLayout) itemView.findViewById(R.id.table);
            mGridLayout.setItemClickListener(new NineGridLayout.OnItemClickListener() {
                @Override
                public void onItemClick(View view, ViewGroup parent, int position, String[] url) {
                    ActivityLauncher.startPhotoActivityForMulti(view.getContext(), url, position);
                }
            });
        }


    }
}
