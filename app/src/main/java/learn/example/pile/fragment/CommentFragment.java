package learn.example.pile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import learn.example.pile.adapters.CommentListAdapter;
import learn.example.pile.jsonbean.NetEaseComment;
import learn.example.pile.jsonbean.ZhihuComment;
import learn.example.pile.net.IService;
import learn.example.pile.net.NetEaseNewsService;
import learn.example.pile.net.ZhihuContentService;
import learn.example.pile.object.Comment;
import learn.example.uidesign.DividerItemDecoration;

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

}
