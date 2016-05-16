package learn.example.joke.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import learn.example.joke.MainActivity;
import learn.example.joke.NewsActivity;
import learn.example.joke.R;
import learn.example.joke.jsonobject.NewsJsonData;
import learn.example.joke.net.NewsRequestTask;

/**
 * Created on 2016/5/7.
 */
public class NewsFragment extends Fragment implements ListView.OnItemClickListener,NewsRequestTask.TaskCompleteListener,PullToRefreshBase.OnRefreshListener2<ListView> {

    private PullToRefreshListView mListView;
    private NewsListAdapter mListAdapter;
    private NewsRequestTask mNewsRequestTask;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_news,container,false);
        mListView= (PullToRefreshListView) view.findViewById(android.R.id.list);
        mListAdapter=new NewsListAdapter();
        mListView.setAdapter(mListAdapter);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.getLoadingLayoutProxy(true,false).setPullLabel("下拉更新数据");
        mListView.getLoadingLayoutProxy(true,false).setRefreshingLabel("更新中...");
        mListView.getLoadingLayoutProxy(true,false).setReleaseLabel("松手更新数据");
        mListView.getLoadingLayoutProxy(false,true).setPullLabel("上拉加载数据");
        mListView.getLoadingLayoutProxy(false,true).setRefreshingLabel("加载中...");
        mListView.getLoadingLayoutProxy(false,true).setReleaseLabel("松手加载数据");
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
        requestNews();
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
        mListView.onRefreshComplete();
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String link= (String) view.getTag(R.id.link);
        Intent intent=new Intent(getActivity(),NewsActivity.class);
        intent.putExtra(NewsActivity.LINK_KEY,link);
        startActivity(intent);
//        Intent intent=new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse(link));
//        startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        if(mListAdapter!=null)
        {
            mListAdapter.clearItem();
            requestNews();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
         requestNews();
    }

    @Override
    public void taskComplete(List<NewsJsonData.resData> data) {
        if(data!=null&&!data.isEmpty())
        {
            mListAdapter.addItem(data);
            mListView.onRefreshComplete();
        }
    }

    @Override
    public void taskFail(String msg) {
        Toast.makeText(getActivity(),"数据请求失败",Toast.LENGTH_SHORT).show();
        mListView.onRefreshComplete();
    }

    public void requestNews()
    {
        if(!((MainActivity)getActivity()).isConnectionNet())
        {
            return;
        }
        mNewsRequestTask=new NewsRequestTask();
        mNewsRequestTask.setNewsCompleteListener(this);
        mNewsRequestTask.execute(" ");//没有参数
    }

    public class NewsListAdapter extends BaseAdapter
    {
        private List<NewsJsonData.resData> mNewsData;

        public NewsListAdapter() {
            this.mNewsData = new ArrayList<>();
        }

        //向尾部添加数据
        public void addItem(List<NewsJsonData.resData> data)
        {
            mNewsData.addAll(data);
            notifyDataSetChanged();
        }
        public void clearItem()
        {
            mNewsData.clear();
        }
        @Override
        public int getCount() {
            return mNewsData.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NewsViewHolder holder;
            if(convertView==null)
            {
                convertView=LayoutInflater.from(getContext()).inflate(R.layout.fragment_news_adpter_view,parent,false);
                holder=new NewsViewHolder();
                holder.title= (TextView) convertView.findViewById(R.id.news_title);
                holder.describe= (TextView) convertView.findViewById(R.id.news_descr);
                holder.img= (ImageView) convertView.findViewById(R.id.news_img);
                convertView.setTag(holder);
            }else
            {
                holder= (NewsViewHolder) convertView.getTag();
            }
            holder.title.setText(mNewsData.get(position).title);
            holder.describe.setText(mNewsData.get(position).newsDes);
            Glide.with(NewsFragment.this).load(mNewsData.get(position).imageUrl).into(holder.img);
            convertView.setTag(R.id.link,mNewsData.get(position).newsUrl);
            return convertView;
        }
    }
    public static class NewsViewHolder{
        public TextView title;
        public TextView describe;
        public ImageView img;
    }
}
