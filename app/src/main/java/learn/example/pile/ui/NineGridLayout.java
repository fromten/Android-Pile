package learn.example.pile.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import jp.wasabeef.glide.transformations.CropSquareTransformation;
import learn.example.pile.R;
import learn.example.pile.util.DeviceInfo;
import learn.example.pile.util.GlideUtil;

/**
 * Created on 2016/8/15.
 */
public class NineGridLayout extends ViewGroup implements View.OnClickListener{


    private static final String TAG = "NineGridLayout";
    private int screenWidth;
    private int childWidth;
    private int childHeight;
    private int childMargin;

    private OnItemClickListener mItemClickListener;

    private CropSquareTransformation mCropSquareTransformation;

    private String[] urls;
    private boolean[] isGif;


    private static final int ROW_COUNT=3;
    private static final int COLUMN_COUNT=3;

    public NineGridLayout(Context context) {
        this(context,null);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        init();
    }

    private void init()
    {

        mCropSquareTransformation=new CropSquareTransformation(getContext());
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int width=MeasureSpec.getSize(widthSpec);

        childWidth=width/3;
        childMargin=childWidth/40;

        childHeight=childWidth+20;


        int row=(getChildCount()-1)/ROW_COUNT;
        int height=childHeight+(row*childHeight);


        int childCount=getChildCount();
        for (int i=0;i<childCount;i++)
        {
            int childWidthSpec= MeasureSpec.makeMeasureSpec(childWidth,MeasureSpec.EXACTLY);
            int childHeightSpec= MeasureSpec.makeMeasureSpec(childHeight,MeasureSpec.EXACTLY);
            getChildAt(i).measure(childWidthSpec,childHeightSpec);
        }


        int hPadding=getPaddingLeft()+getPaddingRight();
        int vPadding=getPaddingTop()+getPaddingBottom();
        setMeasuredDimension(width+hPadding,height+vPadding);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount=getChildCount();


        int pl=getPaddingLeft();
        int pr=getPaddingRight();
        int pt=getPaddingTop();
        int pb=getPaddingBottom();
        for (int i=0;i<childCount;i++)
        {
            View child=getChildAt(i);

            int column=i%COLUMN_COUNT;
            int row=i/COLUMN_COUNT;

            int childLeft=column*childWidth+childMargin;
            int childTop=row*childHeight+childMargin;
            int childRight=childLeft+childWidth-childMargin;
            int childBottom=childTop+childHeight-childMargin;

            child.layout(childLeft+pl,childTop+pt,childRight-pr,childBottom-pb);
        }
    }

    public void updateViews(String[] urls, boolean[] isGif)
    {
        if (urls==null||isGif==null||urls.length!=isGif.length)
        {
            return;
        }
        this.urls=urls;

        final int urlLen=urls.length;
        int i;
        for (i = 0; i < urlLen; i++) {
            ViewGroup viewGroup= (ViewGroup) getChildAt(i);

            ImageView image;
            TextView type;
            if (viewGroup == null) {
                viewGroup=addChild();
            }
            image = (ImageView) viewGroup.getChildAt(0);
            type= (TextView) viewGroup.getChildAt(1);
            Glide.with(getContext())
                    .load(urls[i])
                    .asBitmap()
                    .transform(mCropSquareTransformation)
                    .into(image);
            type.setText(isGif[i]?" gif ":null);
        }
        int childCount=getChildCount();
        if (i < childCount)
            removeViews(i, Math.abs(childCount-urlLen));
    }

    public ViewGroup addChild()
    {
        FrameLayout frameLayout=new FrameLayout(getContext());
        frameLayout.setOnClickListener(this);

        //添加imageview
        frameLayout.addView(createImageView());

        TextView type=new TextView(getContext(),null,R.style.SmallText);
        type.setBackgroundColor(getResources().getColor(R.color.blue_light));
        type.setTextColor(Color.WHITE);
        FrameLayout.LayoutParams typeParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        typeParams.gravity= Gravity.BOTTOM |Gravity.END;

        //添加textView
        frameLayout.addView(type,typeParams);


        this.addView(frameLayout);
     //   frameLayout.setPadding(0,0,childMargin,childMargin);

        return frameLayout;
    }


    private ImageView createImageView()
    {
        ImageView imageView = new ImageView(getContext());
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
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
            mItemClickListener.onItemClick(v,this,index,urls);
        }
    }


    public OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,ViewGroup parent,int position,String[] url);
    }
}
