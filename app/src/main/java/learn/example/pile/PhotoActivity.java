package learn.example.pile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.adapters.ViewPagerAdapter;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created on 2016/6/1.
 */
public class PhotoActivity extends AppCompatActivity implements ViewPagerAdapter.onViewShowListener{

    private PhotoView mImageView;
    private static final String TAG = "PhotoActivity";
    public static final String KEY_IMG_URL ="PhotoActivity_IMG_URL_KEY";
    private ProgressBar mProgressBar;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private String[] url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        mViewPager= (ViewPager) findViewById(R.id.view_pager);
        url=getIntent().getStringArrayExtra(KEY_IMG_URL);
        setViews();
    }

    private void setViews()
    {
        List<View> views=new ArrayList<>();
        for (String s:url)
        {
            PhotoView photoView=new PhotoView(this);
            photoView.setMinimumScale(0.5f);
            photoView.setOnViewTapListener(mOnViewTapListener);
            photoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            photoView.setScaleType(ImageView.ScaleType.CENTER);
            views.add(photoView);
        }
        mViewPagerAdapter=new ViewPagerAdapter(views);
        mViewPagerAdapter.setOnViewShowListener(this);
        mViewPager.setAdapter(mViewPagerAdapter);

    }

    @Override
    public void onBindView(View view,int position) {
        Glide.with(this).load(url[position])
                .dontAnimate()
                .error(R.mipmap.img_error)
                .fitCenter()
                .into((ImageView) view);
    }


    private PhotoViewAttacher.OnViewTapListener mOnViewTapListener=new PhotoViewAttacher.OnViewTapListener() {
        @Override
        public void onViewTap(View view, float v, float v1) {
            finish();
        }
    };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.anim_center_close);
    }
}
