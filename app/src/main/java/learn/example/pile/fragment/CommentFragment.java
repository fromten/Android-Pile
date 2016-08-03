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

    private TextView mEmptyTextView;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEnableSwipeLayout(false);
        mCommentListAdapter=new CommentListAdapter();
        setAdapter(mCommentListAdapter);

    }

    public void addComments(List<Comment> comments)
    {
        mCommentListAdapter.addAll(comments);
    }
    private View generateEmptyView()
    {
        TextView view =new TextView(getContext());
        view.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity=Gravity.CENTER;
        view.setLayoutParams(params);
        view.setClickable(false);
        view.setFocusable(false);
        return view;
    }

<<<<<<< HEAD
=======
    @Override
    public void onDestroy() {
        super.onDestroy();
    }




>>>>>>> master
}
