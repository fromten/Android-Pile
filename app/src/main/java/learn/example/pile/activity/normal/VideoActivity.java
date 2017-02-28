package learn.example.pile.activity.normal;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import learn.example.pile.R;
import learn.example.pile.activity.base.CommentMenuActivity;
import learn.example.pile.ui.VideoErrorDialog;
import learn.example.pile.ui.VolumeProgressView;
import learn.example.pile.video.CopySimpleExoPlayerView;
import learn.example.pile.video.SimpleEventListener;

/**
 * Created on 2016/5/26.
 */
public class VideoActivity extends CommentMenuActivity {

    private static final int DELAY_MILLISECOND=3000;
    private static final int MSG_HIDE_VOLUMEVIEW=0;
    private static final int MSG_HIDE_ACTIONBAR=1;
    private Handler mHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case MSG_HIDE_VOLUMEVIEW:
                    mVolumeProgressView.hide();
                    break;
                case MSG_HIDE_ACTIONBAR:
                    hideActionBar();
                    break;
            }
        }
    };


    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_USE_CONTROLLER="EXTRA_USE_CONTROLLER";
    public static final String EXTRA_SUPPORT_LOOP_MODE="EXTRA_SUPPORT_LOOP";

    private boolean isLooperMode;
    //private static final String STATE_SAVE_VIDEO_POSITION = "STATE_SAVE_VIDEO_POSITION";
    //final String URL = "http://baobab.kaiyanapp.com/api/v1/playUrl?vid=13861&editionType=default&source=ucloud";

    private VolumeProgressView mVolumeProgressView;
    private CopySimpleExoPlayerView mSimpleExoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private PlayerListener mPlayerListener;
    private long resumePosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_exo);
        mSimpleExoPlayerView = (CopySimpleExoPlayerView) findViewById(R.id.player_view);
        mVolumeProgressView= (VolumeProgressView) findViewById(R.id.video_volume);
        setTitle(getIntent().getStringExtra(EXTRA_TITLE));
        isLooperMode=getIntent().getBooleanExtra(EXTRA_SUPPORT_LOOP_MODE,false);
        initPlayerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    protected void onDestroy() {
        mSimpleExoPlayerView.setPlayer(null);//移除回调
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    public void initPlayerView() {
        boolean userController=getIntent().getBooleanExtra(EXTRA_USE_CONTROLLER,true);
        mSimpleExoPlayerView.setUseController(userController);
        final GestureDetector detector = new GestureDetector(this, new PlayViewGestureListener());
        mSimpleExoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;//拦截View事件
            }
        });
    }

    public void initializePlayer() {
        if (mExoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            //view set
            mSimpleExoPlayerView.setPlayer(mExoPlayer);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this, "player"), null);
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            Uri uri=getIntent().getData();
            MediaSource videoSource = new ExtractorMediaSource(uri,
                    dataSourceFactory, extractorsFactory, null, null);

            mPlayerListener=new PlayerListener();
            mExoPlayer.addListener(mPlayerListener);
            mExoPlayer.setPlayWhenReady(true);
            if (resumePosition > 0) {
                mExoPlayer.seekTo(resumePosition);
            }
            mExoPlayer.prepare(videoSource);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            resumePosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.removeListener(mPlayerListener);
            mExoPlayer.release();
            mExoPlayer = null;
            mPlayerListener=null;
        }
    }

    private void showAllAndRemoveMessage()
    {
        mHandler.removeMessages(MSG_HIDE_ACTIONBAR);
        showActionBar();
        mSimpleExoPlayerView.setControllerShowTimeoutMs(0);
        mSimpleExoPlayerView.showController();
    }

    private void showErrorDialog()
    {
        final VideoErrorDialog fragment=new VideoErrorDialog();
        fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializePlayer();
                fragment.dismiss();
            }
        });
        fragment.show(getSupportFragmentManager(),"error");
    }


    private class PlayerListener extends SimpleEventListener{
        private boolean isFirst=true;
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
           if (isFirst&&playWhenReady)
           {
               mHandler.removeMessages(MSG_HIDE_ACTIONBAR);
               mHandler.sendEmptyMessageDelayed(MSG_HIDE_ACTIONBAR,DELAY_MILLISECOND);
               isFirst=false;
           }
            if (playbackState== ExoPlayer.STATE_ENDED&&isLooperMode
                    &&mExoPlayer!=null)
            {
               resumePosition=0;
               mExoPlayer.seekTo(0);
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            showAllAndRemoveMessage();
            releasePlayer();
            showErrorDialog();
        }
    }

    private class PlayViewGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (isLooperMode)
            {
                finish();
                return true;
            }
            ActionBar actionBarV7 = getSupportActionBar();
            if (actionBarV7 != null) {
                if (actionBarV7.isShowing()) {
                    getSupportActionBar().hide();
                    mSimpleExoPlayerView.hideController();
                } else {
                    getSupportActionBar().show();
                    mSimpleExoPlayerView.setControllerShowTimeoutMs(DELAY_MILLISECOND);
                    mSimpleExoPlayerView.showController();

                    mHandler.removeMessages(MSG_HIDE_ACTIONBAR);
                    mHandler.sendEmptyMessageDelayed(MSG_HIDE_ACTIONBAR,DELAY_MILLISECOND);
                }
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mExoPlayer != null) {
                boolean isPlaying=mExoPlayer.getPlayWhenReady();
                mExoPlayer.setPlayWhenReady(!isPlaying);
            }
            return true;
        }


        private static final int SCROLL_DISTANCE=50;
        private int lastY;
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (lastY==0)
            {
                lastY= (int) e1.getY();
            }
            int cy= (int) e2.getY();
            int scrollY=cy-lastY;
            if (Math.abs(scrollY)>=SCROLL_DISTANCE) {
                mVolumeProgressView.show();
                if (scrollY > 0) {
                    mVolumeProgressView.decrease();
                } else {
                    mVolumeProgressView.increase();
                }
                lastY=cy;

                //先移除，后发送
                mHandler.removeMessages(MSG_HIDE_VOLUMEVIEW);
                mHandler.sendEmptyMessageDelayed(MSG_HIDE_VOLUMEVIEW,DELAY_MILLISECOND);
            }
            return true;
        }
    }


}
