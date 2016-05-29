package learn.example.pile.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created on 2016/5/27.
 */
public class VolumeProgressView extends View {
    private Paint mPaint;
    private int VOLUMEVALUE;
    private Rect[] mRect;
    private int currentValue;
    private int rectColor;//格子的颜色
    private int borderColor;//格子四条边的颜色



    private AudioManager mAudioManager;

    private String TAG="AudioProgressView";
    public VolumeProgressView(Context context) {
        super(context);
        initView();
    }


    public VolumeProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView()
    {
        mPaint=new Paint();
        mAudioManager= (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        int audiomax = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        int current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );

        VOLUMEVALUE =audiomax;
        currentValue=current;

        mRect=new Rect[VOLUMEVALUE];
        for(int i = 0; i< VOLUMEVALUE; ++i){
            mRect[i]=new Rect(0,0,0,0);
        }

        rectColor=Color.RED;
        borderColor=Color.BLACK;
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int len=mRect.length;
        int width=right-left;
        int height=bottom-top;
        int space=height/ VOLUMEVALUE;
        mRect[0].set(0,0,width,space);
        for(int i=1;i<len;++i)
        {
            int t=mRect[i-1].bottom;
            int b=t+space;
            mRect[i].set(0,t,width,b);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
         mPaint.setStrokeWidth(8);
         int len=mRect.length;
         int drawNum= VOLUMEVALUE -currentValue;//要绘制的格子个数
        for(int i=0;i<len;++i)
        {
            //绘制实心的格子
            if(i>=drawNum) {
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(rectColor);
                canvas.drawRect(mRect[i],mPaint);
            }
            //绘制空心的格子
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(borderColor);
            canvas.drawRect(mRect[i],mPaint);
        }
    }



    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        int value=currentValue;
        if(currentValue>= VOLUMEVALUE)
        {
           value= VOLUMEVALUE;
        }else if(currentValue<=0)
        {
            value=0;
        }
        this.currentValue = value;
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,value,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        invalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public int getRectColor() {
        return rectColor;
    }

    public void setRectColor(int rectColor) {
        this.rectColor = rectColor;
    }
}
