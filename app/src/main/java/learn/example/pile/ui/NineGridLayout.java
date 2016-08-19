package learn.example.pile.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import jp.wasabeef.glide.transformations.CropSquareTransformation;
import learn.example.pile.util.DeviceInfo;
import learn.example.pile.util.GlideUtil;

/**
 * Created on 2016/8/15.
 */
public class NineGridLayout extends GridLayout implements View.OnClickListener{

    private int screenWidth;
    private int childWidth;
    private int childHeight;
    private int childMargin;

    private OnItemClickListener mItemClickListener;

    private CropSquareTransformation mCropSquareTransformation;
    public NineGridLayout(Context context) {
        this(context,null);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init()
    {
        screenWidth=new DeviceInfo((Activity) getContext()).SCREEN_WIDTH;


        childWidth=screenWidth/3;
        childHeight=childWidth+20;
        childMargin=childWidth/40;
        int columnCount=getColumnCount();
        int paddingLeft=(screenWidth-(childWidth*columnCount))/2-columnCount*childMargin;
        setPadding(paddingLeft,0,0,0);

        mCropSquareTransformation=new CropSquareTransformation(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void updateViews(String[] urls)
    {
        final int urlLen=urls.length;
        int i;
        for (i = 0; i < urlLen; i++) {
            ImageView image = (ImageView) getChildAt(i);
            if (image == null) {
                image = createView(i);
                addView(image);
                image.setOnClickListener(this);
            }
            Glide.with(getContext())
                    .load(urls[i])
                    .asBitmap()
                    .transform(mCropSquareTransformation)
                    .into(image);
        }
        int childCount=getChildCount();
        if (i < childCount)
            removeViews(i, Math.abs(childCount-urlLen));
    }

    public ImageView createView(int position)
    {
        ImageView imageView = new ImageView(getContext());
        ViewGroup.MarginLayoutParams marginParams=new ViewGroup.MarginLayoutParams(childWidth,childHeight);
        marginParams.setMargins(0,0,childMargin,childMargin);
        GridLayout.LayoutParams  params = new GridLayout.LayoutParams (marginParams);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setBackgroundColor(Color.GRAY);
        return imageView;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null)
        {
            int index=indexOfChild(v);
            mItemClickListener.onItemClick(v,this,index);
        }
    }


    public OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,ViewGroup parent,int position);
    }
}
