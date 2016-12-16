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
        if (mWaitEmptyViewHolder==null||mWaitEmptyViewHolder.mRoot.getVisibility()==View.INVISIBLE)
        {
            return;
        }
        mWaitEmptyViewHolder.mTextView.setText("请求失败点击重试");
        mWaitEmptyViewHolder.mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果长度小于等于7,表示当前的文本为 "重新加载..."(防止重复点击)
                if (mWaitEmptyViewHolder.mTextView.getText().length()<=7)
                {
                    return;
                }
                mWaitEmptyViewHolder.mTextView.setText("重新加载...");
                onRefresh();
            }
        });
    }

    @Override
    protected void handleRequestSuccess() {
        super.handleRequestSuccess();
        View view=getView();
        if (view!=null&&mWaitEmptyViewHolder!=null)
        {
            ((ViewGroup)view).removeViewInLayout(mWaitEmptyViewHolder.mRoot);
            mWaitEmptyViewHolder=null;
        }
    }

    /**
     * 通知没有可获得的评论
     * 调用此方法后应该不再调用 notifySuccess()或notifyError() 方法
     */
    public void notifyNonComment()
    {
        if (mWaitEmptyViewHolder!=null)
        {
            mWaitEmptyViewHolder.showFailImageWithText("暂时没有评论");
        }
        disableLoadMore();
    }

    public static class WaitEmptyViewHolder {
        private LinearLayout mRoot;
        private ProgressBar mProgressBar;
        private TextView mTextView;
        public WaitEmptyViewHolder(ViewGroup parent) {
           mRoot= (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_text_layout,parent,false);
           mProgressBar= (ProgressBar) mRoot.findViewById(R.id.progressBar);
           mTextView= (TextView) mRoot.findViewById(R.id.text);
        }

        public void showFailImageWithText(CharSequence charSequence)
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            mTextView.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.emoticon_sad,0,0);
            mTextView.setText(charSequence);
        }
    }
}
