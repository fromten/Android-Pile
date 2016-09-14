package learn.example.pile.activity.normal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import learn.example.net.OkHttpRequest;
import learn.example.pile.R;
import learn.example.pile.activity.base.FullScreenActivity;
import learn.example.pile.activity.base.SupportCommentActivity;
import learn.example.pile.adapters.NetEasePhotoAdapter;
import learn.example.pile.fragment.comment.NetEaseCommentFragment;
import learn.example.pile.object.NetEase;
import learn.example.pile.object.PhotosMessage;
import learn.example.pile.ui.PhotoWatcherLayout;
import learn.example.pile.util.GsonHelper;
import okhttp3.Call;
import okhttp3.Request;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created on 2016/6/1.
 */
public class PhotoActivity extends SupportCommentActivity {





    public static final String KEY_NETEASE_SKIPID ="key_netease_skipid";

    public static final String KEY_IMAGE_URLS ="muliti_image";
    public static final String KEY_SCROLL_TO_POSITION ="scroll_position";

    private ProgressBar mProgressBar;

    private PhotoWatcherLayout mPhotoWatcherLayout;

    private NetEasePhotoWatcher mNetEasePhotoWatcher;

    private PhotoViewAttacher.OnViewTapListener mPhotoViewTapListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        setTitle(null);
        mPhotoWatcherLayout= (PhotoWatcherLayout) findViewById(R.id.photo_layout);

        String skipId=getIntent().getStringExtra(KEY_NETEASE_SKIPID);
        String[] urlArray=getIntent().getStringArrayExtra(KEY_IMAGE_URLS);

        if (urlArray!=null)
        {
            initPhotoView(urlArray);
        }else if (skipId!=null){
            initNewsFragment(skipId);
        }
    }



    private void initPhotoView(String[] url)
    {
        PhotoWatcherLayout.SimplePhotoWatcherAdapter adapter=new PhotoWatcherLayout.SimplePhotoWatcherAdapter(url);
        mPhotoWatcherLayout.setAdapter(adapter);
        mPhotoViewTapListener= new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v1) {
                finish();
            }
        };
        mPhotoWatcherLayout.setOnViewTapListener(mPhotoViewTapListener);

        //滚动到请求的位置
        int position=getIntent().getIntExtra(KEY_SCROLL_TO_POSITION,0);
        if (position>0)
        {
            mPhotoWatcherLayout.getViewPager().setCurrentItem(position);
        }

        //手动调用,显示图片的位置
        if (getIntent().hasExtra(KEY_IMAGE_URLS))
        {
            mPhotoWatcherLayout.onPageSelected(position);
        }
    }

    private void initNewsFragment(final String skipID)
    {
        mNetEasePhotoWatcher=new NetEasePhotoWatcher(skipID);
    }

    @Override
    protected void onDestroy() {
        if (mNetEasePhotoWatcher!=null)
        {
            mNetEasePhotoWatcher.mCall.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.anim_center_close);
    }

    @Override
    protected int getReplaceId() {
        return R.id.root;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu.findItem(R.id.menu_comment)!=null)
        {  //菜单早已显示,不需要重新创建
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public class NetEasePhotoWatcher implements OkHttpRequest.RequestCallback<String>
    {

        private Call mCall;
        private int click=1;
        public NetEasePhotoWatcher(String skipID) {
            try {
                int line = skipID.indexOf("|");
                String first = skipID.substring(4, line);
                String second = skipID.substring(line + 1, skipID.length());
                Request request = new Request.Builder().url(String.format(NetEase.PHOTOS_SET_URL, first, second)).build();
                mCall = OkHttpRequest.getInstanceUnsafe().newStringRequest(request, this);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(String res) {
            JsonObject object = new JsonParser().parse(res).getAsJsonObject();
            List<PhotosMessage> list=getMessageList(object);
            if (list!=null)
            {
                NetEasePhotoAdapter adapter=new NetEasePhotoAdapter(list);
                mPhotoWatcherLayout.setAdapter(adapter);
                mPhotoViewTapListener=new PhotoViewAttacher.OnViewTapListener() {
                    @Override
                    public void onViewTap(View view, float v, float v1) {
                        if (click%2==0)
                        {
                            showActionBar();
                            mPhotoWatcherLayout.showDigestArea();
                        }else {
                            hideActionBar();
                            mPhotoWatcherLayout.hideDigestArea();
                        }
                        click++;
                    }
                };
                mPhotoWatcherLayout.setOnViewTapListener(mPhotoViewTapListener);
                //手动调用
                mPhotoWatcherLayout.onPageSelected(0);
            }
        }

        public List<PhotosMessage> getMessageList(JsonObject jsonObject)
        {
            setCommentMenu(jsonObject);
            JsonArray array = jsonObject.getAsJsonArray("photos");
            if (array != null) {
                List<PhotosMessage> mMessagesList=new ArrayList<>();
                String title = GsonHelper.getAsString(jsonObject.get("setname"), null);
                final int size = array.size();
                for (int i = 0; i < size; ++i) {
                    JsonObject o = (JsonObject) array.get(i);
                    String imgUrl = GsonHelper.getAsString(o.get("imgurl"), null);
                    String content = GsonHelper.getAsString(o.get("note"), null);
                    mMessagesList.add(new PhotosMessage(title,content,imgUrl));
                }
                return mMessagesList;
            }
            return null;
        }

        @Override
        public void onFailure(String msg) {

        }

        /**
         * 网易新闻图集新闻,和其他新闻不同,在请求结束后,手动添加评论
         * @param object
         */
        private void setCommentMenu(JsonObject object)
        {
            String postId=GsonHelper.getAsString(object.get("postid"),null);
            if (postId!=null)
            {
                Intent intent=getIntent();

                Bundle args=new Bundle();
                args.putString(SupportCommentActivity.KEY_FRAGMENT_CLASS_NAME, NetEaseCommentFragment.class.getName());
                args.putString(NetEaseCommentFragment.KEY_DOCID,postId);
                String board=GsonHelper.getAsString(object.get("boardid"),null);
                args.putString(NetEaseCommentFragment.KEY_BOARDID,board);

                intent.putExtra(SupportCommentActivity.KEY_APPLY_COMMENT,args);

                //可能网络请求慢,导致菜单不能创建,需要重新创建菜单
                invalidateOptionsMenu();
            }
        }

    }
}
