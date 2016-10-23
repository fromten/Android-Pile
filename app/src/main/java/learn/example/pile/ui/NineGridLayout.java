package learn.example.pile.ui;

import android.content.Context;
import android.graphics.Color;


import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import learn.example.pile.R;
import learn.example.pile.util.GlideUtil;

import static android.R.attr.type;

/**
 * Created on 2016/8/15.
 */
public class NineGridLayout extends ViewGroup implements View.OnClickListener{



    private int childWidth;
    private int childHeight;
    private int childMargin;

    private OnItemClickListener mItemClickListener;

    private GlideUtil.CropSquareTransformation mCropSquareTransformation;

    private String[] urls;



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

        mCropSquareTransformation=new GlideUtil.CropSquareTransformation(getContext());
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int width=MeasureSpec.getSize(widthSpec);
        int hPadding=getPaddingLeft()+getPaddingRight();
        width=width-hPadding;

        childWidth=width/COLUMN_COUNT;
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
        int vPadding=getPaddingTop()+getPaddingBottom();
        setMeasuredDimension(width,height+vPadding);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount=getChildCount();

        int pl=getPaddingLeft();
        int pt=getPaddingTop();

        for (int i=0;i<childCount;i++)
        {
            View child=getChildAt(i);

            int column=i%COLUMN_COUNT;
            int row=i/COLUMN_COUNT;

            if (childCount==4)//只有四个图片
            {
                //第一行,第三格移动到下一行
                if (column==2) {
                    column=0;
                    row=1;
                }else if (row==1)//第二行,第一格向右移
                {
                    column=1;
                }
            }


            int childLeft=column*childWidth;
            int childTop=row*childHeight;
            int childRight=childLeft+childWidth-childMargin;
            int childBottom=childTop+childHeight-childMargin;

            child.layout(childLeft+pl,childTop+pt,childRight,childBottom);
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
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(mCropSquareTransformation)
                    .into(image);
            type.setText(isGif[i]?" gif ":null);
        }
        int childCount=getChildCount();
        if (i < childCount)
            removeViews(i, Math.abs(childCount-urlLen));
    }


    /**
     * 创建child,并返回
     */
    public ViewGroup addChild()
    {
        FrameLayout frameLayout=new FrameLayout(getContext());
        frameLayout.setOnClickListener(this);

        //添加imageview
        frameLayout.addView(createImageView());

        //添加textView
        frameLayout.addView(createImageTypeView());


        this.addView(frameLayout);

        return frameLayout;
    }


    private ImageView createImageView()
    {
        ImageView imageView = new ImageView(getContext());
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setBackgroundResource(R.color.image_placer);
        return imageView;
    }

    private TextView createImageTypeView()
    {
        TextView type=new TextView(getContext(),null,R.style.SmallText);
        type.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.blue_light,null));
        type.setTextColor(Color.WHITE);
        FrameLayout.LayoutParams typeParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        typeParams.gravity= Gravity.BOTTOM |Gravity.END;
        type.setLayoutParams(typeParams);
        return type;
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
