package learn.example.pile.ui;



import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created on 2016/8/12.
 */
public class CircleViewTarget extends BitmapImageViewTarget {

    public CircleViewTarget(ImageView view) {
        super(view);
    }

    @Override
    protected void setResource(Bitmap resource) {
        RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(getView().getResources(), resource);
        circularBitmapDrawable.setCircular(true);
        setDrawable(circularBitmapDrawable);
    }
}
