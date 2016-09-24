package learn.example.pile.video;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.google.android.exoplayer.util.PlayerControl;


/**
 * Created on 2016/9/10.
 */
public class VideoTextureView extends TextureView implements TextureView.SurfaceTextureListener,
                                ExoPlayer.Listener{

    private static final String TAG = "ExoVideoView";
    private ExoPlayer mExoVideo;
    private Uri mUri;

    private int mVideoWidth;
    private int mVideoHeight;
    private float mVideoWHRatio;
    private int mMaxWidth=-1;
    private int mMaxHeight=-1;


    private boolean isLoop;

    public VideoTextureView(Context context) {
        this(context,null);
    }

    public VideoTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        super.setSurfaceTextureListener(this);
        isLoop=false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure() called with: " + "widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        if (mVideoWidth > 0 && mVideoHeight > 0) {

            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;

                // for compatibility, we adjust size based on aspect ratio
                if ( mVideoWidth * height  < width * mVideoHeight ) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if ( mVideoWidth * height  > width * mVideoHeight ) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize;
                height = width * mVideoHeight / mVideoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize;
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize;
                width = height * mVideoWidth / mVideoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize;
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = mVideoWidth;
                height = mVideoHeight;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize;
                    width = height * mVideoWidth / mVideoHeight;
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize;
                    height = width * mVideoHeight / mVideoWidth;
                }
            }
        } else {
            width=Math.max(getSuggestedMinimumWidth(),width);
            height=Math.max(getSuggestedMinimumHeight(),height);
        }
        if (mMaxHeight>=0&&height>mMaxHeight)
        {
            height=mMaxHeight;
        }
        if (mMaxWidth>=0&&width>mMaxWidth)
        {
            width=mMaxWidth;
        }

        setMeasuredDimension(width, height);
    }

    public void release() {
        if (mExoVideo!=null)
        {
            mExoVideo.release();
            mExoVideo=null;
        }
    }

    public PlayerControl getController()
    {
        return mExoVideo==null?null:mExoVideo.getPlayerControl();
    }


    public void setDataSource(String path) {
         mUri=Uri.parse(path);
         openVideo();
    }


    private void openVideo()
    {

        if (mUri==null||getSurfaceTexture()==null)
        {
            return;
        }

        release();
        ExtractorRendererBuilder builder=new ExtractorRendererBuilder(getContext(),"test",mUri);
        mExoVideo=new ExoPlayer(builder);
        mExoVideo.setPlayWhenReady(true);
        mExoVideo.addListener(this);
        mExoVideo.setSurface(new Surface(getSurfaceTexture()));
        mExoVideo.prepare();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Log.d(TAG, "onSurfaceTextureAvailable");
        if (mExoVideo==null)
        {
            openVideo();
        }else {
            mExoVideo.setSurface(new Surface(surfaceTexture));
            mExoVideo.setBackgrounded(false);
        }

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "onSurfaceTextureSizeChanged");
    }


    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.d(TAG, "onSurfaceTextureDestroyed");
        if (mExoVideo!=null)
        {
            mExoVideo.setBackgrounded(true);
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    protected void onDetachedFromWindow() {
        release();
        super.onDetachedFromWindow();
    }

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState==ExoPlayer.STATE_ENDED&&isLoop)
        {
            if (mExoVideo!=null)
            {
                mExoVideo.getPlayerControl().seekTo(0);
            }
        }
    }

    @Override
    public void onError(Exception e) {

    }



    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Log.d(TAG, "onVideoSizeChanged");
        mVideoWidth =width;
        mVideoHeight =height;
        mVideoWHRatio =pixelWidthHeightRatio;
        requestLayout();
    }

    public void setLoop(boolean loop)
    {
        isLoop=loop;
    }

    public void setMaxWidth(int maxWidth)
    {
        mMaxWidth=maxWidth;
    }

    public void setMaxHeight(int maxHeight)
    {
        mMaxHeight=maxHeight;
    }



}

