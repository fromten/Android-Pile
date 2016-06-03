package learn.example.pile;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * Created on 2016/6/3.
 */
public class AppGlideModule implements GlideModule {

    public static final String diskCacheName="img_cache";
    public static  String diskCacheFilePath;
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int byte300mb=314572800;
        diskCacheFilePath=getFile(context).getPath()+"/"+diskCacheName;
        builder.setDiskCache(new DiskLruCacheFactory(diskCacheFilePath,byte300mb));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {


    }
    public File getFile(Context context)
    {
        String state = Environment.getExternalStorageState();
        File file;
        if (state.equals(Environment.MEDIA_MOUNTED))
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
