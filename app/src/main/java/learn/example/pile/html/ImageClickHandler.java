package learn.example.pile.html;

import android.content.Context;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import learn.example.pile.util.ActivityLauncher;

/**
 * Created on 2016/8/1.
 */
public class ImageClickHandler {

    private Context mContext;

    public ImageClickHandler(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void openPhotoActivity(String src)
    {

        ActivityLauncher.startPhotoActivityForSingle(mContext,src);
    }

    public String getClickJS()
    {
        return "var objs = document.getElementsByTagName(\"img\");\n" +
                "for(var i=0;i<objs.length;i++)\n" +
                "    {\n" +
                "       objs[i].onclick=function(){\n" +
                "          ImageClickHandler.openPhotoActivity(this.src);\n" +
                "       }\n" +
                "    }";
    }

    public String getName()
    {
        return "ImageClickHandler";
    }
}
