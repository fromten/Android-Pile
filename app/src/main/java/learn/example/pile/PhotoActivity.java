package learn.example.pile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import learn.example.pile.util.UrlCheck;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created on 2016/6/1.
 */
public class PhotoActivity extends AppCompatActivity{

    private PhotoView mImageView;
    private static final String TAG = "PhotoActivity";
    public static final String KEY_IMG_URL ="PhotoActivity_IMG_URL_KEY";
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        mImageView= (PhotoView) findViewById(R.id.photo);
        mProgressBar= (ProgressBar) findViewById(R.id.load_progress);
        mImageView.setMinimumScale(0.5f);

        mImageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v1) {
               finish();
            }
        });
            String url=getIntent().getStringExtra(KEY_IMG_URL);
            if (UrlCheck.isGifImg(url))
            {
                loadGif(url);
            }else {
                loadImage(url);
            }

    }
    public void loadGif(String url)
    {
        Glide.with(this).load(url)
                .asGif()
                .dontAnimate()
                .error(R.mipmap.img_error)
                .fitCenter()
                .into(mImageView);
//        mProgressBar.setVisibility(View.VISIBLE);
//        mPhotoProgressTask=new PhotoProgressTask(new PhotoProgressTask.ProgressListener() {
//            @Override
//            public void onProgressChanged(int value) {
//                mProgressBar.incrementProgressBy(value);
//            }
//
//            @Override
//            public void onComplete(byte[] bytes) {
//                 mProgressBar.setVisibility(View.GONE);
//                 Glide.with(PhotoActivity.this).load(bytes)
//                         .asGif()
//                         .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                         .dontAnimate().into(mImageView);
//            }
//        });
//        Request request=new Request.Builder().url(url).build();
//        mPhotoProgressTask.execute(request);

    }
    public void loadImage(String url)
    {
        Glide.with(this).load(url).asBitmap().fitCenter()
                .dontAnimate()
                .error(R.mipmap.img_error)
                .into(mImageView);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.anim_center_close);
    }
}
