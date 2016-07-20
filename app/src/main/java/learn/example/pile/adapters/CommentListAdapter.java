package learn.example.pile.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.BitmapImageViewTarget;


import learn.example.pile.R;
import learn.example.pile.object.Comment;

/**
 * Created on 2016/7/13.
 */
public class CommentListAdapter extends SaveStateAbleAdapter<CommentListAdapter.CommentViewHolder,Comment> {


    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comment, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {
        Comment c = getItem(position);
        final Context context = holder.itemView.getContext();
        holder.content.setText(c.getContent());
        holder.author.setText(c.getAuthor());

        Glide.with(context).load(c.getUsePic()).asBitmap().fitCenter()
                .into(new BitmapImageViewTarget(holder.user_pic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.user_pic.setImageDrawable(circularBitmapDrawable);
                    }
                });
        String address=c.getAddress()==null?"":c.getAddress();
        holder.addressAndTime.setText(address+c.getTime());

        holder.like.setText(String.valueOf(c.getLikeNumber()));

    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView content;
        public TextView author;
        public TextView like;
        public ImageView user_pic;
        private TextView addressAndTime;
        public CommentViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            author = (TextView) itemView.findViewById(R.id.author);
            user_pic = (ImageView) itemView.findViewById(R.id.user_pic);
            like = (TextView) itemView.findViewById(R.id.like);
            addressAndTime= (TextView) itemView.findViewById(R.id.adress_and_time);
        }
    }
}
