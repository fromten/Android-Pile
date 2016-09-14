package learn.example.pile.video;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
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

    private int mVideoWidth;
    private int mVideoHeight;


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
        mVideoWidth =0;
        mVideoHeight =0;
        isInvokePrepareListener=false;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
            // no size yet, just adopt the given spec sizes
        }
        setMeasuredDimension(width, height);
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
            isInvokePrepareListener=false;
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
        release();
       if (mPlayInfoListener!=null)
       {
           mPlayInfoListener.onError(this,e);
       }
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
//        mVideoWidth =width;
//        mVideoHeight =height;
//
//        int viewWidth=getWidth()-getPaddingRight()-getPaddingLeft();
//        int viewHeight=getHeight()-getPaddingTop()-getPaddingBottom();
//        boolean isLandscape=viewWidth>viewHeight;
//
//        int surfaceHeight=isLandscape?viewHeight: mVideoHeight;
//        getHolder().setFixedSize(viewWidth,surfaceHeight);
//        requestLayout();

        mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            getHolder().setFixedSize(mVideoWidth, mVideoHeight);
            requestLayout();
        }
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
