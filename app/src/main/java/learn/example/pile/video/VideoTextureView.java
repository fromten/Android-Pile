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

    private int videoWidth;
    private int videoHeight;
    private float videoWHRatio;
    private boolean aspectRatio;
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
        aspectRatio =true;
        isLoop=false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure() called with: " + "widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int measureWidth=getMeasuredWidth();
        int measureHeight=getMeasuredHeight();

        int w=measureWidth;
        int h=measureHeight;
        if (measureWidth<=0)
        {
           w=Math.max(getSuggestedMinimumWidth(),videoWidth);
        }
        if (measureHeight<=0)
        {
           h=Math.max(getSuggestedMinimumHeight(),videoHeight);
        }

        if (aspectRatio&&(videoHeight>0||videoWidth>0))
        {
            h= (int) (w*videoWHRatio);
        }
        setMeasuredDimension(w,h);
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
        Log.d(TAG, "openVideo invoke");
        if (mUri==null||getSurfaceTexture()==null)
        {
            return;
        }
        Log.d(TAG, "openVideo start set ");
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
        videoWidth=width;
        videoHeight=height;
        videoWHRatio=pixelWidthHeightRatio;
        requestLayout();
    }

    public void setLoop(boolean loop)
    {
        isLoop=loop;
    }

    public void setAspectRatio(boolean aspectRatio)
    {
        this.aspectRatio = aspectRatio;
    }

}

