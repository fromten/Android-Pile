package learn.example.pile.activity.normal.reader;

import android.support.v4.app.Fragment;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;

import learn.example.pile.fragment.comment.ZhihuCommentFragment;
import learn.example.pile.html.ImageClickInserter;
import learn.example.pile.html.JavaScriptInserter;
import learn.example.pile.html.ZhihuHtml;
import learn.example.pile.jsonbean.ZhihuNewsContent;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuContentService;

/**
 * Created on 2016/9/20.
 */
public class ZhihuRenderer implements ContentRenderer
{
    private int zhihuDocId;
    private ZhihuContentService mService;
    private ReaderActivity mReaderActivity;
    @Override
    public void onActivityCreated(ReaderActivity activity) {
        mReaderActivity=activity;
        zhihuDocId=mReaderActivity.getIntent().getIntExtra(ReaderActivity.KEY_ZHIHU_CONTENT_ID,0);

        requestHtml();
    }

    @Override
    public void onActivityDestroy() {
        if (mService!=null)
        {
            mService.cancelAll();
        }
        mReaderActivity=null;
    }

    @Override
    public boolean onHasCommentMenu() {
        return true;
    }

    @Override
    public Fragment onCreateCommentFragment() {
        return ZhihuCommentFragment.newInstance(zhihuDocId);
    }


    public void requestHtml()
    {
        mService=new ZhihuContentService();
        mService.getContent(zhihuDocId, new IService.Callback<ZhihuNewsContent>() {
            @Override
            public void onSuccess(ZhihuNewsContent data) {

                final String imgSource=data.getImageSource();
                Glide.with(mReaderActivity).load(data.getImage())
                        .fitCenter()
                        .into(new ImageViewTarget<GlideDrawable>(mReaderActivity.getToolbarImageView()) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        getView().setImageDrawable(resource);
                        mReaderActivity.getToolbarTextView().setText(imgSource);
                    }
                });
                WebView webView=mReaderActivity.getWebView();
                webView.getSettings().setJavaScriptEnabled(true);


                TagClickInsert tagClickInsert=new TagClickInsert();
                webView.addJavascriptInterface(tagClickInsert,tagClickInsert.getName());

                //添加图片点击监听
                ImageClickInserter imageClickInserter =new ImageClickInserter(mReaderActivity);
                webView.addJavascriptInterface(imageClickInserter, imageClickInserter.getName());

                ZhihuHtml html=new ZhihuHtml(data.getBody(),data.getCss(),data.getJs());
                String aTagClickJs=tagClickInsert.getJavaScript();
                String imageClickJs=imageClickInserter.getJavaScript();
                html.setJs(imageClickJs+"\n"+aTagClickJs);

                mReaderActivity.onRenderCompleted(html.generateHtml(),false);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }



    /**
     * 替换知乎页面的评论点击
     */
    public class TagClickInsert implements JavaScriptInserter
    {

        @JavascriptInterface()
        public void showCommentFragment()
        {
            mReaderActivity.showCommentFragment();
        }

        @Override
        public String getName() {
            return "TagClickInsert";
        }

        @Override
        public String getJavaScript() {
            return  "var aTag=document.getElementsByTagName('a');\n" +
                    "aTag=aTag[aTag.length-1];\n" +
                    "aTag.removeAttribute('href');" +
                    "aTag.setAttribute('onClick',\"showAndroidComment()\");\n" +
                    "\n" +
                    "function showAndroidComment()\n" +
                    "{\n" +
                    "  TagClickInsert.showCommentFragment();\n" +
                    "}\n";
        }
    }

}
