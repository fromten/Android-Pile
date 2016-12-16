package learn.example.pile.fragment.caterogy;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.View;

import learn.example.pile.adapters.JokeListAdapter;
import learn.example.pile.fragment.base.BaseListFragment;
import learn.example.pile.net.IService;
import learn.example.pile.net.JokeService;

import learn.example.pile.pojo.Joke;
import learn.example.pile.util.DeviceInfo;

/**
 * Created on 2016/5/5.
 */
public class JokeListFragment extends BaseListFragment implements IService.Callback<Joke> {



    private JokeListAdapter mJokeListAdapter;


    private JokeService mJokeService;

    private int requestCount=30;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
         super.onViewCreated(view,savedInstanceState);
         mJokeListAdapter=new JokeListAdapter(getActivity());
         setAdapter(mJokeListAdapter);
         mJokeService=new JokeService();
         if (savedInstanceState==null)
         {
             setRefreshing(true);
             mJokeService.getTuijianJoke(requestCount,new DeviceInfo(getActivity()).SCREEN_WIDTH,this);
         }

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState!=null&&mJokeListAdapter.getItemCount()<=0) {
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
    public void onSuccess(Joke data) {
        if (isRefreshing())
        {
            mJokeListAdapter.clear();
        }

        int count=data.getJokeItems().size();
        if (count<=0)
        { //数据为空,当做失败处理
            notifyError();
            return;
        }
        mJokeListAdapter.addAll(data.getJokeItems());
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
