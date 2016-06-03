package learn.example.pile.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
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
import learn.example.pile.net.NetRequestQueue;
import learn.example.pile.net.ParseHtmlManager;

/**
 * Created on 2016/5/25.
 */
public class VideoListFragment extends RecyclerViewFragment implements Response.ErrorListener,Response.Listener<VideoJsonData>
                                                                    ,ParseHtmlManager.ParserCompleteListener{

    private VideoListAdapter mVideoListAdapter;
    private RequestQueue volleyRequestQueue;
    private ParseHtmlManager mParseHtmlManager;
    private static final String TAG="VideoListFragment";

    public static final String KEY_VIDEO_PAGE="KEY_VIDEO_PAGE";
    public static final String KEY_STATE_SAVE="KEY_VIDEO_PAGE";
    private static final int MAXREQNUM=5;
    private int currentPage;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mVideoListAdapter=new VideoListAdapter();
        setRecyclerAdapter(mVideoListAdapter);
        volleyRequestQueue= NetRequestQueue.getInstance(getContext()).getRequestQueue();
        currentPage=readPage();
        mParseHtmlManager=new ParseHtmlManager(this);
        if (savedInstanceState!=null)
        {
            List<VideoJsonData.VideoItem> list=savedInstanceState.getParcelableArrayList(KEY_STATE_SAVE);
            mVideoListAdapter.addAllItem(list);
        }else {
            requestNetData(MAXREQNUM,currentPage);
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
         outState.putParcelableArrayList(KEY_STATE_SAVE, (ArrayList<? extends Parcelable>) mVideoListAdapter.getItemList());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        volleyRequestQueue.cancelAll(TAG);
        savePage(currentPage);
        mParseHtmlManager.destroy();
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
               mParseHtmlManager.addParse(response);//执行解析获得视频和图片地址
        }
        stopRefresh();
    }


    //请求失败
    @Override
    public void onErrorResponse(VolleyError error) {
          stopRefresh();
         Toast.makeText(getContext(),"网络请求失败",Toast.LENGTH_SHORT).show();
    }


    //解析完成
    @Override
    public  void ParserComplete(VideoJsonData.VideoItem item) {
        int len=mVideoListAdapter.getSelfItemSize();
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
            requestNetData(MAXREQNUM,currentPage);
    }

    @Override
    public void pullDownRefresh() {
          mVideoListAdapter.clearAll();
          requestNetData(MAXREQNUM,currentPage);
    }


    /**
     *
     * @param reqnum: 请求个数
     * @param page:请求页数
     */
    public void requestNetData(int reqnum,int page)
    {
        startRefresh();
        try {
            String type=URLEncoder.encode("休息视频","utf-8");
            String url= MyURI.VIDEO_DATA_REQUEST_URL+type+"/"+reqnum+"/"+page;
            GsonRequest<VideoJsonData> request=new GsonRequest<>
                    (url,VideoJsonData.class,this,this);
            request.setTag(TAG);
            volleyRequestQueue.add(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //保存页数
    public void savePage(int page)
    {
        SharedPreferences sp=getActivity().getPreferences(Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_VIDEO_PAGE,page).apply();
    }
    //读取页数
    public int readPage()
    {
        SharedPreferences sp=getActivity().getPreferences(Context.MODE_PRIVATE);
        return sp.getInt(KEY_VIDEO_PAGE,1);
    }


}
