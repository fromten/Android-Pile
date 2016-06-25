package learn.example.pile.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.Formatter;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import learn.example.joke.R;
import learn.example.pile.AppGlideModule;
import learn.example.pile.util.AccessAppDataHelper;

/**
 * Created on 2016/5/6.
 */
public class SettingFragment extends Fragment {
    public static final String TAG = "SettingFragment";
    private TextView mImgCache;
    private Spinner mSpinner;
    private Button mSpinnerButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        mImgCache = (TextView) v.findViewById(R.id.img_cache);
        mSpinner = (Spinner) v.findViewById(R.id.mode_spinner);
        mSpinnerButton = (Button) v.findViewById(R.id.spinner_button);
        mSpinnerButton.setOnClickListener(new SpinnerButtonClick());
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCacheSizeString(readGlideDiskCacheSize());
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    /**
     * 设置显示的缓存大小文本
     *
     * @param sizeStr 缓存大小字符串
     */
    public void setCacheSizeString(String sizeStr) {
        //转换成字符串,在取小数点后一位;
        String size = sizeStr;
        String summary = "磁盘缓存大小: ";
        ClickableSpan clickSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showCommonDialog(getContext(), "清除缓存", "确定清楚图片缓存?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearGlideDiskCache();
                    }
                }, null);
                mImgCache.setSelected(true);
            }
        };
        mImgCache.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableStringBuilder builder = SpannableStringBuilder.valueOf(summary + size);
        builder.setSpan(clickSpan, summary.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mImgCache.setText(builder);
    }


    /**
     * 获得Glide保存图片的缓存大小
     *
     * @return
     */
    public String readGlideDiskCacheSize() {
        File file = new File(AppGlideModule.diskCacheFilePath);
        long size = 0;
        for (File cf : file.listFiles()) {
            size += cf.length();
        }
        return Formatter.formatFileSize(getContext(), size);
    }


    /**
     * 清理Glide磁盘缓存
     */
    public void clearGlideDiskCache() {
        new Thread() {
            @Override
            public void run() {
                Glide.get(getContext()).clearDiskCache();
            }
        }.start();
        mImgCache.setText("磁盘缓存大小:  0.0");
    }


    private class SpinnerButtonClick implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            final String selected = (String) mSpinner.getSelectedItem();
            String title = "重新设置页数";
            ForegroundColorSpan fgspan = new ForegroundColorSpan(Color.RED);
            SpannableString spannableString = new SpannableString("重新设置" + selected + "请求页数");
            spannableString.setSpan(fgspan, 4, 4 + selected.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            showCommonDialog(getContext(), title, spannableString, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   performClear(selected);
                }
            }, null);
        }

        public void performClear(final String selected) {
            switch (selected) {
                case "笑话":
                    AccessAppDataHelper.saveInteger(getActivity(), AccessAppDataHelper.KEY_JOKE_PAGE, 1);
                    break;
                case "视频":
                    AccessAppDataHelper.saveInteger(getActivity(), AccessAppDataHelper.KEY_VIDEO_PAGE, 1);
                    break;
                case "学习":
                    AccessAppDataHelper.saveInteger(getActivity(), AccessAppDataHelper.KEY_READ_PAGE, 1);
                    break;
            }

        }
    }


    /**
     * 显示带有确定和取消按钮的对话框
     *
     * @param context          context
     * @param title            标题
     * @param msg              消息
     * @param positiveListener 确定点击监听
     * @param negativeListener 取消点击监听
     */
    public static void showCommonDialog(Context context, CharSequence title, CharSequence msg,
                                        DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        if (context == null) return;
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
                .setNegativeButton("取消", negativeListener)
                .setPositiveButton("确定", positiveListener).show();
    }
}
