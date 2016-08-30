package learn.example.pile.html;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import learn.example.pile.util.ActivityLauncher;

/**
 * Created on 2016/8/27.
 */
public class VideoClickHandler {
    Context mContext;
    public VideoClickHandler(Context context) {
        mContext=context;
    }

    @JavascriptInterface
    public void openVideo(String url)
    {
        Log.d("video", url);
        ActivityLauncher.startVideoActivity(mContext,url);
    }

    public String videoClickJS()
    {
        return
                "var objs = document.getElementsByClassName(\"videowrap\");\n" +
                "for(var i=0;i<objs.length;i++)\n" +
                "    {\n" +
                "      var imageTag= objs[i].children;\n" +
                "      for(var j=0;j<imageTag.length;j++)\n" +
                "      {\n" +
                "        imageTag[j].onclick=null;\n" +
                "      }\n" +
                "      objs[i].onclick=function()\n" +
                "      {\n" +
                "         var url=this.getAttribute(\"mp4\"); \n" +
                "         VideoClickHandler.openVideo(url)\n" +
                "      }  \n" +
                "      \n" +
                "    }\n";
    }

    public String getName()
    {
        return "VideoClickHandler";
    }
}
