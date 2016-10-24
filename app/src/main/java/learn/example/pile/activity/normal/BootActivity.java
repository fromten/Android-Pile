package learn.example.pile.activity.normal;

import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import learn.example.pile.R;
import learn.example.pile.activity.base.FullScreenActivity;
import learn.example.pile.service.StartImageCacheService;

import static learn.example.pile.service.StartImageCacheService.CACHE_FILE_NAME;
import static learn.example.pile.service.StartImageCacheService.KEY_IMAGE_OWNER;

/**
 * Created on 2016/9/25.
 */

public class BootActivity extends FullScreenActivity{
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private TextView mImageOwn;
    private ValueAnimator mValueAnimator;
    public static final int SHOW_DURATION=2500;//2.5 second

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
        setTitle(null);
        hideActionBar();
        mImageView= (ImageView) findViewById(R.id.image);
        mProgressBar= (ProgressBar) findViewById(R.id.progress);
        mImageOwn= (TextView) findViewById(R.id.image_own);
        setNextCacheAlarm();
        setStartImage();
        startCountDown();
    }



    public void startCountDown()
    {
        mValueAnimator=ValueAnimator.ofInt(0,100);
        mValueAnimator.setDuration(SHOW_DURATION);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (isFinishing())return;

                        int value= (int) animation.getAnimatedValue();
                        mProgressBar.setProgress(value);
                        if (value>=99)
                        {
                            startMainActivity();
                        }
                    }
                });
        mValueAnimator.start();

    }

    public void setStartImage()
    {
        Bitmap bitmap=findCacheImage();
        if (bitmap!=null)
        {
            String imageOwn=getSharedPreferences(StartImageCacheService.PREFERENCE_FILE_NAME,Context.MODE_PRIVATE)
                    .getString(KEY_IMAGE_OWNER,null);
            mImageOwn.setText(imageOwn);
            mImageView.setImageBitmap(bitmap);
        }else {
            Intent intent=new Intent(this, StartImageCacheService.class);
            startService(intent);
            startMainActivity();
        }

    }


    public Bitmap findCacheImage()
    {
        File file= getFileStreamPath(CACHE_FILE_NAME);
        if (file.exists())
        {
            return BitmapFactory.decodeFile(file.getPath());
        }
        return null;
    }

    public void startMainActivity()
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        mValueAnimator.cancel();
        super.onDestroy();
    }


    @Override
    public void enableSupportHomeUp() {
        //no display
    }

    public void setNextCacheAlarm()
    {
        AlarmManager manager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long sevenHour=7*AlarmManager.INTERVAL_HOUR;
        Intent intent=new Intent(this,StartImageCacheService.class);
        PendingIntent pendingIntent=PendingIntent.getService(this,1,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME,sevenHour,sevenHour,pendingIntent);
    }



}
