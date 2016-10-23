package learn.example.pile.fragment.comment;


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
import learn.example.pile.pojo.Comment;


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

    @Override
    public boolean isSupportTopView() {
        return false;
    }

    public final void addComments(List<Comment.CommentItem> comments)
    {
        mCommentListAdapter.addAll(comments);
    }


    @Override
    public final View createEmptyView() {
       mWaitEmptyViewHolder=new WaitEmptyViewHolder((ViewGroup) getView());
       return mWaitEmptyViewHolder.mRoot;
    }

    @Override
    protected void handleRequestError() {
        super.handleRequestError();

        //当前不可见时,意味着当前adapter数据不为空,忽略处理
        if (mWaitEmptyViewHolder.mRoot.getVisibility()==View.INVISIBLE)
        {
            return;
        }
        mWaitEmptyViewHolder.mEmptyView.setText("请求失败点击重试");
        mWaitEmptyViewHolder.mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果长度小于等于7,表示当前的文本为 "重新加载..."
                if (mWaitEmptyViewHolder.mEmptyView.getText().length()<=7)
                {
                    return;
                }
                mWaitEmptyViewHolder.mEmptyView.setText("重新加载...");
                onRefresh();
            }
        });
    }

    @Override
    protected void handleRequestSuccess() {
        super.handleRequestSuccess();
        mWaitEmptyViewHolder.mEmptyView.setText(null);
        mWaitEmptyViewHolder.mRoot.setOnClickListener(null);
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
