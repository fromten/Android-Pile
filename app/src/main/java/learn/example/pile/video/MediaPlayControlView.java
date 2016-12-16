package learn.example.pile.video;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Formatter;
import java.util.Locale;

import learn.example.pile.R;

/**
 * Created on 2016/9/7.
 */
public class MediaPlayControlView extends FrameLayout{


    private static final int DEF_SHOW_TIMEOUT=3000;
    private static final int HIDE_VIEW = 1;
    private static final int SHOW_PROGRESS = 2;
    private boolean mDragging;

    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    public View mRoot;
    private ImageButton mPauseButton;
    private SeekBar mSeekBar;
    private TextView mCurrentTime;
    private TextView mEndTime;
    private ExoPlayer mPlayer;
    private ComponentListener mComponentListener;

    public MediaPlayControlView(Context context) {
        this(context,null);
    }

    public MediaPlayControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init()
    {
        mRoot=LayoutInflater.from(getContext()).inflate(R.layout.video_controler_view,this);
        mSeekBar= (SeekBar) mRoot.findViewById(R.id.progress);
        mPauseButton= (ImageButton) mRoot.findViewById(R.id.pause);
        mCurrentTime= (TextView) mRoot.findViewById(R.id.time_current);
        mEndTime = (TextView) mRoot.findViewById(R.id.time);

        mSeekBar.setMax(1000);
        mCurrentTime.setText("00:00");
        mEndTime.setText("00:00");

        mComponentListener=new ComponentListener();
        mSeekBar.setOnSeekBarChangeListener(mComponentListener);
        mPauseButton.setOnClickListener(mComponentListener);

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mDragging=false;
    }

    public void show(int timeOut)
    {
        if (timeOut<0)
        {
            timeOut=DEF_SHOW_TIMEOUT;
        }
        if (!isShown())
        {
           setVisibility(VISIBLE);
        }
        if (mPlayer!=null) mHandler.sendEmptyMessage(SHOW_PROGRESS);
        mHandler.removeMessages(HIDE_VIEW);
        mHandler.sendEmptyMessageDelayed(HIDE_VIEW,timeOut);
    }

    public void hide()
    {
        if (isShown())
        {
            this.setVisibility(INVISIBLE);
            mDragging=false;
        }
    }


    public void setExoPlayer(ExoPlayer player)
    {
        if (mPlayer!=null)
        {
            mPlayer.removeListener(mComponentListener);
        }
        mPlayer=player;
        mPlayer.addListener(mComponentListener);
        updatePauseDrawable();
        show(DEF_SHOW_TIMEOUT);
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }


    private void updatePauseDrawable()
    {
        if (mPlayer==null)return;
        if (mPlayer.getPlayWhenReady())
        {
            mPauseButton.setImageResource(android.R.drawable.ic_media_pause);
        }else {
            mPauseButton.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                case HIDE_VIEW:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && isShown() && mPlayer.getPlayWhenReady()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = (int) mPlayer.getCurrentPosition();
        int duration = (int) mPlayer.getDuration();
        if (duration > 0) {
            // use long to avoid overflow
            long pos = 1000L * position / duration;
            mSeekBar.setProgress( (int) pos);
        }
        int percent = mPlayer.getBufferedPercentage();
        mSeekBar.setSecondaryProgress(percent * 10);

        mEndTime.setText(stringForTime(duration));
        mCurrentTime.setText(stringForTime(position));

        return position;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mPlayer!=null)
        {
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(SHOW_PROGRESS);
        mHandler.removeMessages(HIDE_VIEW);
        mPlayer=null;
    }

    public class ComponentListener implements View.OnClickListener,ExoPlayer.Listener,
            SeekBar.OnSeekBarChangeListener{
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.pause)
            {
                if (mPlayer!=null)
                {
                    mPlayer.setPlayWhenReady(!mPlayer.getPlayWhenReady());
                }
            }
        }

        @Override
        public void onStateChanged(boolean playWhenReady, int playbackState) {
            updatePauseDrawable();
            if (playbackState==ExoPlayer.STATE_ENDED)
            {
                mHandler.removeMessages(HIDE_VIEW);
            }
        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser)
            {
                long duration = mPlayer.getDuration();
                long videoPosition = (duration * progress) / 1000L;
                mCurrentTime.setText(stringForTime((int) videoPosition));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mDragging=true;
            mHandler.removeMessages(SHOW_PROGRESS);
            mHandler.removeMessages(HIDE_VIEW);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mDragging=false;
            int progress=seekBar.getProgress();
            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo( (int) newposition);
            setProgress();
            updatePauseDrawable();
            show(DEF_SHOW_TIMEOUT);
        }
    }
}
