package learn.example.pile.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.Formatter;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import learn.example.pile.R;
import learn.example.pile.AppGlideModule;
import learn.example.pile.ui.Messages;

/**
 * Created on 2016/5/6.
 */
public class SettingFragment extends Fragment {
    public static final String TAG = "SettingFragment";
    private TextView mImgCache;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        mImgCache = (TextView) v.findViewById(R.id.img_cache);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setCacheSizeString(readGlideDiskCacheSize());
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
                Messages.showMessages(getContext(), "清除缓存", "确定清楚图片缓存?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==DialogInterface.BUTTON_POSITIVE)
                        {
                            clearGlideDiskCache();
                        }
                    }
                });
                mImgCache.setSelected(true);
            }
        };
        mImgCache.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableStringBuilder builder = SpannableStringBuilder.valueOf(summary+size);
        builder.setSpan(clickSpan, summary.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mImgCache.setText(builder);
    }


    /**
     * 获得Glide保存图片的缓存大小
     *
     * @return
     */
    public String readGlideDiskCacheSize() {
        File file = new File(AppGlideModule.sDiskCacheFilePath);
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
}
