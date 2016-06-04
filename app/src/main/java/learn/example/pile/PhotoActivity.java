package learn.example.pile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import learn.example.joke.R;

/**
 * Created on 2016/6/1.
 */
public class PhotoActivity extends AppCompatActivity{

    private ImageView mImageView;
    public static final String KEY_PHOTOACTIVITY_IMG_URL="PhotoActivity_IMG_URL_KEY";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        mImageView= (ImageView) findViewById(R.id.photo);
        Intent intent=getIntent();
        if (intent!=null)
        {
            String url=intent.getStringExtra(KEY_PHOTOACTIVITY_IMG_URL);
            Glide.with(this).load(url)
                    .dontAnimate()
                    .fitCenter()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.mipmap.img_error)
                    .into(mImageView);
        }
    }

}
