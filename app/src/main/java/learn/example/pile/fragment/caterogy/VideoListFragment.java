package learn.example.pile.fragment.caterogy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.R;
import learn.example.pile.adapters.VideoListAdapter;
import learn.example.pile.database.AADatabaseStore;
import learn.example.pile.database.DatabaseStore;
import learn.example.pile.factory.OpenEyeJsonParserFactory;
import learn.example.pile.fragment.base.PersistentFragment;
import learn.example.pile.jsonbean.OpenEyeVideo;
import learn.example.pile.net.IService;
import learn.example.pile.net.OpenEyeService;
import learn.example.pile.provider.OpenEyes;
import learn.example.pile.ui.RecyclerViewImprove;
import learn.example.pile.util.DeviceInfo;
import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/5/25.
 */
public class VideoListFragment extends PersistentFragment implements IService.Callback<OpenEyeVideo> {


    //save state key
    private static final String KEY_NEXT_URL="next_url";
    private static final String KEY_NEXT_PUSH_TIME="next_push_time";


    private VideoListAdapter mAdapter;
    private CategoryViewHolder mCategoryViewHolder;


    private OpenEyeService mService;
    private String nextUrl;
    private long nextPushTime;



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        mAdapter=new VideoListAdapter();
        setAdapter(mAdapter);
        createCategoryHead();

        mService=new OpenEyeService();
        if (savedInstanceState==null)
        {
            setRefreshing(true);
            onRefresh();
        }else {
            nextUrl=savedInstanceState.getString(KEY_NEXT_URL);
            nextPushTime=savedInstanceState.getLong(KEY_NEXT_PUSH_TIME);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_NEXT_URL,nextUrl);
        outState.putLong(KEY_NEXT_PUSH_TIME,nextPushTime);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mService.cancelAll();
        super.onDestroy();
    }


    @Override
    public void onSuccess(OpenEyeVideo data) {

        List<OpenEyes.VideoInfo> mList = null;
        if (data.getIssueList()!=null)
        {
            mList=new OpenEyeJsonParserFactory().parseIssueList(data.getIssueList());
        }else if (data.getItemList()!=null){
            mList=new ArrayList<>();
            new OpenEyeJsonParserFactory().parseItemList(data.getItemList(),mList);
        }

        //再次确认,如果数据没有成功添加,表明请求失败
        if(mList==null||mList.isEmpty())
        {
            notifyError();
            return;
        }

        //在刷新状态下,adapter元素不为空,清空数据
        if (isRefreshing())
        {
            mAdapter.clear();
        }

        nextUrl=data.getNextPageUrl();
        mAdapter.addAll(mList);
        notifySuccess();


        saveDataToDB(mList);
    }

    @Override
    public void onFailure(String message) {
        notifyError();

        if(mAdapter.getItemCount()<=0)
        {
            addDBItemsToAdapter();
        }
    }

    @Override
    public void onRefresh() {
        if (DeviceInfo.checkNetConnected(getContext()))
        {
            long second=nextPushTime-TimeUtil.getCurrentTime();
            //如果到下一次推送时间,或者当前的Adapter 没有元素,需要再次请求
            boolean requireRequest=nextUrl==null
                                    ||nextUrl.isEmpty()
                                    ||mAdapter.isEmpty()
                                    ||second<=0;
            if (requireRequest)
            {
                mService.getHotVideo(this);
                nextPushTime=TimeUtil.getNextDayTime(0,0,0);
            }else {
                setRefreshing(false);
                showTopView("下次更新时间为"+TimeUtil.formatTime(TimeUtil.FORMAT_YMD,nextPushTime/1000));
            }
        }else{
            if (mAdapter.getItemCount()<=0){//没有网络,而且adapter没有任何元素
                addDBItemsToAdapter();
                notifySuccess();
            }else {
                clearRequestState();
            }
        }
    }

    @Override
    public void onLoadMore() {
        if (DeviceInfo.checkNetConnected(getContext()))
        {
            if (nextUrl!=null)
            {
                mService.nextVideoList(nextUrl,this);
            }else {
                notifyError();
            }
        }else {
            addDBItemsToAdapter();
            notifySuccess();
        }
    }

    //创建需要的头部
    private void createCategoryHead()
    {
        mCategoryViewHolder=new CategoryViewHolder();
        addHeadHolder(mCategoryViewHolder);
    }




    private void requestCategoryVideo(@OpenEyes.Category int categoryId)
    {
        setRefreshing(true);
        mService.getCategoryVideoInDateSort(categoryId,VideoListFragment.this);
    }

    @Override
    public DatabaseStore<?,?> onCreateDataStore() {
        return new AADatabaseStore(OpenEyes.VideoInfo.class);
    }

    public class CategoryViewHolder extends RecyclerViewImprove.HeadHolder implements View.OnClickListener{


        CategoryViewHolder()
        {
            this(LayoutInflater.from(getContext()).inflate(R.layout.category,getRecyclerView(),false));
        }

        private CategoryViewHolder(View view) {
            super(view);
            ViewGroup viewGroup= (ViewGroup) view;
            int count=viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                TextView text= (TextView) viewGroup.getChildAt(i);
                text.setOnClickListener(this);
            }
        }

        @Override
        public void viewAppear(RecyclerView.Adapter adapter) {

        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.trip:
                    requestCategoryVideo(OpenEyes.Category.TRIP);
                    break;
                case R.id.ads:
                    requestCategoryVideo(OpenEyes.Category.ADVERTISEMENT);
                    break;
                case R.id.art:
                    requestCategoryVideo(OpenEyes.Category.ART);
                    break;
                case R.id.record:
                    requestCategoryVideo(OpenEyes.Category.RECORD);
                    break;
                case R.id.drama:
                    requestCategoryVideo(OpenEyes.Category.DRAMA);
                    break;
                case R.id.preview:
                    requestCategoryVideo(OpenEyes.Category.PREVIEW);
                    break;
            }

        }
    }
}
