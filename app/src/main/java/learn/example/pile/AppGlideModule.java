package learn.example.pile;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;


import java.io.File;

import learn.example.pile.util.DeviceInfo;

/**
 * Created on 2016/6/3.
 */
public class AppGlideModule implements GlideModule {

    public static final String DISK_CACHE_NAME ="img_cache";
    public static String sDiskCacheFilePath;


    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int byte300mb=300*1024*1024;
        sDiskCacheFilePath =getStoreFile(context).getPath()+"/"+ DISK_CACHE_NAME;
        builder.setDiskCache(new DiskLruCacheFactory(sDiskCacheFilePath,byte300mb));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
    public File getStoreFile(Context context)
    {
        File file;
        if (DeviceInfo.checkExternalStorageState())
        {
            if (Build.VERSION.SDK_INT>=23&&context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
            {
                file=context.getCacheDir();
            }else {
                file=context.getExternalCacheDir();
            }
        }else {
            file=context.getCacheDir();
        }
        return file;
    }
}
