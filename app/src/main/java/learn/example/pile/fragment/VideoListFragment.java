package learn.example.pile.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import learn.example.pile.MyURI;
import learn.example.pile.adapters.VideoListAdapter;
import learn.example.pile.jsonobject.VideoJsonData;
import learn.example.pile.net.GsonRequest;
import learn.example.pile.net.VolleyRequestQueue;
import learn.example.pile.net.VideoHtmlParser;

/**
 * Created on 2016/5/25.
 */
public class VideoListFragment extends RecyclerViewFragment implements Response.ErrorListener,Response.Listener<VideoJsonData>
                                                                    ,VideoHtmlParser.ParserCompleteListener{

    private VideoListAdapter mVideoListAdapter;
    private VideoHtmlParser mVideoHtmlParser;
    private static final String TAG="VideoListFragment";

    public static final String KEY_VIDEO_HIRSTORY_PAGE ="KEYVIDEOHIRSTORYPAGE";
    public static final String KEY_VIDEO_STATE_SAVE="KEYVIDEOSTATESAVE";
    private static final int MAXREQNUM=5;
    private int currentPage;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mVideoListAdapter=new VideoListAdapter();
        setRecyclerAdapter(mVideoListAdapter);
        currentPage=readPage();
        mVideoHtmlParser =new VideoHtmlParser(this);
        if (savedInstanceState!=null)
        {
            List<VideoJsonData.VideoItem> list=savedInstanceState.getParcelableArrayList(KEY_VIDEO_STATE_SAVE);
            mVideoListAdapter.addAllItem(list);
        }else {
            startRefresh();
            requestData(MAXREQNUM,currentPage);
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
         outState.putParcelableArrayList(KEY_VIDEO_STATE_SAVE, (ArrayList<? extends Parcelable>) mVideoListAdapter.getItemList());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        VolleyRequestQueue.getInstance(getContext()).cancelAll(TAG);

        savePage(currentPage);
        mVideoHtmlParser.destroy();
        mVideoListAdapter=null;
        super.onDestroy();
    }

    //请求成功
    @Override
    public void onResponse(VideoJsonData response) {
        if(response!=null&&!response.isError())
        {
               mVideoListAdapter.addAllItem(response.getVideoItemList());
               savePage(currentPage);//保存页数
               currentPage++;//再把当前页数增加
               mVideoHtmlParser.addParse(response);//执行解析获得视频和图片地址
        }
        refreshComplete();
    }


    //请求失败
    @Override
    public void onErrorResponse(VolleyError error) {
        if (mVideoListAdapter.getItemSize()==0)
        {
            setEmptyViewText("数据飞走了");
        }
        refreshFail();
    }

    //解析完成
    @Override
    public  void ParserComplete(VideoJsonData.VideoItem item) {
        int len=mVideoListAdapter.getItemSize();
        if (item==null||len<0)
        {
            return;
        }
        //每次只会解析后5个数据
        for (int i=len-1;i>=len-MAXREQNUM&&i>=0;i--)
        {
            VideoJsonData.VideoItem vi=mVideoListAdapter.getItem(i);
            if(vi.equals(item))
            {
                vi.setImgUrl(item.getImgUrl());
                vi.setFileUrl(item.getFileUrl());
                mVideoListAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void pullUpRefresh() {
            requestData(MAXREQNUM,currentPage);
    }

    @Override
    public void pullDownRefresh() {
          startRefresh();
          mVideoListAdapter.clearAll();
          requestData(MAXREQNUM,currentPage);
    }


    /**
     *
     * @param reqnum: 请求个数
     * @param page:请求页数
     */
    public void requestData(int reqnum, int page)
    {
        setEmptyViewText(null);
        try {
            String type=URLEncoder.encode("休息视频","utf-8");
            String url= MyURI.GANK_DATA_REQUEST_URL +type+"/"+reqnum+"/"+page;
            GsonRequest<VideoJsonData> request=new GsonRequest<>
                    (url,VideoJsonData.class,this,this,false);
            request.setTag(TAG);
            VolleyRequestQueue.getInstance(getContext()).addToRequestQueue(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //保存页数
    public void savePage(int page)
    {
        SharedPreferences sp=getActivity().getPreferences(Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_VIDEO_HIRSTORY_PAGE,page).apply();
    }
    //读取页数
    public int readPage()
    {
        SharedPreferences sp=getActivity().getPreferences(Context.MODE_PRIVATE);
        return sp.getInt(KEY_VIDEO_HIRSTORY_PAGE,1);
    }


}
