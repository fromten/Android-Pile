package learn.example.pile.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.MyURI;
import learn.example.pile.adapters.ReadListAdapter;
import learn.example.pile.jsonobject.GankCommonJson;
import learn.example.pile.net.GsonRequest;
import learn.example.pile.net.VolleyRequestQueue;

/**
 * Created on 2016/6/3.
 */
public class ReadListFragment extends RecyclerViewFragment implements Response.ErrorListener,Response.Listener<GankCommonJson>{

    public static final String KEY_READ_SAVE_STATE="KEYREADSAVESTATE";
    public static final String KEY_READ_SAVE_PAGE="KEYREADSAVEPAGE";

    private ReadListAdapter mAdapter;
    private static final String TAG="ReadListFragment";
    private final int MAX_REQNUM=15;
    private int currentPage;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
          mAdapter=new ReadListAdapter();
          setRecyclerAdapter(mAdapter);
          currentPage=readLastHistoryPage();
          if (savedInstanceState!=null)
          {
              List<GankCommonJson.ResultsBean> saveData=savedInstanceState.getParcelableArrayList(KEY_READ_SAVE_STATE);
              mAdapter.addAllItem(saveData);
          }else
          {   startRefresh();
              requestData(MAX_REQNUM,currentPage);
          }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_READ_SAVE_STATE, (ArrayList<? extends Parcelable>) mAdapter.getList());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        VolleyRequestQueue.getInstance(getContext()).cancelAll(TAG);
        mAdapter=null;
        super.onDestroy();
    }

    @Override
    public void pullUpRefresh() {
          requestData(MAX_REQNUM,readLastHistoryPage());
    }

    @Override
    public void pullDownRefresh() {
         mAdapter.clearItems();
         startRefresh();
         requestData(MAX_REQNUM,readLastHistoryPage());
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        refreshFail();
        if (mAdapter.getList().isEmpty())
        {
            setEmptyViewText("数据飞走了");
        }
    }

    @Override
    public void onResponse(GankCommonJson response) {
         if (response!=null&&!response.isError())
         {
             mAdapter.addAllItem(response.getResults());
             saveLastHistoryPage(++currentPage);
         }
         refreshComplete();
    }

    public void requestData(int num, int page)
    {
        setEmptyViewText(null);
        String type="Android";
        String url= MyURI.GANK_DATA_REQUEST_URL+type+"/"+num+"/"+page;
        GsonRequest<GankCommonJson> request=new GsonRequest<>(url,GankCommonJson.class,this,this,false);
        request.setTag(TAG);
        VolleyRequestQueue.getInstance(getContext()).addToRequestQueue(request);
    }


    public void saveLastHistoryPage(int page)
    {
        SharedPreferences p=getActivity().getPreferences(Context.MODE_PRIVATE);
        p.edit().putInt(KEY_READ_SAVE_PAGE,page).apply();
    }

    //读取最后历史页数
    public int readLastHistoryPage()
    {
        SharedPreferences p=getActivity().getPreferences(Context.MODE_PRIVATE);
        return p.getInt(KEY_READ_SAVE_PAGE,1);
    }
}
