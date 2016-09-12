package learn.example.pile.video;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;

import learn.example.pile.R;
import learn.example.pile.activity.base.SupportCommentActivity;
import learn.example.pile.ui.VolumeProgressView;

/**
 * Created on 2016/5/26.
 */
public class VideoActivity extends SupportCommentActivity {

    private static final String KEY_SAVE_VIDEO_POSITION = "video_position";



    private ExoVideoView mExoVideoView;
    private MediaControlViewHolder mControlView;
    private VolumeProgressView mVolumeProgressView;
    private LinearLayout mRetryRoot;
    private MediaController.MediaPlayerControl mPlayControl;

    private RetryViewHolder mRetryViewHolder;

    private int mSavedSeekPosition=-1;

    private GestureDetector mScreenTouchListener;

    public static final int HIDE_VOLUME_PROGERESSBAR=0;
    public static final int HIDE_ACTIONBAR=1;
    private Handler mHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what)
           {
               case HIDE_VOLUME_PROGERESSBAR:
                   mVolumeProgressView.hide();
                   break;
               case HIDE_ACTIONBAR:
                    hideActionBar();
                   break;
           }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_exo);

        mExoVideoView= (ExoVideoView) findViewById(R.id.video);
        mControlView= (MediaControlViewHolder) findViewById(R.id.control);
        mRetryRoot= (LinearLayout) findViewById(R.id.retry_root);
        mVolumeProgressView= (VolumeProgressView) findViewById(R.id.video_volume);

        setListener();

        mSavedSeekPosition=savedInstanceState==null?0:savedInstanceState.getInt(KEY_SAVE_VIDEO_POSITION);

        playVideo();


    }

    private void playVideo()
    {
        Uri uri=getIntent().getData();
        if (uri!=null) {
            mExoVideoView.setVideoUri(uri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_comment)
        {
            if (mPlayControl!=null)
            {
                mPlayControl.pause();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getCommentFragmentIfShowing()!=null&&mPlayControl!=null)
        {
            mPlayControl.start();
        }
        super.onBackPressed();
    }

    private ExoVideoView.OnPlayInfoListener mPlayInfoListener=new ExoVideoView.OnPlayInfoListener() {
        @Override
        public void onPrepare(ExoVideoView view) {
            mPlayControl=view.getPlayerControl();
            if (mSavedSeekPosition>0)
            {
                mPlayControl.seekTo(mSavedSeekPosition);
            }
            mControlView.setMediaPlayerControl(mPlayControl);
            mRetryViewHolder.hide();

            if (getCommentFragmentIfShowing()!=null)
            {
                mPlayControl.pause();
            }

            mHandler.sendEmptyMessageDelayed(HIDE_ACTIONBAR,3000);
        }

        @Override
        public void onCompleted(ExoVideoView view) {
            mRetryViewHolder.show();
        }

        @Override
        public void onError(ExoVideoView view, Exception error) {
            mRetryViewHolder.show();
        }
    } ;


    public void setListener() {
        mRetryViewHolder=new RetryViewHolder(mRetryRoot);

        mExoVideoView.setPlayInfoListener(mPlayInfoListener);

        mScreenTouchListener=new GestureDetector(this,mSimpleOnGestureListener);
        mExoVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScreenTouchListener.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        if (mPlayControl!=null)
        {
            mPlayControl.start();
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mPlayControl!=null)
        {
            mPlayControl.pause();
        }
        super.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //保存现在的播放位置
        outState.putInt(KEY_SAVE_VIDEO_POSITION,mPlayControl==null?0:mPlayControl.getCurrentPosition() );
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {

        mHandler.removeMessages(HIDE_ACTIONBAR);
        mHandler.removeMessages(HIDE_VOLUME_PROGERESSBAR);
        mHandler=null;

        mExoVideoView.release();
        mExoVideoView.setPlayInfoListener(null);
        mExoVideoView.setOnTouchListener(null);
        mExoVideoView=null;
        mPlayControl=null;
        mPlayInfoListener=null;
        mSimpleOnGestureListener=null;
        super.onDestroy();
    }



    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener=new GestureDetector.SimpleOnGestureListener(){

        private void toggleControlsVisible()
        {
            if (mControlView.isShown()) {
                mControlView.hide();
                hideActionBar();
            } else {
                mControlView.show(3000);
                showActionBar();
            }

            if (mVolumeProgressView.isShown())//确保view隐藏
            {
                mHandler.sendEmptyMessage(HIDE_VOLUME_PROGERESSBAR);
            }

           // 在事件时间内如果重新点击,移除msg
            mHandler.removeMessages(HIDE_ACTIONBAR);
          //  3秒后隐藏ActionBar
            mHandler.sendEmptyMessageDelayed(HIDE_ACTIONBAR,3000);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mPlayControl==null)return false;

            if (mPlayControl.isPlaying()) {
                mPlayControl.pause();
            } else {
                mPlayControl.start();
            }
            mControlView.updatePauseDrawable();
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


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            if (Math.abs(distanceY)>Math.abs(distanceX)&&Math.abs(distanceY)>15)
            {
                //  int distance=y1-y2;
                mVolumeProgressView.show();
                if (distanceY>0)
                {
                    mVolumeProgressView.increase();
                }else {

                    mVolumeProgressView.decrease();
                }
                mHandler.removeMessages(HIDE_VOLUME_PROGERESSBAR);
                mHandler.sendEmptyMessageDelayed(HIDE_VOLUME_PROGERESSBAR,3000);
                return true;
            }

            return false;
        }
    };

    @Override
    protected int getReplaceId() {
        return R.id.root;
    }

    private  class RetryViewHolder {
        private View mView;

        public RetryViewHolder(View view) {
            mView = view;
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPlayControl!=null)
                    {
                        mPlayControl.seekTo(0);
                        hide();
                    }else {
                        playVideo();
                    }
                }
            });
        }

        public void show()
        {
            mView.setVisibility(View.VISIBLE);
            mView.setFocusable(true);
            mView.setClickable(true);
            mView.requestFocus();
        }

        public void hide()
        {
            mView.setVisibility(View.INVISIBLE);
            mView.setFocusable(false);
            mView.setClickable(false);
        }

    }


}
