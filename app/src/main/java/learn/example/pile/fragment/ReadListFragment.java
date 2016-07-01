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
import learn.example.pile.util.AccessAppDataHelper;

/**
 * Created on 2016/6/3.
 */
public class ReadListFragment extends BaseListFragment implements {



    private ReadListAdapter mAdapter;
    private static final String TAG="ReadListFragment";

    //最大请求个数
    private final int MAX_REQNUM=10;
    //现在页数
    private int currentPage=0;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAdapter = new ReadListAdapter();
        setRecyclerAdapter(mAdapter);

        //读取保存的页数
        currentPage = AccessAppDataHelper.readInteger(getActivity(), AccessAppDataHelper.KEY_READ_PAGE);
        if (savedInstanceState != null) {
            List<GankCommonJson.ResultsBean> saveData = savedInstanceState.getParcelableArrayList(KEY_READ_SAVE_STATE);
            mAdapter.addAllItem(saveData);

        } else {
            startRefresh();
            requestData(MAX_REQNUM, currentPage);
        }
    }





}
