package learn.example.pile.html.plugin;

import android.content.Context;
import android.webkit.JavascriptInterface;

import learn.example.pile.util.ActivityLauncher;

/**
 * Created on 2016/8/27.
 */
public class VideoClickPlugin extends ContextPlugin {


    public VideoClickPlugin(Context context) {
        super(context);
    }

    @JavascriptInterface
    public void openVideo(String url)
    {
        Context context=mWeakReference.get();
        if (context!=null)
            ActivityLauncher.startVideoActivity(context,url);
    }

    public String getJavaScript()
    {
        return "var objs = document.getElementsByClassName(\"videowrap\");\n" +
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
                "         VideoClickPlugin.openVideo(url)\n" +
                "      }  \n" +
                "    }\n";
    }

    public String getName()
    {
        return "VideoClickPlugin";
    }

}
