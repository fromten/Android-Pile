package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.activeandroid.query.Select;

import learn.example.pile.R;
import learn.example.pile.database.model.NewsArticle;
import learn.example.pile.fragment.comment.NetEaseCommentFragment;
import learn.example.pile.fragment.op.FragmentActor;
import learn.example.pile.fragment.op.SimpleAnimFragmentActor;
import learn.example.pile.html.Html;
import learn.example.pile.html.plugin.ImageClickPlugin;
import learn.example.pile.html.NetEaseHtml;
import learn.example.pile.html.plugin.VideoClickPlugin;
import learn.example.pile.net.IService;
import learn.example.pile.net.NetEaseNewsService;
import learn.example.pile.util.HtmlBuilder;

/**
 * Created on 2016/10/25.
 */

public class NetEaseReaderFragment extends WebViewFragment implements IService.Callback<String> {

    public static final String ARGUMENT_DOCID ="ARGUMENT_DOCID";
    public static final String ARGUMENT_BOARD_ID ="ARGUMENT_BOARD_ID";
    private static final String TAG_COMMENT_FRAGMENT="NetEaseReaderFragment_TAG_COMMENT_FRAGMENT";
    private static final String STATE_HTML="STATE_HTML";

    private String docId;
    private String boardId;
    private NetEaseNewsService mNewsService;
    private FragmentActor mFragmentActor;
    private DatabaseOperation mDatabaseOperation;
    private String mHtml;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        setBackUpListener();
        bindArgument();
        mFragmentActor=new SimpleAnimFragmentActor(getFragmentManager());
        mDatabaseOperation=new DatabaseOperation();
        if (savedInstanceState==null)
        {
            readOrRequest();
        }else{
            mHtml=savedInstanceState.getString(STATE_HTML);
            String completeHtml=insertListener(mHtml);
            loadLocalDataWithDefCss(completeHtml);
        }
    }

    private void readOrRequest()
    {
        String historicalHtml=mDatabaseOperation.readArticle(docId);
        if (historicalHtml!=null)
        {
            String completeHtml=insertListener(historicalHtml);
            loadLocalDataWithDefCss(completeHtml);
            mHtml=historicalHtml;
        }else {
            mNewsService=new NetEaseNewsService();
            mNewsService.getArticle(docId,this);
        }
    }

    private void bindArgument()
    {
        Bundle args=getArguments();
        if (args!=null)
        {
            docId=args.getString(ARGUMENT_DOCID);
            boardId=args.getString(ARGUMENT_BOARD_ID);
        }
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
        Html netEaseHtml=new NetEaseHtml(docId,data);
        String originHtml=netEaseHtml.getHtml();
        String completeHtml= insertListener(originHtml);
        loadLocalDataWithDefCss(completeHtml);
        mHtml=originHtml;

        mDatabaseOperation.saveArticle(docId,originHtml);
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_HTML,mHtml);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if (mNewsService!=null)
        {
            mNewsService.cancelAll();
        }
        super.onDestroy();
    }


    private String addImageClickJs(WebView webView)
    {
        ImageClickPlugin imageHandler=new ImageClickPlugin(getActivity());
        webView.addJavascriptInterface(imageHandler,imageHandler.getName());
        return HtmlBuilder.tag("script","type='text/javascript'",imageHandler.getJavaScript());
    }

    private String addVideoClickJs(WebView webView)
    {
        VideoClickPlugin videoHandler=new VideoClickPlugin(getActivity());
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

    public static class DatabaseOperation{

        public void saveArticle(String docId,String article)
        {
            //保存到数据库
            NewsArticle database=new NewsArticle();
            database.docid=docId;
            database.html=article;
            database.save();
        }

        public String readArticle(String docId)
        {
            if (docId==null)return null;

            NewsArticle article=new Select().from(NewsArticle.class)
                                            .where("docid= ?",docId)
                                            .executeSingle();
            boolean isValid=article!=null&&article.html!=null&&article.html.length()>0;
            return isValid?article.html:null;
        }
    }
}
