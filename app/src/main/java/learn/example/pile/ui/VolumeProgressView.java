package learn.example.pile.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created on 2016/5/27.
 */
public class VolumeProgressView extends View {
    private Paint mPaint;
    private int VOLUME_MAX_VALUE;
    private Rect[] mRect;
    private int currentValue;
    private int rectColor;//格子的颜色
    private int borderColor;//格子四边的颜色
    private int defWidth;
    private int defHeight;

    private AudioManager mAudioManager;

    public VolumeProgressView(Context context) {
        this(context,null);
    }


    public VolumeProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int viewWidth=resolveSize(defWidth,widthMeasureSpec);
        int viewHeight=resolveSize(defHeight,heightMeasureSpec);

        setMeasuredDimension(viewWidth,viewHeight);
    }

    public void initView()
    {
        mPaint=new Paint();
        mAudioManager= (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        int audiomax = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        int current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );

        VOLUME_MAX_VALUE =audiomax;
        currentValue=current;

        mRect=new Rect[VOLUME_MAX_VALUE];
        for(int i = 0; i< VOLUME_MAX_VALUE; ++i){
            mRect[i]=new Rect(0,0,0,0);
        }
        rectColor=Color.GRAY;
        borderColor=Color.BLACK;

        //View的高度默认为屏幕3分之一,宽度为 高度 ÷ 音量最大值
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager= (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);

        defHeight=(metrics.heightPixels/3);
        defWidth=defHeight/VOLUME_MAX_VALUE;
    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int len=mRect.length;
        int width=right-left;
        int height=bottom-top;
        int rectHeight=height/ VOLUME_MAX_VALUE;
        mRect[0].set(0,0,width,rectHeight);
        for(int i=1;i<len;++i)
        {
            int t=mRect[i-1].bottom;
            int b=t+rectHeight;
            mRect[i].set(0,t,width,b);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
         mPaint.setStrokeWidth(8);
         int len=mRect.length;
         int drawNum= VOLUME_MAX_VALUE -currentValue;//要绘制的格子个数
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
        if (this.currentValue==currentValue)
        {
            return;
        }

        int value=currentValue;
        if(currentValue>= VOLUME_MAX_VALUE)
        {
           value= VOLUME_MAX_VALUE;
        }else if(currentValue<0)
        {
            value=0;
        }
        this.currentValue = value;
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,value,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        invalidate();
    }

    public void increase() {
        setCurrentValue(currentValue+1);
    }

    public void decrease() {
        setCurrentValue(currentValue-1);
    }



    public void show()
    {
        if (getVisibility()!=View.VISIBLE)
        setVisibility(VISIBLE);
    }

    public void hide()
    {
        if (getVisibility()!=View.INVISIBLE)
           setVisibility(INVISIBLE);
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
