package learn.example.pile.fragment.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.JsonObject;

import learn.example.pile.factory.CommentFactory;
import learn.example.pile.net.IService;
import learn.example.pile.net.OpenEyeService;
import learn.example.pile.pojo.Comment;
import learn.example.pile.util.gson.GsonHelper;

/**
 * Created on 2016/8/5.
 */
public class OpenEyeCommentFragment extends CommentFragment implements IService.Callback<String> {

    public static final String STATE_NEXT_PAGE_URL = "STATE_NEXT_PAGE_URL";
    public static final String ARGUMENT_VIDEO_ID = "ARGUMENT_VIDEO_ID";
    private OpenEyeService mOpenEyeService;
    private int id;
    private String nextPageUrl;

    public static OpenEyeCommentFragment newInstance(int videoId) {

        Bundle args = new Bundle();
        args.putInt(ARGUMENT_VIDEO_ID, videoId);
        OpenEyeCommentFragment fragment = new OpenEyeCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        super.onViewCreated(view, savedInstanceState);
        id = args.getInt(ARGUMENT_VIDEO_ID);
        mOpenEyeService = new OpenEyeService();
        if (savedInstanceState != null) {
            nextPageUrl = savedInstanceState.getString(STATE_NEXT_PAGE_URL);
        }

        if (nextPageUrl==null)
        {
            mOpenEyeService.getComments(id,this);
        }

    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_NEXT_PAGE_URL, nextPageUrl);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSuccess(String data) {
        Comment comment = CommentFactory.newInstance().produceOpenEyesComment(data);
        if (comment != null) {
            JsonObject object = comment.getExtraMsg();
            if (object != null) {
                nextPageUrl = GsonHelper.getAsString(object.get("nextPageUrl"), null);
            } else {
                nextPageUrl = null;
            }
            addComments(comment.getComments());
        }

        notifySuccess();
    }

    @Override
    public void onFailure(String message) {
        notifyError();
    }

    @Override
    public void onRefresh() {
        mOpenEyeService.getComments(id, this);
    }

    @Override
    public void onLoadMore() {
        if (nextPageUrl != null) {
            mOpenEyeService.nextCommentList(nextPageUrl, this);
        } else {
            notifyRequestEnd();
        }
    }

    @Override
    public void onDestroy() {
        mOpenEyeService.cancelAll();
        super.onDestroy();
    }

}
