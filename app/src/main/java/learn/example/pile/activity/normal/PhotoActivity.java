package learn.example.pile.activity.normal;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import learn.example.pile.adpter.NetEasePhotoAdapter;
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
public class PhotoActivity extends FullScreenActivity {



    public static final String KEY_IMG_URL ="photoactivity_img_url_key";
    public static final String KEY_MULITI_IMAGE ="muliti_image";
    public static final String KEY_NETEASE_SKIPID ="key_netease_skipid";
    public static final String KEY_SCROLL_POSITION="scroll_position";

    private ProgressBar mProgressBar;

    private PhotoWatcherLayout mPhotoWatcherLayout;

    private NetEasePhotoWatcher mNetEasePhotoWatcher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        mPhotoWatcherLayout= (PhotoWatcherLayout) findViewById(R.id.photo_layout);
        String url=getIntent().getStringExtra(KEY_IMG_URL);
        String skipId=getIntent().getStringExtra(KEY_NETEASE_SKIPID);
        String[] urlArray=getIntent().getStringArrayExtra(KEY_MULITI_IMAGE);
        if (url!=null)
        {
            initPhotoView(new String[]{url});
        }else if (urlArray!=null)
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
        mPhotoWatcherLayout.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v1) {
                finish();
            }
        });

        //滚动到请求的位置
        int position=getIntent().getIntExtra(KEY_SCROLL_POSITION,0);
        if (position>0)
        {
            mPhotoWatcherLayout.getViewPager().setCurrentItem(position);
        }

        //手动调用,显示图片的位置
        if (getIntent().hasExtra(KEY_MULITI_IMAGE))
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
                mPhotoWatcherLayout.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                    @Override
                    public void onViewTap(View view, float v, float v1) {
                        if (click%2==0)
                        {
                            mPhotoWatcherLayout.showDigestArea();
                        }else {
                            mPhotoWatcherLayout.hideDigestArea();
                        }
                        click++;
                    }
                });
                //手动调用
                mPhotoWatcherLayout.onPageSelected(0);
            }
        }

        public List<PhotosMessage> getMessageList(JsonObject jsonObject)
        {
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

    }
}
