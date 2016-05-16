package learn.example.joke.ui;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.model.GlideUrl;

import java.io.File;
import java.io.IOException;

import learn.example.joke.MainActivity;
import learn.example.joke.R;

/**
 * Created on 2016/5/6.
 */
public class SettingFragment extends Fragment {

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
        String size= String.valueOf(readDiskCacheSize()/1024.0/1024.0);
        int index=size.indexOf(".");
        size=size.substring(0,index+2)+" mb";
        picCache.setText(size);
    }

    public long readDiskCacheSize()
    {
        File file=Glide.getPhotoCacheDir(getContext());
        long size=0;
        try {
            DiskLruCache cache=DiskLruCache.open(file,1,1,DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
            size=cache.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }
}
