package learn.example.pile.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.List;

import learn.example.pile.NewsActivity;
import learn.example.joke.R;
import learn.example.pile.adapters.NewsListAdapter;
import learn.example.pile.jsonobject.BaseNewsData;
import learn.example.pile.net.NewsRequestTask;

/**
 * Created on 2016/5/7.
 */
public class NewsFragment extends RecyclerViewFragment implements NewsRequestTask.TaskCompleteListener {


    private NewsListAdapter mNewsListAdapter;
    private NewsRequestTask mNewsRequestTask;

    private String TAG="NewsFragment";
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNewsListAdapter=new NewsListAdapter(getContext());
        setRecyclerAdapter(mNewsListAdapter);
        correctReqData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mNewsRequestTask!=null)
            mNewsRequestTask.setNewsCompleteListener(this);
    }


    @Override
    public void onPause() {
        if(mNewsRequestTask!=null)
        mNewsRequestTask.setNewsCompleteListener(null);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void taskComplete(List<BaseNewsData> data) {
         mNewsListAdapter.addAllItem(data);
         setRefreshing(false);
    }

    @Override
    public void taskFail(String msg) {
        Log.i(TAG,msg);
        setRefreshing(false);
        if(mNewsListAdapter.getItemSize()==0)
            setEmptyViewText("数据飞走了");
    }

    @Override
    public void pullUpRefresh(RecyclerView recyclerView) {
         correctReqData();
    }

    @Override
    public void onRefresh() {
        mNewsListAdapter.clearItem();
         correctReqData();
    }

    @Override
    public void onItemClick(RecyclerView recyclerView,View view, int position) {
//        String link= (String) view.getTag(R.id.link);
//        Intent intent=new Intent(getActivity(),NewsActivity.class);
//        intent.putExtra(NewsActivity.LINK_KEY,link);
//        startActivity(intent);
//
//          String url=mNewsListAdapter.getItem(position).getNewsUrl();
//          Intent intent=new Intent(Intent.ACTION_VIEW);
//          intent.setData(Uri.parse(url));
//          startActivity(intent);
        System.out.println(position);
    }

    public void correctReqData()
    {
        setRefreshing(true);
        if(checkNetState())
        {
            requestNews();
        }else
        {
            if(mNewsListAdapter.getItemSize()==0)
                setEmptyViewText("网络请求失败");
            Log.i(TAG,"Internet is disconnect");
            setRefreshing(false);
        }
    }
    public void requestNews()
    {
        mNewsRequestTask=new NewsRequestTask();
        mNewsRequestTask.setNewsCompleteListener(this);
        mNewsRequestTask.execute(" ");//参数
    }


}
