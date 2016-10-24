package learn.example.pile.activity.normal.reader;

import android.support.v4.app.Fragment;
import android.webkit.WebView;

import com.activeandroid.query.Select;

import java.util.Locale;

import learn.example.net.OkHttpRequest;
import learn.example.pile.database.model.NewsArticle;
import learn.example.pile.fragment.comment.NetEaseCommentFragment;
import learn.example.pile.html.Html;
import learn.example.pile.html.ImageClickInserter;
import learn.example.pile.html.NetEaseHtml;
import learn.example.pile.html.VideoClickInserter;
import learn.example.pile.provider.NetEase;
import learn.example.pile.util.HtmlBuilder;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created on 2016/9/20.
 */
public class NetEaseRenderer  implements ContentRenderer {
    private String docid;
    private String boardid;
    private Call mCall;
    private ReaderActivity mReaderActivity;

    @Override
    public void onActivityCreated(ReaderActivity activity) {
        mReaderActivity=activity;
        String[] array=activity.getIntent().getStringArrayExtra(ReaderActivity.KEY_NETEASE_CONTENT_ID);
        this.boardid =array[0];
        this.docid =array[1];
        String historyHtml=readHistoricalData();


        if (historyHtml!=null)
        {
            mReaderActivity.onRenderCompleted(insertListener(historyHtml),true);
        }else {
            requestHtml();
        }

    }

    public String readHistoricalData()
    {
       NewsArticle article= new Select().from(NewsArticle.class)
                .where("docid= ?",docid)
                .executeSingle();
       return article==null?null:article.html;
    }

    @Override
    public void onActivityDestroy() {
         if (mCall!=null)
         {
             mCall.cancel();
         }
        mReaderActivity=null;
    }

    @Override
    public boolean onHasCommentMenu() {
        return true;
    }

    @Override
    public Fragment onCreateCommentFragment() {
        return  NetEaseCommentFragment.newInstance(docid,boardid);
    }

    public void requestHtml()
    {
        Request request=new Request.Builder()
                .url(String.format(Locale.CHINA, NetEase.ARTICLE_URL2, docid))
                .build();
        mCall= OkHttpRequest.getInstanceUnsafe().newStringRequest(request, new OkHttpRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String res) {

                Html html=new NetEaseHtml(docid,res);
                String originHtml=html.getHtml();
                String completeHtml= insertListener(originHtml);

                mReaderActivity.onRenderCompleted(completeHtml,true);

                NewsArticle article=new NewsArticle();
                article.docid=docid;
                article.html=originHtml;
                article.save();
            }
            @Override
            public void onFailure(String msg) {

            }
        });
    }

    private String addImageClickJs(WebView webView)
    {
        ImageClickInserter imageHandler=new ImageClickInserter(mReaderActivity);
        webView.addJavascriptInterface(imageHandler,imageHandler.getName());
        return HtmlBuilder.tag("script","type='text/javascript'",imageHandler.getJavaScript());
    }

    private String addVideoClickJs(WebView webView)
    {
        VideoClickInserter videoHandler=new VideoClickInserter(mReaderActivity);
        webView.addJavascriptInterface(videoHandler,videoHandler.getName());
        return HtmlBuilder.tag("script","type='text/javascript'",videoHandler.getJavaScript());
    }


    /**
     * 生成包含 图片点击事件和视频点击事件的Html
     * @param originHtml 原来的Html
     * @return 新的Html
     */
    private String insertListener(String originHtml)
    {
        mReaderActivity.getWebView().getSettings().setJavaScriptEnabled(true);
        String imageClickJs= addImageClickJs(mReaderActivity.getWebView());
        String videoClickJs=addVideoClickJs(mReaderActivity.getWebView());
        return originHtml+imageClickJs+videoClickJs;
    }

}
