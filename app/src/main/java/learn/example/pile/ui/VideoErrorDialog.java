package learn.example.pile.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import learn.example.pile.R;

/**
 * Created on 2017/2/15.
 */

public class VideoErrorDialog extends DialogFragment {


    private static final String ERROR_TEXT="重试";
    private static final int TOP_DRAWABLE_ID = R.mipmap.ic_refresh_grey;
    private View.OnClickListener mOnClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context=inflater.getContext();
        TextView textView=new TextView(context);
        Drawable top=ResourcesCompat.getDrawable(getResources(),TOP_DRAWABLE_ID,context.getTheme());
        textView.setCompoundDrawablesWithIntrinsicBounds(null,
                top,null,null);
        textView.setOnClickListener(mOnClickListener);
        textView.setText(ERROR_TEXT);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        getDialog().setCanceledOnTouchOutside(false);
        return textView;
    }

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
