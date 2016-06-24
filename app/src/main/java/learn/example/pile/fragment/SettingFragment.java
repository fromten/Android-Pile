package learn.example.pile.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import learn.example.joke.R;
import learn.example.pile.AppGlideModule;

/**
 * Created on 2016/5/6.
 */
public class SettingFragment extends Fragment {
    public static final String TAG = "SettingFragment";
    private TextView picCache;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_setting,container,false);
        picCache= (TextView) v.findViewById(R.id.pic_cache);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        picCache.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog=new AlertDialog.Builder(getContext())
                            .setTitle("清除图片缓存")
                            .setMessage("确定清除图片缓存吗?")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Thread()
                                    {
                                        @Override
                                        public void run() {
                                            Glide.get(getContext()).clearDiskCache();
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getContext(),"清除完成",Toast.LENGTH_SHORT)
                                                            .show();
                                                    picCache.setText(" ");
                                                }
                                            });
                                        }
                                    }.start();
                                }
                            }).create();
                    dialog.show();
                }
            });
    }

    @Override
    public void onResume() {
        super.onResume();
        //转换成字符串,在取小数点后一位;
        String size= readDiskCacheSize();
        String html="<p>磁盘缓存大小:      <b><font color='#00bbaa'>"+size+"</b></p>";
        picCache.setText(Html.fromHtml(html));

    }

    public String readDiskCacheSize()
    {
        File file=  new File(AppGlideModule.diskCacheFilePath);
        long size = 0;
        for (File cf:file.listFiles())
        {
            size+=cf.length();
        }
        return Formatter.formatFileSize(getContext(),size);
    }
}
