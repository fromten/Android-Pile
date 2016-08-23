package learn.example.pile.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import learn.example.pile.util.DeviceInfo;
import uk.co.senab.photoview.PhotoView;

/**
 * Created on 2016/8/17.
 */
public class FitScalePhotoView extends PhotoView {

    public FitScalePhotoView(Context context) {
        super(context);
    }

    public FitScalePhotoView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        if (drawable!=null)
        {
            int width=getWidth()-getPaddingLeft()-getPaddingRight();
            float scale=width/drawable.getIntrinsicWidth();
            setMaximumScale(Math.max(DEFAULT_MAX_SCALE,scale));
        }
        super.setImageDrawable(drawable);
    }
}
