package learn.example.pile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import learn.example.pile.R;
import learn.example.pile.adapters.base.ParcelableStateAdapter;
import learn.example.pile.pojo.Comment;
import learn.example.pile.ui.CircleViewTarget;

/**
 * Created on 2016/7/13.
 */
public class CommentListAdapter extends ParcelableStateAdapter<Comment.CommentItem,CommentListAdapter.CommentViewHolder> {


    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comment, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {
        Comment.CommentItem c = get(position);
        final Context context = holder.itemView.getContext();
        holder.content.setText(c.getContent());
        String author=c.getAuthor();
        holder.author.setText(author==null?"网友":author);

        Glide.with(context).load(c.getUsePic()).asBitmap()
                .error(R.mipmap.ic_def_show_user)
                .placeholder(R.mipmap.ic_def_show_user)
                .fitCenter()
                .into(new CircleViewTarget(holder.user_pic));
        String address=c.getAddress()==null?"":c.getAddress()+"\n";
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
