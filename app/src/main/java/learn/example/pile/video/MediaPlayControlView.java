package learn.example.pile.video;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Formatter;
import java.util.Locale;

import learn.example.pile.R;

/**
 * Created on 2016/9/7.
 */
public class MediaPlayControlView extends FrameLayout implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener{


    private static final int DEF_SHOW_TIMEOUT=3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private boolean mShowing;
    private boolean mDragging;

    StringBuilder mFormatBuilder;
    Formatter mFormatter;

    public View mRoot;
    private ImageButton mPauseButton;
    private SeekBar mSeekBar;
    private TextView mCurrentTime;
    private TextView mEndTime;

    private MediaController.MediaPlayerControl mControl;


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

        mSeekBar.setOnSeekBarChangeListener(this);
        mPauseButton.setOnClickListener(this);

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mShowing=true;
        mDragging=false;
        disUnSupportButton();
    }


    private void disUnSupportButton()
    {
        boolean hasControl=mControl!=null;

        mPauseButton.setEnabled(hasControl);
        mSeekBar.setEnabled(hasControl);
    }


    public void show(int time)
    {


        if (!mShowing)
        {
            this.setVisibility(VISIBLE);
            mDragging=false;
            mShowing=true;
        }

        if (mControl!=null)
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        if (time!=0)
        {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendEmptyMessageDelayed(FADE_OUT,time);
        }

    }

    public void hide()
    {
        if (mShowing)
        {
            this.setVisibility(INVISIBLE);
            mDragging=false;
            mShowing=false;
        }
    }


    public void setMediaPlayerControl(MediaController.MediaPlayerControl control)
    {
        mControl =control;
        updatePauseDrawable();
        show(DEF_SHOW_TIMEOUT);
        disUnSupportButton();
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

    @Override
    public void onClick(View v) {
        if (mControl.isPlaying())
        {
            mControl.pause();
        }else {
            mControl.start();
        }
        updatePauseDrawable();
        show(DEF_SHOW_TIMEOUT);
    }

    public void updatePauseDrawable()
    {
        if (mControl==null)return;
        if (mControl.isPlaying())
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
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && mShowing && mControl.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };

    private int setProgress() {
        if (mControl == null || mDragging) {
            return 0;
        }
        int position = mControl.getCurrentPosition();
        int duration = mControl.getDuration();
        if (duration > 0) {
            // use long to avoid overflow
            long pos = 1000L * position / duration;
            mSeekBar.setProgress( (int) pos);
        }
        int percent = mControl.getBufferPercentage();
        mSeekBar.setSecondaryProgress(percent * 10);

        mEndTime.setText(stringForTime(duration));
        mCurrentTime.setText(stringForTime(position));

        return position;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (fromUser)
        {
            long duration = mControl.getDuration();
            long videoPosition = (duration * progress) / 1000L;
            mCurrentTime.setText(stringForTime((int) videoPosition));
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

          mDragging=true;
          mHandler.removeMessages(SHOW_PROGRESS);
          mHandler.removeMessages(FADE_OUT);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
           mDragging=false;
           int progress=seekBar.getProgress();
           long duration = mControl.getDuration();
           long newposition = (duration * progress) / 1000L;
           mControl.seekTo( (int) newposition);

           setProgress();
           updatePauseDrawable();
           show(DEF_SHOW_TIMEOUT);
    }

    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeMessages(SHOW_PROGRESS);
        mHandler.removeMessages(FADE_OUT);
        mHandler.removeCallbacksAndMessages(null);
        mControl=null;
        super.onDetachedFromWindow();
    }
}
