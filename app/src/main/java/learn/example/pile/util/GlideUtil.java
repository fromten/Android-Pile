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
            return "CropSquareTransformation";
        }
    }


    public static class MatchTransformation extends BitmapTransformation {
        private int targetHeight;
        private int targetWidth;
        private boolean aspectRatio;
        private Canvas mCanvas=new Canvas();
        private Rect mDstRect=new Rect();

        public MatchTransformation(Context context,int desireWidth, int desireHeight) {
            super(context);
            targetWidth=desireWidth;
            targetHeight=desireHeight;
            aspectRatio=false;
        }

        public MatchTransformation(Context context) {
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
            //Bitmap recycler=pool.get(width,height,toTransform.getConfig()==null? Bitmap.Config.ARGB_8888:toTransform.getConfig());
           // return TransformationUtils.centerCrop(recycler,toTransform,width,height);
            Bitmap.Config config =
                    toTransform.getConfig() != null ? toTransform.getConfig() : Bitmap.Config.ARGB_8888;
            Bitmap bitmap = pool.get(width, height, config);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(width,height,config);
            }
            mCanvas.setBitmap(bitmap);
            mDstRect.set(0,0,width,height);
            mCanvas.drawBitmap(toTransform,null,mDstRect,null);
            return bitmap;
        }

        @Override public String getId() {
            return "match_transformation";
        }
    }


    public static class FitGifTransform extends BitmapTransformation{
        private Canvas mCanvas;
        private Rect mRect;

        public FitGifTransform(Context context) {
            super(context);
            mCanvas=new Canvas();
            mRect=new Rect();
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
            mCanvas.setBitmap(bitmap);
            mRect.set(0,0,w,h);
            mCanvas.drawBitmap(toTransform,null,mRect,null);
            return bitmap;
        }

        @Override
        public String getId() {
            return "fit_gif_transform";
        }
    }



}
