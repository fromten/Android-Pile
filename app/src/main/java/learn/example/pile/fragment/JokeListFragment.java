package learn.example.pile.fragment;

import android.os.Bundle;

import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.adapters.JokeListAdapter2;
import learn.example.pile.database.JokeDatabase;
import learn.example.pile.jsonbean.JokeBean;
import learn.example.pile.net.IService;
import learn.example.pile.net.JokeService;

import learn.example.pile.util.DeviceInfo;
import okhttp3.Request;

/**
 * Created on 2016/5/5.
 */
public class JokeListFragment extends BaseListFragment implements IService.Callback<JokeBean> {

    public static final String KEY_SAVE_LIST_DATA="jokedata";

    private JokeListAdapter2 mJokeListAdapter;

    private JokeDatabase mJokeDataBase;

    private JokeService mJokeService;

    private int requestCount=30;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
         super.onViewCreated(view,savedInstanceState);
         mJokeListAdapter=new JokeListAdapter2();
         setAdapter(mJokeListAdapter);
         mJokeService=new JokeService();
         if (savedInstanceState==null)
         {
             setRefreshing(true);
             mJokeService.getTuijianJoke(requestCount,new DeviceInfo(getActivity()).SCREEN_WIDTH,this);
         }else {

             String json=savedInstanceState.getString(KEY_SAVE_LIST_DATA);
             List<JokeBean.DataBean.DataListBean.GroupBean> bean=new Gson().fromJson(json,new TypeToken<ArrayList<JokeBean.DataBean.DataListBean.GroupBean>>(){}.getType());
             mJokeListAdapter.addList(bean);
         }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String json=new Gson().toJson(mJokeListAdapter.getList(), new TypeToken<ArrayList<JokeBean.DataBean.DataListBean.GroupBean>>(){}.getType());
        outState.putString(KEY_SAVE_LIST_DATA,json);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if (mJokeService!=null)
        {
            mJokeService.cancelAll();
        }
        super.onDestroy();
    }

    @Override
    public void onSuccess(JokeBean data) {

        if (isRefreshing())
        {
            mJokeListAdapter.clear();
        }

        List<JokeBean.DataBean.DataListBean.GroupBean> list=new ArrayList<>();
        int count=data.getData().getData().size();
        //不需要第一个
        for (int i = 1; i < count; i++) {
            JokeBean.DataBean.DataListBean listBean=data.getData().getData().get(i);
            if (listBean.getType()!=5)//type==5是广告
                list.add(listBean.getGroup());
        }

        mJokeListAdapter.addList(list);
        notifySuccess();
    }

    @Override
    public void onFailure(String msg) {
         notifyError();
    }

    @Override
    public void onRefresh() {
        mJokeService.getTuijianJoke(requestCount,new DeviceInfo(getActivity()).SCREEN_WIDTH,this);
    }

    @Override
    public void onLoadMore() {
        mJokeService.getTuijianJoke(requestCount,new DeviceInfo(getActivity()).SCREEN_WIDTH,this);
    }


}
