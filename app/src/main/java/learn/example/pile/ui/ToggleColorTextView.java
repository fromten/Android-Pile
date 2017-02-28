package learn.example.pile.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

/**
 * Created on 2017/2/13.
 */

public class ToggleColorTextView extends TextView implements View.OnClickListener{


    private int cursor;

    private Drawable[] oldDrawables;
    private Drawable[] newDrawables;
    private static final int drawableColor= Color.RED;
    private static final int newTextColor=Color.RED;
    private int oldTextColor;


    public ToggleColorTextView(Context context) {
        this(context,null);
    }

    public ToggleColorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        oldTextColor=getCurrentTextColor();
        oldDrawables=getCompoundDrawables();
        newDrawables=new Drawable[oldDrawables.length];
        for (int i = 0; i < oldDrawables.length; i++) {
            Drawable drawable=oldDrawables[i];
            if (drawable!=null)
            {
                newDrawables[i]=drawable.getConstantState().newDrawable().mutate();
                newDrawables[i].setColorFilter(new LightingColorFilter(drawableColor,1));
            }
        }
    }


    @Override
    public void onClick(View v) {
        int value;
        try {
            value=Integer.valueOf(getText().toString());
        }catch (NumberFormatException e)
        {
            return;
        }
        if (cursor%2==0)
        {
            setText(String.valueOf(value+1));
            setTextColor(newTextColor);
            setCompoundDrawablesWithIntrinsicBounds(newDrawables[0],newDrawables[1],newDrawables[2],newDrawables[3]);
        }else {
            setText(String.valueOf(value-1));
            setTextColor(oldTextColor);
            setCompoundDrawablesWithIntrinsicBounds(oldDrawables[0],oldDrawables[1],oldDrawables[2],oldDrawables[3]);
        }
        cursor++;
        boom();
    }

    public void boom()
    {
        Animator animatorY= ObjectAnimator.ofFloat(this,View.SCALE_Y,1F,1.3F,1f);
        animatorY.setDuration(400);
        animatorY.setInterpolator(new BounceInterpolator());
        animatorY.start();
    }
}
