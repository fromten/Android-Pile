package learn.example.pile.activity.normal;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import learn.example.pile.R;
import learn.example.pile.activity.base.FullScreenActivity;
import learn.example.pile.fragment.PhotosWatcherFragment;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created on 2016/6/1.
 */
public class PhotoActivity extends FullScreenActivity {


    private static final String TAG = "PhotoActivity";
    public static final String KEY_IMG_URL ="photoactivity_img_url_key";
    public static final String KEY_NETEASE_SKIPID ="key_netease_skipid";

    private ProgressBar mProgressBar;

    private PhotoView mPhotoView;

    private PhotosWatcherFragment mNetEasePhotoWatcher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        String url=getIntent().getStringExtra(KEY_IMG_URL);
        String skipId=getIntent().getStringExtra(KEY_NETEASE_SKIPID);
        if (url!=null)
        {
            initPhotoView(url);
        }else if (skipId!=null){
            initNewsFragment(skipId);
        }
    }

    private void initPhotoView(String url)
    {

        mPhotoView= (PhotoView) findViewById(R.id.photo_view);
        mPhotoView.setMinimumScale(0.5f);
        mPhotoView.setOnViewTapListener(mOnViewTapListener);
        Glide.with(this).load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .error(R.mipmap.img_error)
                .fitCenter()
                .into(mPhotoView);
    }

    private void initNewsFragment(final String skipID)
    {
        mNetEasePhotoWatcher=new PhotosWatcherFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root,mNetEasePhotoWatcher).commit();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                 mNetEasePhotoWatcher.setContent(skipID);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.anim_center_close);
    }
    private PhotoViewAttacher.OnViewTapListener mOnViewTapListener=new PhotoViewAttacher.OnViewTapListener() {
        @Override
        public void onViewTap(View view, float v, float v1) {
            finish();
        }
    };
}
