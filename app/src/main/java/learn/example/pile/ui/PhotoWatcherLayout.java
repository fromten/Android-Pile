package learn.example.pile.ui;

import android.content.Context;

import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.transcode.BitmapToGlideDrawableTranscoder;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.R;
import learn.example.pile.adapters.ViewPagerAdapter;
import learn.example.pile.util.GlideUtil;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created on 2016/8/16.
 */
public class PhotoWatcherLayout extends FrameLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private RelativeLayout mRelativeLayout;

    private TextView title;
    private TextView number;
    private TextView content;


    private PhotoViewAttacher.OnViewTapListener mOnViewTapListener;
    private PhotoWatcherAdapter mPhotoWatcherAdapter;



    public PhotoWatcherLayout(Context context) {
        this(context,null);
    }

    public PhotoWatcherLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init()
    {
        inflate(getContext(), R.layout.photo_watcher_layout,this);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mRelativeLayout= (RelativeLayout) findViewById(R.id.container);
        title= (TextView) findViewById(R.id.title);
        number= (TextView) findViewById(R.id.number);
        content= (TextView)findViewById(R.id.content);

    }


    public void setAdapter(PhotoWatcherAdapter adapter)
    {
        if (adapter==null)
        {
            throw new NullPointerException("Adapter may be null");
        }
        mPhotoWatcherAdapter=adapter;
        int count=adapter.getItemCount();
        if (count>0)
        {
            List<View> viewList=new ArrayList<>();
            for (int i = 0; i < count; i++) {
                PhotoView imageView= createPhotoView();
                viewList.add(imageView);
                String url=adapter.getUrl(i);
                Glide.with(getContext())
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.mipmap.ic_defplacer)
                        .dontAnimate()
                        .dontTransform()
                        .into(new GlideDrawableImageViewTarget(imageView)
                        {
                            @Override
                            protected void setResource(GlideDrawable resource) {

                                PhotoView photoView= (PhotoView) this.getView();
                                photoView.setImageDrawable(resource);

                                RectF rectF=photoView.getDisplayRect();
                                int vwidth=photoView.getWidth();
                                float widthPercent=(float)vwidth/rectF.width();
                                photoView.setScaleLevels(widthPercent*0.5f,widthPercent*1,widthPercent*2);
                                photoView.setScale(widthPercent,0,0,false);//缩放匹配view宽度
                            }
                        });

            }
            mViewPagerAdapter=new ViewPagerAdapter(viewList);
            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.addOnPageChangeListener(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mPhotoWatcherAdapter.onPageScrolled(position,positionOffset,positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mPhotoWatcherAdapter.onPageSelected(position,title,content,number);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mPhotoWatcherAdapter.onPageScrollStateChanged(state);
    }

    public PhotoView createPhotoView(){
        PhotoView photoView = new PhotoView(getContext());
        photoView.setMinimumScale(0.5f);
        return photoView;
    }

    public PhotoViewAttacher.OnViewTapListener getOnViewTapListener() {
        return mOnViewTapListener;
    }

    public void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener onViewTapListener) {
         mOnViewTapListener = onViewTapListener;
         int count=mViewPagerAdapter.getCount();
         for (int i = 0; i < count; i++) {
            ((PhotoView)mViewPagerAdapter.getView(i)).setOnViewTapListener(onViewTapListener);
        }
    }


    /**
     * 显示底部内容区域,
     * @see #mRelativeLayout
     * @see #hideDigestArea()
     */
    public void showDigestArea()
    {
        if (mRelativeLayout.getVisibility() != View.VISIBLE) {
            mRelativeLayout.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 隐藏底部内容区域,
     * @see #mRelativeLayout
     * @see #hideDigestArea()
     */
    public void hideDigestArea()
    {
        if (mRelativeLayout.getVisibility() != View.INVISIBLE) {
            mRelativeLayout.setVisibility(View.INVISIBLE);
        }
    }


    public ViewPager getViewPager() {
        return mViewPager;
    }

    public interface PhotoWatcherAdapter {
        int getItemCount();

        /**
         * 如果getItemCount()>0,将会循环调用
         * @param position 现在位置
         * @return 图片URL地址
         */
        String getUrl(int position);

        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);


        /**
         * ViewPager选择时调用
         * @param position 现在位置
         * @param title 标题
         * @param content 内容
         * @param order 顺序
         */
        void onPageSelected(int position,TextView title,TextView content,TextView order);

        void onPageScrollStateChanged(int state);
    }

    public static class SimplePhotoWatcherAdapter implements PhotoWatcherAdapter{
        String[] urls;

        public SimplePhotoWatcherAdapter(String urls[]) {
            this.urls=urls;
        }

        @Override
        public int getItemCount() {
            return urls==null||urls.length==0?0:urls.length;
        }

        @Override
        public String getUrl(int position) {
            return urls[position];
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position, TextView title, TextView content, TextView order) {
            String str=position+1+"/"+getItemCount();
            order.setText(str);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
