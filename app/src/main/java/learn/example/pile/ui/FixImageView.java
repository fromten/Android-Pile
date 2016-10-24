package learn.example.pile.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 强制设置View大小等于Drawable大小
 * 目的减小onMeasure()方法计算
 * Created on 2016/10/16.
 */

public class FixImageView extends ImageView {

    public FixImageView(Context context) {
        super(context);
    }

    public FixImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable=getDrawable();
        if (drawable!=null)
        {
            int width=drawable.getIntrinsicWidth();
            int height=drawable.getIntrinsicHeight();
            setMeasuredDimension(width,height);
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
