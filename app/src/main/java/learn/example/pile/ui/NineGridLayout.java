package learn.example.pile.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import learn.example.pile.util.DeviceInfo;

/**
 * Created on 2016/8/15.
 */
public class NineGridLayout extends GridLayout implements View.OnClickListener{

    private int screenWidth;
    private int childWidth;
    private int childHeight;

    private OnItemClickListener mItemClickListener;

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
        childWidth=screenWidth/4;
        childHeight=childWidth+20;

        int paddingLeft=(screenWidth-(childWidth*getColumnCount()))/2-16;
        setPadding(paddingLeft,0,0,0);

    }

    public void updateViews(String[] urls)
    {
        final int urlLen=urls.length;
        int i;
        for (i = 0; i < urlLen; i++) {
            ImageView image = (ImageView) getChildAt(i);
            if (image == null) {
                image = addView(i);
                addView(image);
                image.setOnClickListener(this);
            }
            Glide.with(getContext()).load(urls[i]).asBitmap().centerCrop().into(image);
        }
        int childCount=getChildCount();
        if (i < childCount)
            removeViews(i, Math.abs(childCount-urlLen));
    }

    public ImageView addView(int position)
    {
        ImageView imageView = new ImageView(getContext());
        ViewGroup.MarginLayoutParams marginParams=new ViewGroup.MarginLayoutParams(childWidth,childHeight);
        marginParams.setMargins(0,8,position+1%3==0?0:8,8);//第三列不加MarginRight
        GridLayout.LayoutParams  params = new GridLayout.LayoutParams (marginParams);
        imageView.setLayoutParams(params);
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
