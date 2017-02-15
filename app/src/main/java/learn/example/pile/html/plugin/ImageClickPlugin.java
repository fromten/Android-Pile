package learn.example.pile.html.plugin;

import android.content.Context;
import android.webkit.JavascriptInterface;

import learn.example.pile.util.ActivityLauncher;

/**
 * Created on 2016/8/1.
 */
public class ImageClickPlugin extends ContextPlugin {


    public ImageClickPlugin(Context context) {
        super(context);
    }

    @JavascriptInterface
    public void openPhotoActivity(String src)
    {
        Context context=mWeakReference.get();
        if (context!=null)
            ActivityLauncher.startPhotoActivityForSingle(context,src);
    }


    public String getName()
    {
        return "ImageClickPlugin";
    }

    @Override
    public String getJavaScript() {
        return "var objs = document.getElementsByTagName(\"img\");\n" +
                "for(var i=0;i<objs.length;i++)\n" +
                "    {\n" +
                "       objs[i].onclick=function(){\n" +
                        getName()+".openPhotoActivity(this.src);\n" +
                "       }" +
                "    }";
    }
}
