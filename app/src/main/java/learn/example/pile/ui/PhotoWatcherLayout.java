package learn.example.pile.ui;

import android.content.Context;

import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.R;
import learn.example.pile.adapters.ViewPagerAdapter;
import learn.example.pile.util.DeviceInfo;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created on 2016/8/16.
 */
public class PhotoWatcherLayout extends FrameLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private RelativeLayout mRelativeLayout;
    private boolean isWifiConnected;

    private TextView title;
    private TextView page;
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
        page = (TextView) findViewById(R.id.page);
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
                isWifiConnected=DeviceInfo.ifWifiConnected(getContext());
                if (isWifiConnected)//wifi可用
                {
                    loadImageIntoView(imageView,url);
                }
            }
            mViewPagerAdapter=new ViewPagerAdapter(viewList);
            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.addOnPageChangeListener(this);
        }
    }

    public void loadImageIntoView(ImageView imageView,String url)
    {

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
                        photoView.setScaleLevels(widthPercent*0.5f,widthPercent*1f,widthPercent*2f);
                        photoView.setScale(widthPercent,0,0,false);//缩放匹配view宽度
                    }
                });
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mPhotoWatcherAdapter.onPageScrolled(position,positionOffset,positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        if (!isWifiConnected)//wifi不可用,需要选择Page时再加载图片
        {
             String url=mPhotoWatcherAdapter.getUrl(position);
             loadImageIntoView((ImageView) mViewPagerAdapter.getView(position),url);
        }
        //防止监听失效
        ((PhotoView) mViewPagerAdapter.getView(position)).setOnViewTapListener(mOnViewTapListener);

        //回调
        mPhotoWatcherAdapter.onPageSelected(position,this);
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
     * @see #hideSummaryArea()
     */
    public void showSummaryArea()
    {
        if (mRelativeLayout.getVisibility() != View.VISIBLE) {
            mRelativeLayout.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 隐藏底部内容区域,
     * @see #mRelativeLayout
     * @see #hideSummaryArea()
     */
    public void hideSummaryArea()
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
         * @param viewGroud view容器
         */
        void onPageSelected(int position,PhotoWatcherLayout viewGroud);

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
        public void onPageSelected(int position, PhotoWatcherLayout viewGroud) {
            String str=position+1+"/"+getItemCount();

            TextView textView= (TextView) viewGroud.findViewById(R.id.page);
            textView.setText(str);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
