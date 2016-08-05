package learn.example.pile.activity.normal;

import android.content.Context;
import android.gesture.GestureUtils;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import learn.example.pile.R;
import learn.example.pile.activity.base.FullScreenActivity;
import learn.example.pile.fragment.OpenEyeCommentFragment;
import learn.example.pile.object.OpenEyes;
import learn.example.uidesign.VolumeProgressView;

/**
 * Created on 2016/5/26.
 */
public class VideoActivity extends FullScreenActivity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    private static final String KEY_SAVE_STATE_POSITION = "key_save_state_position";
    public static final String KEY_VIDEO_OPEN_EYE="open_eye";


    private VideoView mVideoView;
    private MediaController mMediaController;
    private VolumeProgressView mVolumeProgressView;
    private TextView mLogView;

    private Bundle saveState;

    private GestureDetector mScreenTouchListener;

    public static final int HIDE_VOLUME_PROGERESSBAR=0;
    public static final int HIDE_ACTIONBAR=1;
    private Handler mHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what)
           {
               case HIDE_VOLUME_PROGERESSBAR:mVolumeProgressView.hide();break;
               case HIDE_ACTIONBAR:
                   if (!mMediaController.isShowing())//Actionbar和MediaController应该同步显示或隐藏
                    hideActionBar();break;
           }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
        saveState = savedInstanceState;
        Uri uri = getIntent().getData();
        if (uri != null) {
            mLogView.setText("加载中....");
            mVideoView.setVideoURI(uri);
        } else {
            mLogView.setText("无效的地址");
        }



    }


    public void initView() {
        mVideoView = (VideoView) findViewById(R.id.video_view);
        mVolumeProgressView = (VolumeProgressView) findViewById(R.id.video_volume);
        mLogView = (TextView) findViewById(R.id.video_logmsg);
        mVolumeProgressView.setRectColor(getResources().getColor(R.color.grey));
        mMediaController = new MediaController(this);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        mScreenTouchListener=new GestureDetector(this,mSimpleOnGestureListener);
        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mScreenTouchListener.onTouchEvent(event);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().hasExtra(KEY_VIDEO_OPEN_EYE))
        {
            Log.d("v","have" );
            getMenuInflater().inflate(R.menu.video_menu,menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_comment)
        {
            //打开评论
            OpenEyes.VideoInfo info=getIntent().getParcelableExtra(KEY_VIDEO_OPEN_EYE);
            if (info!=null)
            {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.root, OpenEyeCommentFragment.newInstance(info.getId()))
                        .commit();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mVideoView.isPlaying())
            mVideoView.resume();
    }

    @Override
    protected void onPause() {
        if (mVideoView.isPlaying())
            mVideoView.pause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //保存现在的播放位置
        outState.putInt(KEY_SAVE_STATE_POSITION, mVideoView.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(HIDE_ACTIONBAR);
        mHandler.removeMessages(HIDE_VOLUME_PROGERESSBAR);
        mHandler=null;
        mVideoView.stopPlayback();
        mVideoView = null;
        mMediaController = null;

        super.onDestroy();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        //跳到旋转之前的播放位置
        if (saveState != null) {
            int playOldPos = saveState.getInt(KEY_SAVE_STATE_POSITION);
            mVideoView.seekTo(playOldPos);
        }
        mVideoView.start();
        mLogView.setText(null);
        hideActionBar();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mLogView.setText("播放结束");
        //十秒后
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLogView.setText(null);
            }
        },10000);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mLogView.setText("发生错误了...");
        return true;
    }

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener=new GestureDetector.SimpleOnGestureListener(){

        private void showControls()
        {
            if (mMediaController.isShowing()) {
                mMediaController.hide();
                hideActionBar();
            } else {
                mMediaController.show(3000);
                showActionBar();
            }
            //在事件时间内如果重新点击,移除msg
            mHandler.removeMessages(HIDE_ACTIONBAR);
            //3秒后隐藏ActionBar
            mHandler.sendEmptyMessageDelayed(HIDE_ACTIONBAR,3000);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mVideoView.isPlaying() && mVideoView.canPause()) {
                mVideoView.pause();
            } else {
                mVideoView.start();
            }
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            showControls();
            return true;
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceY) >= 20) {
                mVolumeProgressView.show();
                int value = mVolumeProgressView.getCurrentValue();
                if (distanceY > 0) {
                    mVolumeProgressView.setCurrentValue(value + 1);
                } else {
                    mVolumeProgressView.setCurrentValue(value - 1);
                }
                //发送消息隐藏音量进度条,如果重新
                mHandler.removeMessages(HIDE_VOLUME_PROGERESSBAR);
                mHandler.sendEmptyMessageDelayed(HIDE_VOLUME_PROGERESSBAR, 3000);
                return true;
            }
            return false;
        }
    };
}
