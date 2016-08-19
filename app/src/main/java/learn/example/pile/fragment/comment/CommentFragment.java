package learn.example.pile.fragment.comment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import learn.example.pile.R;
import learn.example.pile.adapters.CommentListAdapter;
import learn.example.pile.fragment.base.BaseListFragment;
import learn.example.pile.object.Comment;


/**
 * Created on 2016/7/13.
 */
public class CommentFragment extends BaseListFragment {

    private CommentListAdapter mCommentListAdapter;
    private WaitEmptyViewHolder mWaitEmptyViewHolder;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDisEnableRefresh(true);
        mCommentListAdapter=new CommentListAdapter();
        setAdapter(mCommentListAdapter);
    }

    public void addComments(List<Comment.CommentItem> comments)
    {
        mCommentListAdapter.addAll(comments);
    }

    @Override
    protected View createTopView() {
        return null;
    }

    @Override
    protected View createEmptyView() {
       mWaitEmptyViewHolder=new WaitEmptyViewHolder((ViewGroup) getView());
       return mWaitEmptyViewHolder.mRoot;
    }

   
    public void setEmptyViewText(CharSequence charSequence)
    {
        mWaitEmptyViewHolder.mEmptyView.setText(charSequence);
    }


    public static class WaitEmptyViewHolder {
        private LinearLayout mRoot;
        private ProgressBar mProgressBar;
        private TextView mEmptyView;
        public WaitEmptyViewHolder(ViewGroup parent) {
           mRoot= (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_text_layout,parent,false);
           mProgressBar= (ProgressBar) mRoot.findViewById(R.id.progressBar);
           mEmptyView= (TextView) mRoot.findViewById(R.id.text);
        }
    }
}
