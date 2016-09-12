package learn.example.pile.video;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.exoplayer.util.PlayerControl;

/**
 * Created on 2016/9/6.
 */
public class ExoVideoView extends SurfaceView implements SurfaceHolder.Callback
                        ,ExoPlayer.Listener{

    public interface OnPlayInfoListener{
        void onPrepare(ExoVideoView view);
        void onCompleted(ExoVideoView view);
        void onError(ExoVideoView view, Exception error);
    }


    private static final String TAG = "ExoVideoView";


    private ExoPlayer mExoVideo;
    private ExoPlayer.RendererBuilder  mRendererBuilder;
    private SurfaceHolder mSurfaceHolder;
    private Uri mUri;

    private int videoWidth;
    private int videoHeight;


    private OnPlayInfoListener mPlayInfoListener;

    private boolean autoPlay;
    private boolean isInvokePrepareListener;



    public ExoVideoView(Context context) {
        this(context,null);
    }

    public ExoVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        autoPlay=true;
        videoWidth=-1;
        videoHeight=-1;
        isInvokePrepareListener=false;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        if (width>height)
        {   //在横屏情况下,充满整个父view
            setMeasuredDimension(width,height);
        }else if (videoHeight>0||videoWidth>0)
        {
            int desireWidth=resolveSize(videoWidth,widthMeasureSpec);
            int desireHeight=resolveSize(videoHeight,heightMeasureSpec);
            setMeasuredDimension(desireWidth,desireHeight);
        }else {
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }
    }
    public PlayerControl getPlayerControl(){
        return mExoVideo==null?null:mExoVideo.getPlayerControl();
    }

    /**
     * 是否默认播放,默认是true
     * @param autoPlay
     */
    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }


    public void setVideoUri(Uri uri)
    {
        mUri=uri;
        openVideo();
    }

    public void setBackgrounded(boolean backgrounded )
    {
      if (mExoVideo!=null)
      {
          mExoVideo.setBackgrounded(backgrounded);
      }
    }

    private void openVideo()
    {
        if (mUri==null||mSurfaceHolder==null)
        {
            return;
        }
        release();//释放之前的播放器
        mRendererBuilder=getRenderBuilder(mUri);
        mExoVideo=new ExoPlayer(mRendererBuilder);
        mExoVideo.setSurface(mSurfaceHolder.getSurface());
        mExoVideo.addListener(this);
        mExoVideo.setPlayWhenReady(autoPlay);
        mExoVideo.prepare();
    }

    public ExoPlayer.RendererBuilder getRenderBuilder(Uri uri){
        return new ExtractorRendererBuilder(getContext(),"exoplayer",uri);
    }

    public void release()
    {
        if (mExoVideo!=null)
        {
            mExoVideo.release();
            mExoVideo.removeListener(this);
            mExoVideo=null;
        }
    }

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState== ExoPlayer.STATE_READY&&!isInvokePrepareListener)
        {
            if (mPlayInfoListener!=null)
            {
                mPlayInfoListener.onPrepare(this);
            }
            isInvokePrepareListener=true;
        }

        if (playbackState== ExoPlayer.STATE_ENDED)
        {
            if (mPlayInfoListener!=null)
            {
                mPlayInfoListener.onCompleted(this);
            }
        }
    }

    @Override
    public void onError(Exception e) {
       if (mPlayInfoListener!=null)
       {
           mPlayInfoListener.onError(this,e);
       }
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        videoWidth=width;
        videoHeight=height;

        int viewWidth=getWidth()-getPaddingRight()-getPaddingLeft();
        int viewHeight=getHeight()-getPaddingTop()-getPaddingBottom();
        boolean isLandscape=viewWidth>viewHeight;

        int surfaceHeight=isLandscape?viewHeight:videoHeight;
        getHolder().setFixedSize(viewWidth,surfaceHeight);
        requestLayout();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder=holder;
        if (mExoVideo==null)
        {
            openVideo();
        }else {
            mExoVideo.setSurface(holder.getSurface());
            mExoVideo.setBackgrounded(false);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mExoVideo!=null)
        {
            mExoVideo.setBackgrounded(true);
        }
         mSurfaceHolder=null;
    }

    public OnPlayInfoListener getPlayInfoListener() {
        return mPlayInfoListener;
    }

    public void setPlayInfoListener(OnPlayInfoListener playInfoListener) {
        mPlayInfoListener = playInfoListener;
    }

}
