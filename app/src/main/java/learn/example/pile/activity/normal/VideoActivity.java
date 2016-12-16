package learn.example.pile.activity.normal;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import learn.example.pile.R;
import learn.example.pile.activity.base.CommentMenuActivity;
import learn.example.pile.ui.VolumeProgressView;
import learn.example.pile.video.ExoPlayer;
import learn.example.pile.video.ExoVideoView;
import learn.example.pile.video.MediaPlayControlView;

/**
 * Created on 2016/5/26.
 */
public class VideoActivity extends CommentMenuActivity {


    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    private static final String STATE_SAVE_VIDEO_POSITION = "STATE_SAVE_VIDEO_POSITION";


    private ExoVideoView mExoVideoView;
    private MediaPlayControlView mPlayerControlView;
    private VolumeProgressView mVolumeProgressView;
    private LinearLayout mRetryRoot;
    private RetryViewHolder mRetryViewHolder;

    private GestureDetector mScreenTouchListener;
    private VideoStateListener mVideoStateListener;
    private ExoPlayer mPlayer;
    private int mSavedSeekPosition = -1;

    public static final int HIDE_VOLUME_PROGERESSBAR = 0;
    public static final int HIDE_ACTIONBAR = 1;
    private static final int RESUME_PLAY = 4;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HIDE_VOLUME_PROGERESSBAR:
                    mVolumeProgressView.hide();
                    break;
                case HIDE_ACTIONBAR:
                    hideActionBar();
                    break;
                case RESUME_PLAY:
                    mExoVideoView.start();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_exo);
        bindViews();
        setListener();
        showTitle();
        if (savedInstanceState != null) {
            mSavedSeekPosition = savedInstanceState.getInt(STATE_SAVE_VIDEO_POSITION, -1);
        }
        playVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(RESUME_PLAY, 1000);
    }

    @Override
    protected void onPause() {
        mExoVideoView.pause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //保存现在的播放位置
        outState.putInt(STATE_SAVE_VIDEO_POSITION, mPlayer == null ? 0 : (int) mPlayer.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        mExoVideoView.release();
        mExoVideoView.setPlayInfoListener(null);
        mExoVideoView.setOnTouchListener(null);
        mExoVideoView = null;
        mVideoStateListener = null;
        mSimpleOnGestureListener = null;
        super.onDestroy();
    }


    private void showTitle() {
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        setTitle(title);
    }

    private void bindViews() {
        mExoVideoView = (ExoVideoView) findViewById(R.id.video);
        mPlayerControlView = (MediaPlayControlView) findViewById(R.id.control);
        mRetryRoot = (LinearLayout) findViewById(R.id.retry_root);
        mVolumeProgressView = (VolumeProgressView) findViewById(R.id.video_volume);
    }

    private void playVideo() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            mExoVideoView.setVideoUri(uri);
        }
    }

    public void setListener() {
        mRetryViewHolder = new RetryViewHolder(mRetryRoot);
        mVideoStateListener = new VideoStateListener();
        mExoVideoView.setPlayInfoListener(mVideoStateListener);
        mScreenTouchListener = new GestureDetector(this, mSimpleOnGestureListener);
        mExoVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScreenTouchListener.onTouchEvent(event);
                return true;
            }
        });
    }


    private void enableView(View view, boolean enable) {
        view.setEnabled(enable);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                enableView(viewGroup.getChildAt(i), enable);
            }
        }
    }


    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        private void toggleControlsVisible() {
            if (mPlayerControlView.isShown()) {
                mPlayerControlView.hide();
                hideActionBar();
            } else {
                mPlayerControlView.show(3000);
                showActionBar();
            }

            if (mVolumeProgressView.isShown())//确保view隐藏
            {
                mHandler.sendEmptyMessage(HIDE_VOLUME_PROGERESSBAR);
            }

            // 在事件时间内如果重新点击,移除msg
            mHandler.removeMessages(HIDE_ACTIONBAR);
            //  3秒后隐藏ActionBar
            mHandler.sendEmptyMessageDelayed(HIDE_ACTIONBAR, 3000);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mPlayer == null) return false;
            mPlayerControlView.findViewById(R.id.pause).performClick();
            toggleControlsVisible();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            toggleControlsVisible();
            return true;
        }

        int lastY;
        private final int MAX_SCROLL_DISTANCE=50;
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (lastY==0)lastY= (int) e1.getY();
            int cy= (int) e2.getY();
            distanceY=cy-lastY;
            if (Math.abs(distanceY)>=MAX_SCROLL_DISTANCE) {
                mVolumeProgressView.show();
                if (distanceY > 0) {
                    mVolumeProgressView.decrease();
                } else {
                    mVolumeProgressView.increase();
                }
                mHandler.removeMessages(HIDE_VOLUME_PROGERESSBAR);
                mHandler.sendEmptyMessageDelayed(HIDE_VOLUME_PROGERESSBAR, 3000);
                lastY=cy;
            }
            return true;
        }
    };


    private class RetryViewHolder {
        private View mRoot;
        private TextView mTextView;

        public RetryViewHolder(View view) {
            mRoot = view;
            mTextView = (TextView) mRoot.findViewById(R.id.hint);
            mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPlayer != null) {   //播放器实例还存在
                        mPlayer.seekTo(0);
                    } else {//重新播放视频
                        playVideo();
                    }
                    hide();
                    enableView(mPlayerControlView, true);
                }
            });
        }

        public void show(CharSequence hintText) {
            mRoot.setVisibility(View.VISIBLE);
            mRoot.setFocusable(true);
            mRoot.setClickable(true);
            mRoot.requestFocus();

            mTextView.setText(hintText);
        }

        public void hide() {
            mRoot.setVisibility(View.INVISIBLE);
            mRoot.setFocusable(false);
            mRoot.setClickable(false);
        }

    }

    private class VideoStateListener implements ExoVideoView.OnPlayInfoListener {

        @Override
        public void onPrepare(ExoVideoView view) {
            mPlayer = view.getPlayer();
            if (mSavedSeekPosition > 0) {
                mPlayer.seekTo(mSavedSeekPosition);
            }
            mPlayerControlView.setExoPlayer(mPlayer);
            enableView(mPlayerControlView,true);
            mHandler.sendEmptyMessageDelayed(HIDE_ACTIONBAR, 3000);
            if (mRetryViewHolder.mRoot.isShown()) mRetryViewHolder.hide();
        }


        @Override
        public void onCompleted(ExoVideoView view) {
            mRetryViewHolder.show("重新播放");
            enableView(mPlayerControlView, false);
        }

        @Override
        public void onError(ExoVideoView view, Exception error) {
            mRetryViewHolder.show("播放错误,重试");
            enableView(mPlayerControlView,false);
            mExoVideoView.release();
            mPlayer = null;
        }
    }


}
