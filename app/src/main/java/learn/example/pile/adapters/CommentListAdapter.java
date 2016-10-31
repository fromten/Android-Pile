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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {
        Comment.CommentItem comment = get(position);
        final Context context = holder.itemView.getContext();
        holder.content.setText(comment.getContent());
        String userName=comment.getUserName();
        holder.userName.setText(userName==null?"网友":userName);

        Glide.with(context).load(comment.getAvatar()).asBitmap()
                .error(R.mipmap.ic_def_show_user)
                .placeholder(R.mipmap.ic_def_show_user)
                .fitCenter()
                .into(new CircleViewTarget(holder.avatar));
        String address=comment.getAddress()==null?"":comment.getAddress()+"\n";
        String addressAndTime=address+comment.getCommentTime();
        holder.addressAndTime.setText(addressAndTime);

        holder.like.setText(String.valueOf(comment.getLikeNumber()));

    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView content;
        public TextView userName;
        public TextView like;
        public ImageView avatar;
        private TextView addressAndTime;
        public CommentViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            like = (TextView) itemView.findViewById(R.id.like);
            addressAndTime= (TextView) itemView.findViewById(R.id.address_and_time);
        }
    }
}
