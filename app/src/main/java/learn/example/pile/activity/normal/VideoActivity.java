package learn.example.pile.activity.normal;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import learn.example.pile.R;
import learn.example.pile.activity.base.FullScreenActivity;
import learn.example.uidesign.VolumeProgressView;

/**
 * Created on 2016/5/26.
 */
public class VideoActivity extends FullScreenActivity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    private VideoView mVideoView;
    private MediaController mMediaController;
    private VolumeProgressView mVolumeProgressView;
    private TextView mLogView;
    private static final String KEY_SAVE_STATE_POSITION = "KEY_SAVE_STATE_POSITION";

    private Bundle saveState;
    private String TAG = "VideoActivity";

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
        mVideoView.setOnTouchListener(touchListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mVideoView.isPlaying())
            mVideoView.start();
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
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mLogView.setText("发生错误了...");
        return true;
    }

    //监听,控制播放的音量
    private View.OnTouchListener touchListener = new View.OnTouchListener() {


        private float mDownY; //按下的y坐标
        private boolean inPress;//是否按下
        private boolean inMove;//是否移动

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                mDownY = event.getY();
                inPress = true;
                inMove = false;
            } else if (action == MotionEvent.ACTION_MOVE) {   //移动增加或减少音量
                float y = event.getY();
                if (Math.abs(mDownY - y) > 90) {
                    //如果不可见,则显示
                    if (mVolumeProgressView.getVisibility() != View.VISIBLE)
                        mVolumeProgressView.setVisibility(View.VISIBLE);
                    int value = mVolumeProgressView.getCurrentValue();
                    if (mDownY > y) {
                        mVolumeProgressView.setCurrentValue(value + 1);
                    } else {
                        mVolumeProgressView.setCurrentValue(value - 1);
                    }
                    inPress = false;
                    inMove = true;
                    mDownY = event.getY();
                }
            } else if (action == MotionEvent.ACTION_UP) {
                inMove = false;
                if (inPress) {
                    if (mMediaController.isShowing()) {
                        mMediaController.hide();
                        hideActionBar();
                    } else {
                        mMediaController.show();
                        showActionBar();
                    }
                } else {
                    mVolumeProgressView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!inMove)
                                mVolumeProgressView.setVisibility(View.INVISIBLE);
                        }
                    }, 3000);//三秒后移除音量进度栏
                }
            }
            return true;
        }
    };
}