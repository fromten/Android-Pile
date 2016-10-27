package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import learn.example.pile.R;
import learn.example.pile.database.model.NewsArticle;
import learn.example.pile.fragment.comment.NetEaseCommentFragment;
import learn.example.pile.fragment.op.SimpleFragmentActor;
import learn.example.pile.html.Html;
import learn.example.pile.html.ImageClickInserter;
import learn.example.pile.html.NetEaseHtml;
import learn.example.pile.html.VideoClickInserter;
import learn.example.pile.net.IService;
import learn.example.pile.net.NetEaseNewsService;
import learn.example.pile.util.HtmlBuilder;

/**
 * Created on 2016/10/25.
 */

public class NetEaseReaderFragment extends WebViewFragment implements IService.Callback<String> {

    public static final String KEY_DOCID="net_ease_doc_id";
    public static final String KEY_BOARD_ID="net_ease_board_id";
    private static final String TAG_COMMENT_FRAGMENT="comment_fragment_tag";

    private String docId;
    private String boardId;
    private NetEaseNewsService mNewsService;
    private SimpleFragmentActor mFragmentActor;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        setBackUpListener();
        Bundle args=getArguments();
        if (args!=null)
        {
            docId=args.getString(KEY_DOCID);
            boardId=args.getString(KEY_BOARD_ID);
        }
        mNewsService=new NetEaseNewsService();
        if (savedInstanceState==null)
        {
            mNewsService.getArticle(docId,this);
        }
        mFragmentActor=new SimpleFragmentActor(getFragmentManager());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.reader_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         if (item.getItemId()==R.id.menu_comment)
         {
             int layoutId=getContainerViewId();
             Fragment fragment=getCommentFragment();
             if (fragment==null)
             {
                 fragment= NetEaseCommentFragment.newInstance(docId,boardId);
                 mFragmentActor.add(layoutId,fragment,TAG_COMMENT_FRAGMENT);
             }else if (fragment.isHidden()){
                 mFragmentActor.show(fragment);
             }
             return true;
         }
         return super.onOptionsItemSelected(item);
    }

    public void setBackUpListener()
    {
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_BACK)
                {
                    Fragment fragment=getCommentFragment();
                    if (fragment!=null&&fragment.isVisible())
                    {
                        mFragmentActor.hide(fragment);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private Fragment getCommentFragment()
    {
        return getFragmentManager().findFragmentByTag(TAG_COMMENT_FRAGMENT);
    }

    private int  getContainerViewId()
    {
        View rootContainer= (View) getView().getParent();
        return rootContainer.getId();
    }

    @Override
    public void onSuccess(String data) {

        Html html=new NetEaseHtml(docId,data);
        String originHtml=html.getHtml();
        String completeHtml= insertListener(originHtml);
        loadLocalDataWithDefCss(completeHtml);

        //保存到数据库
        NewsArticle article=new NewsArticle();
        article.docid=docId;
        article.html=originHtml;
        article.save();
    }

    @Override
    public void onFailure(String message) {

    }

    private String addImageClickJs(WebView webView)
    {
        ImageClickInserter imageHandler=new ImageClickInserter(getActivity());
        webView.addJavascriptInterface(imageHandler,imageHandler.getName());
        return HtmlBuilder.tag("script","type='text/javascript'",imageHandler.getJavaScript());
    }

    private String addVideoClickJs(WebView webView)
    {
        VideoClickInserter videoHandler=new VideoClickInserter(getActivity());
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
        String imageClickJs= addImageClickJs(getWebView());
        String videoClickJs=addVideoClickJs(getWebView());
        return originHtml+imageClickJs+videoClickJs;
    }
}
