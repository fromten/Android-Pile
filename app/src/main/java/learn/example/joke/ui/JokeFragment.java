package learn.example.joke.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import learn.example.joke.MainActivity;
import learn.example.joke.MyURI;
import learn.example.joke.R;
import learn.example.joke.jsonobject.BaseJokeData;
import learn.example.joke.net.JokeRequestTask;

/**
 * Created on 2016/5/5.
 */
public class JokeFragment extends Fragment implements JokeRequestTask.TaskCompleteListener,
        SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerViewAdapter mRecyclerAdapter;
    private JokeRequestTask mRequestTask;

    public final static String HISTORY_PAGE_KEY="HISTORY_PAGE_KEY";
    private final String STATE_LIST_KEY="STATE_LIST_KEY";
    private String TAG="JokeFragemnt";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_text_joke,container,false);
        mRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerAdapter=new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mRecyclerAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState==RecyclerView.SCROLL_STATE_IDLE)
                {
                    LinearLayoutManager manager= (LinearLayoutManager) recyclerView.getLayoutManager();
                    int last=manager.findLastVisibleItemPosition();
                    if(last>= mRecyclerAdapter.getItemCount()-1)
                    {
                        requestData(readLastHistoryPage()+1);
                    }
                }
            }
        });
        if(savedInstanceState!=null)
        {
            List<BaseJokeData> datas=savedInstanceState.getParcelableArrayList(STATE_LIST_KEY);
            mRecyclerAdapter.addItem(datas);
        }else
        {
            requestData(readLastHistoryPage());
        }
    }
    @Override
    public void onResume() {
        if(mRequestTask!=null)
         mRequestTask.setTaskCompleteListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        if(mRequestTask!=null)
        mRequestTask.setTaskCompleteListener(null);
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STATE_LIST_KEY, (ArrayList<? extends Parcelable>) mRecyclerAdapter.getAll());
        super.onSaveInstanceState(outState);
    }
    @Override
    public void taskComplete(List<BaseJokeData> data) {
        mRecyclerAdapter.addItem(data);
        mRefreshLayout.setRefreshing(false);
        saveLastHistoryPage(data.get(0).getCurrentPage());
    }

    @Override
    public void taskError(String msg) {
         Log.e(TAG,msg);
         mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        if(mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
        super.onDestroy();
    }
    //执行网络数据获取
    public void requestData(int page)
    {
        if(!((MainActivity)getActivity()).isConnectionNet()) return;//没有网络直接返回
        mRequestTask=new JokeRequestTask();
        mRequestTask.setTaskCompleteListener(this);
        mRefreshLayout.setRefreshing(true);
        String texturl=MyURI.TEXT_JOKE_REQUEST_URL+"?page="+page;
        String imgurl=MyURI.IMAGE_JOKE_REQUEST_URL+"?page="+page;
        mRequestTask.execute(texturl,imgurl);
    }
    @Override
    public void onRefresh() {
        mRecyclerAdapter.clearItem();
        requestData(readLastHistoryPage());
    }

    public void saveLastHistoryPage(int page)
    {
        SharedPreferences p=getActivity().getPreferences(Context.MODE_PRIVATE);
        p.edit().putInt(HISTORY_PAGE_KEY,page).apply();
    }
    public int readLastHistoryPage()
    {
        SharedPreferences p=getActivity().getPreferences(Context.MODE_PRIVATE);
        return p.getInt(HISTORY_PAGE_KEY,1);
    }
    public  class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {

        RequestManager  mRequestManager;
        List<BaseJokeData> mItems;
        public RecyclerViewAdapter() {
            mItems=new ArrayList<>();
            mRequestManager=Glide.with(JokeFragment.this);
        }

        public void addItem(List<BaseJokeData> items)
        {
            mItems.addAll(items);
            notifyDataSetChanged();
        }
        public void clearItem()
        {
           mItems.clear();
        }
        public List<BaseJokeData> getAll()
        {
            return mItems;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View  view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_text_joke_adpter,parent,false);
            return new ReViewItemHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                BaseJokeData item=mItems.get(position);
                ReViewItemHolder itemHolder= (ReViewItemHolder) holder;
                if(item.getType()== BaseJokeData.TYPE_TEXT)
                {
                    itemHolder.img.setVisibility(View.GONE);
                    itemHolder.content.setVisibility(View.VISIBLE);
                    itemHolder.content.setText(Html.fromHtml(item.getText()));
                } else
                {   itemHolder.img.setVisibility(View.VISIBLE);
                    itemHolder.content.setVisibility(View.GONE);
                    mRequestManager.load(item.getImgUrl()).into(itemHolder.img);
                }
                itemHolder.title.setText(item.getTitle());

        }
        @Override
        public int getItemCount() {
            return mItems.size();
        }

    }
    public static  class ReViewItemHolder extends RecyclerView.ViewHolder
    {   public TextView title;
        public TextView content;
        public ImageView img;
        ReViewItemHolder(View v)
        {
            super(v);
            title= (TextView) v.findViewById(R.id.joke_title);
            content= (TextView) v.findViewById(R.id.joke_content);
            img= (ImageView) v.findViewById(R.id.joke_img);
        }
    }
}
