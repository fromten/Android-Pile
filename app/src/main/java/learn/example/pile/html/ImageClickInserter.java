package learn.example.pile.html;

import android.content.Context;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import learn.example.pile.util.ActivityLauncher;

/**
 * Created on 2016/8/1.
 */
public class ImageClickInserter  implements JavaScriptInserter{

    private Context mContext;

    public ImageClickInserter(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void openPhotoActivity(String src)
    {
        ActivityLauncher.startPhotoActivityForSingle(mContext,src);
    }


    public String getName()
    {
        return "ImageClickInserter";
    }

    @Override
    public String getJavaScript() {
        return "var objs = document.getElementsByTagName(\"img\");\n" +
                "for(var i=0;i<objs.length;i++)\n" +
                "    {\n" +
                "       objs[i].onclick=function(){\n" +
                "          ImageClickInserter.openPhotoActivity(this.src);\n" +
                "       }\n" +
                "    }";
    }
}
