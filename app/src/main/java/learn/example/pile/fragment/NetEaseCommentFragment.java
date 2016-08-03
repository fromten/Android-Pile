package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.jsonbean.NetEaseComment;
import learn.example.pile.net.NetEaseNewsService;
import learn.example.pile.object.Comment;
import learn.example.pile.util.GsonHelper;

/**
 * Created on 2016/8/3.
 */
public class NetEaseCommentFragment extends CommentFragment implements NetEaseNewsService.Callback<NetEaseComment>{

    public static final String KEY_DOC_ID="docID";
    public static final String KEY_BOARD_ID="boardID";

    private String docId;
    private String boardId;

    private final int MAX_LEN=10;
    private int currentPos=0;

    private NetEaseNewsService mNetEaseNewsService;

    public static NetEaseCommentFragment newInstance(String docId,String boardID) {
        NetEaseCommentFragment fragment = new NetEaseCommentFragment();
        Log.d("instance", "newInstance() called with: " + "docId = [" + docId + "], boardID = [" + boardID + "]");
        fragment.docId=docId;
        fragment.boardId=boardID;
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("null", "onViewCreated");
        if (boardId==null&&docId==null)
        {
            return;
        }
        Log.d("tag", "onViewCreated");
        mNetEaseNewsService =new NetEaseNewsService();
        mNetEaseNewsService.getHotComment(boardId,docId,currentPos,10,this);
    }

    @Override
    public void onSuccess(NetEaseComment data) {
        addComments(new NetEaseCommentFactory().getCommentList(data));
        notifySuccess();
    }

    @Override
    public void onFailure(String message) {
        notifyError();
    }

    @Override
    public void onLoadMore() {
       currentPos+=MAX_LEN;
       mNetEaseNewsService.getNormalComment(boardId,docId,currentPos,MAX_LEN,this);
    }

    @Override
    public void onDestroy() {
        if (mNetEaseNewsService !=null)
        mNetEaseNewsService.cancelAll();
        super.onDestroy();
    }

    private static class NetEaseCommentFactory{

        public List<Comment> getCommentList(NetEaseComment netEaseComment ) {
            List<Comment> list = new ArrayList<>();
            JsonArray array = netEaseComment.getHotPosts() == null ? netEaseComment.getNewPosts() : netEaseComment.getHotPosts();
            if (array != null) {
                for (JsonElement item : array) {
                    try {
                        JsonObject object = item.getAsJsonObject();
                        Comment comment = getComment(object.getAsJsonObject("1"));
                        if (comment != null)
                            list.add(comment);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
            return list;
        }

        public Comment getComment(JsonObject netEaseCommen) {
            String author = GsonHelper.getAsString(netEaseCommen.get("n"), "网友");
            String time = GsonHelper.getAsString(netEaseCommen.get("t"), null);
            String content = GsonHelper.getAsString(netEaseCommen.get("b"), null);
            int like = GsonHelper.getAsInteger(netEaseCommen.get("v"), 0);
            String imgUrl = GsonHelper.getAsString(netEaseCommen.get("timg"), null);
            String address = GsonHelper.getAsString(netEaseCommen.get("f"), null);
            try {
                int index = address.indexOf("&nbsp");
                if (index > 0) {
                    address = address.substring(0, index);
                }
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                address = null;
            }
            return new Comment(author, like, time, imgUrl, address, content);
        }
    }



}
