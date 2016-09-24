package learn.example.pile.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

/**
 * Created on 2016/8/18.
 */
public class GlideUtil {

    public static class CropSquareTransformation implements Transformation<Bitmap> {
        private BitmapPool mBitmapPool;
        private int mWidth;
        private int mHeight;

        public CropSquareTransformation(Context context) {
            this(Glide.get(context).getBitmapPool());
        }

        public CropSquareTransformation(BitmapPool pool) {
            this.mBitmapPool = pool;
        }

        @Override
        public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
            Bitmap source = resource.get();
            int size = Math.min(source.getWidth(), source.getHeight());

            mWidth = (source.getWidth() - size) / 2;
            mHeight = (source.getHeight() - size) / 2;

            Bitmap.Config config =
                    source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888;
            Bitmap bitmap = mBitmapPool.get(mWidth, mHeight, config);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(source, mWidth, mHeight, size, size);
            }

            return BitmapResource.obtain(bitmap, mBitmapPool);
        }

        @Override public String getId() {
            return "CropSquareTransformation(width=" + mWidth + ", height=" + mHeight + ")";
        }
    }


    public static class MatchWidthTransformation extends BitmapTransformation {
        int targetHeight;
        int targetWidth;
        boolean aspectRatio;

        public MatchWidthTransformation(Context context,int desireHeight) {
            super(context);
            targetHeight=desireHeight;
            targetWidth=new DeviceInfo((Activity) context).SCREEN_WIDTH;
            aspectRatio=false;
        }

        public MatchWidthTransformation(Context context) {
            super(context);
            aspectRatio=true;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            int width;
            int height;
            if (aspectRatio)
            {
                width=outWidth;
                int srcWidth=toTransform.getWidth();
                float scale=width/(float)srcWidth;
                height= (int) (toTransform.getHeight()*scale);
            }else {
                width=targetWidth;
                height=targetHeight;
            }
            Bitmap recycler=pool.get(width,height,toTransform.getConfig()==null? Bitmap.Config.ARGB_8888:toTransform.getConfig());
            return TransformationUtils.centerCrop(recycler,toTransform,width,height);
        }

        @Override public String getId() {
            return "matchwidthtransformation";
        }
    }


    public static class FitGifTransform extends BitmapTransformation{

        public FitGifTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            int srcWidth=toTransform.getWidth();
            int srcHeight=toTransform.getHeight();
            int minSize=200;
            int size=Math.max(Math.abs(srcWidth-srcHeight),minSize);

            int w=srcWidth+size;
            int h=srcHeight+size;
            Bitmap.Config config =
                    toTransform.getConfig() != null ? toTransform.getConfig() : Bitmap.Config.ARGB_8888;
            Bitmap bitmap = pool.get(w, h, config);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap( w, h,config);
            }
            Canvas canvas=new Canvas(bitmap);
            Rect dst=new Rect(0,0,w,h);
            canvas.drawBitmap(toTransform,null,dst,null);
            return bitmap;
        }

        @Override
        public String getId() {
            return "fit_gif_transform";
        }
    }



}
