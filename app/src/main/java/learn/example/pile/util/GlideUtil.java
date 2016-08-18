package learn.example.pile.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;


import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

/**
 * Created on 2016/8/18.
 */
public class GlideUtil {


    public static Bitmap matchViewWidth(Bitmap toFit, BitmapPool pool, int viewWidth, int viewHeight) {
        int bitmapWidth = toFit.getWidth();
        int bitmapHeight = toFit.getHeight();

        if (bitmapWidth == viewWidth) {
            return toFit;
        }


        final float widthPercent = viewWidth / bitmapWidth;
        final int desireHeight = (int) (bitmapHeight * widthPercent);


        Bitmap.Config config = getSafeConfig(toFit);
        Bitmap toReuse = pool.get(viewWidth, desireHeight, config);
        if (toReuse == null) {
            toReuse = Bitmap.createBitmap(viewWidth, desireHeight, config);
        }

        TransformationUtils.setAlpha(toFit, toReuse);

        Canvas canvas = new Canvas(toReuse);
        Paint paint = new Paint(TransformationUtils.PAINT_FLAGS);
        Rect rect = new Rect(0, 0, viewWidth, desireHeight);
        canvas.drawBitmap(toFit, null, rect, paint);
        return toReuse;
    }

    private static Bitmap.Config getSafeConfig(Bitmap toFit) {
        return toFit.getConfig() == null ? Bitmap.Config.ARGB_8888 : toFit.getConfig();
    }

    public static Bitmap matchView(Bitmap toFit, BitmapPool pool, int viewWidth, int viewHeight) {
        int bitmapWidth = toFit.getWidth();
        int bitmapHeight = toFit.getHeight();

        if (bitmapWidth == viewWidth&&bitmapHeight==viewHeight) {
            return toFit;
        }

        final float widthPercent =bitmapWidth/viewWidth;
        final float heightPercent= bitmapHeight/viewHeight;

        final float mixPercent=Math.max(widthPercent,heightPercent);


        Bitmap.Config config = getSafeConfig(toFit);
        Bitmap toReuse = pool.get(viewWidth, viewHeight, config);
        if (toReuse == null) {
            toReuse = Bitmap.createBitmap(viewWidth, viewHeight, config);
        }

        TransformationUtils.setAlpha(toFit, toReuse);

        Canvas canvas = new Canvas(toReuse);
        Paint paint = new Paint(TransformationUtils.PAINT_FLAGS);
        Matrix matrix=new Matrix();
        matrix.setScale(mixPercent,mixPercent);
        canvas.drawBitmap(toFit,matrix, paint);
        return toReuse;
    }


}
