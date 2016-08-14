package learn.example.pile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import learn.example.pile.adapters.CommentListAdapter;
import learn.example.pile.object.Comment;


/**
 * Created on 2016/7/13.
 */
public class CommentFragment extends BaseListFragment {

    private CommentListAdapter mCommentListAdapter;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDisEnableRefresh(true);
        mCommentListAdapter=new CommentListAdapter();
        setAdapter(mCommentListAdapter);
    }

    public void addComments(List<Comment> comments)
    {
        mCommentListAdapter.addAll(comments);
    }

    @Override
    protected void addTopView() {

    }
}
