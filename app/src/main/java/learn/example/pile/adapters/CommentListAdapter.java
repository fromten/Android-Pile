package learn.example.pile.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.util.TimeUtils;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.BitmapImageViewTarget;


import learn.example.joke.R;
import learn.example.pile.jsonbean.ZhihuComment;
import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/7/13.
 */
public class CommentListAdapter extends SaveStateAbleAdapter<CommentListAdapter.CommentViewHolder,ZhihuComment.CommentsBean> {

    @Override
    public void updaterItemView(final CommentViewHolder holder, int position) {
        ZhihuComment.CommentsBean bean=getItem(position);
        final Context context=holder.itemView.getContext();
        holder.content.setText(bean.getContent());
        holder.author.setText(bean.getAuthor());

        Glide.with(context).load(bean.getAvatar()).asBitmap().fitCenter()
                .into(new BitmapImageViewTarget(holder.user_pic){
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.user_pic.setImageDrawable(circularBitmapDrawable);
                    }
                });
        String show= String.valueOf(bean.getLikes())+" 赞\n"+ TimeUtil.formatYMD(bean.getTime());
        holder.likeAndTime.setText(show);
    }

    @Override
    public CommentViewHolder getItemViewHolder(ViewGroup parent, int type) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_comment,parent,false);
        CommentViewHolder holder=new CommentViewHolder(view);
        return holder;
    }

    @Override
    protected View onCreateFooterView(ViewGroup parent) {
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView=new TextView(parent.getContext());
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void updateFooterView(RecyclerView.ViewHolder holder, int position) {
         if (getItemSize()<=0)
         {
             holder.itemView.setVisibility(View.INVISIBLE);
         }else {
             if(holder.itemView instanceof TextView){
                 ((TextView) holder.itemView).setText("没有了...");
             }
         }
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{
        public TextView content;
        public TextView author;
        public TextView likeAndTime;
        public ImageView user_pic;

        public CommentViewHolder(View itemView) {
            super(itemView);
            content= (TextView) itemView.findViewById(R.id.content);
            author= (TextView) itemView.findViewById(R.id.author);
            user_pic= (ImageView) itemView.findViewById(R.id.user_pic);
            likeAndTime= (TextView) itemView.findViewById(R.id.like_and_time);
        }
    }
}
