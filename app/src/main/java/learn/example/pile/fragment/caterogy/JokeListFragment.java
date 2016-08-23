package learn.example.pile.fragment.caterogy;

import android.os.Bundle;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.adapters.JokeListAdapter;
import learn.example.pile.database.JokeDatabase;
import learn.example.pile.fragment.base.BaseListFragment;
import learn.example.pile.jsonbean.JokeBean;
import learn.example.pile.net.IService;
import learn.example.pile.net.JokeService;

import learn.example.pile.util.DeviceInfo;

/**
 * Created on 2016/5/5.
 */
public class JokeListFragment extends BaseListFragment implements IService.Callback<JokeBean> {



    private JokeListAdapter mJokeListAdapter;

    private JokeDatabase mJokeDataBase;

    private JokeService mJokeService;

    private int requestCount=30;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
         super.onViewCreated(view,savedInstanceState);
         mJokeListAdapter=new JokeListAdapter();
         setAdapter(mJokeListAdapter);
         mJokeService=new JokeService();
         if (savedInstanceState==null)
         {
             setRefreshing(true);
             mJokeService.getTuijianJoke(requestCount,new DeviceInfo(getActivity()).SCREEN_WIDTH,this);
         }

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