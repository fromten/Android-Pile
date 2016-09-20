package learn.example.pile.activity.normal;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;

import learn.example.pile.R;
import learn.example.pile.activity.base.CommentMenuActivity;
import learn.example.pile.activity.base.FullScreenActivity;
import learn.example.pile.fragment.comment.OpenEyeCommentFragment;
import learn.example.pile.ui.VolumeProgressView;
import learn.example.pile.video.ExoVideoView;
import learn.example.pile.video.MediaPlayControlView;

/**
 * Created on 2016/5/26.
 */
public class VideoActivity extends CommentMenuActivity {



    private static final String KEY_SAVE_VIDEO_POSITION = "video_position";
    public static final String KEY_TITLE="title";


    private ExoVideoView mExoVideoView;
    private MediaPlayControlView mPlayerControlView;
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
        mPlayerControlView = (MediaPlayControlView) findViewById(R.id.control);
        mRetryRoot= (LinearLayout) findViewById(R.id.retry_root);
        mVolumeProgressView= (VolumeProgressView) findViewById(R.id.video_volume);

        setListener();
        showTitle();

        if (savedInstanceState != null) {
            mSavedSeekPosition=savedInstanceState.getInt(KEY_SAVE_VIDEO_POSITION,-1);
        }


        playVideo();


    }

    private void showTitle()
    {
        String title=getIntent().getStringExtra(KEY_TITLE);
        setTitle(title);
    }

    private void playVideo()
    {
        Uri uri=getIntent().getData();
        if (uri!=null) {
            mExoVideoView.setVideoUri(uri);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mExoVideoView.start();
        mPlayerControlView.updatePauseDrawable();
    }

    @Override
    protected void onPause() {
        mExoVideoView.pause();
        super.onPause();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //保存现在的播放位置
        outState.putInt(KEY_SAVE_VIDEO_POSITION,mPlayControl==null?0:mPlayControl.getCurrentPosition() );
        super.onSaveInstanceState(outState);
    }

    private ExoVideoView.OnPlayInfoListener mPlayInfoListener=new ExoVideoView.OnPlayInfoListener() {
        @Override
        public void onPrepare(ExoVideoView view) {
            mPlayControl=view.getPlayerControl();
            if (mSavedSeekPosition>0)
            {
                mPlayControl.seekTo(mSavedSeekPosition);
            }
            mPlayerControlView.setMediaPlayerControl(mPlayControl);
            enableView(mPlayerControlView,true);


            mHandler.sendEmptyMessageDelayed(HIDE_ACTIONBAR,3000);
            if (mRetryViewHolder.mRoot.isShown()) mRetryViewHolder.hide();

        }

        @Override
        public void onCompleted(ExoVideoView view) {
            mRetryViewHolder.show("重新播放");
            enableView(mPlayerControlView,false);
        }

        @Override
        public void onError(ExoVideoView view, Exception error) {
            mRetryViewHolder.show("播放错误,重试");
            enableView(mPlayerControlView,false);
            mPlayControl=null;
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



    private void enableView(View view,boolean enable)
    {
        view.setEnabled(enable);
        if (view instanceof ViewGroup)
        {
            ViewGroup viewGroup= (ViewGroup) view;
            for (int i = viewGroup.getChildCount()-1; i >=0; i--) {
                enableView(viewGroup.getChildAt(i),enable);
            }
        }
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
            mPlayerControlView.updatePauseDrawable();
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

        int previousDownY;
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int downY= (int) e2.getY();

            int lenY= previousDownY-downY;
            int increase=0;
            if (lenY>=20)
            {
                increase=1;
            }else if (Math.abs(lenY)>=20){
                increase=-1;
            }
            if (increase!=0)
            {
                mVolumeProgressView.show();
                mVolumeProgressView.setCurrentValue(mVolumeProgressView.getCurrentValue()+increase);

                previousDownY=downY;
                mHandler.removeMessages(HIDE_VOLUME_PROGERESSBAR);
                mHandler.sendEmptyMessageDelayed(HIDE_VOLUME_PROGERESSBAR,3000);
                return true;
            }

            return false;
        }
    };



    private  class RetryViewHolder {
        private View mRoot;
        private TextView mTextView;

        public RetryViewHolder(View view) {
            mRoot = view;
            mTextView= (TextView) mRoot.findViewById(R.id.hint);
            mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPlayControl!=null)
                    {   //播放器实例还存在
                        mPlayControl.seekTo(0);
                    }else {//重新播放视频
                        playVideo();
                    }
                    hide();
                    enableView(mPlayerControlView,true);
                }
            });
        }

        public void show(CharSequence hintText)
        {
            mRoot.setVisibility(View.VISIBLE);
            mRoot.setFocusable(true);
            mRoot.setClickable(true);
            mRoot.requestFocus();

            mTextView.setText(hintText);
        }

        public void hide()
        {
            mRoot.setVisibility(View.INVISIBLE);
            mRoot.setFocusable(false);
            mRoot.setClickable(false);
        }

    }


}
