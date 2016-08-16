package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.R;
import learn.example.pile.adapters.VideoListAdapter;
import learn.example.pile.factory.OpenEyeCommentFactory;
import learn.example.pile.factory.OpenEyeVideoFactory;
import learn.example.pile.jsonbean.OpenEyeVideo;
import learn.example.pile.net.IService;
import learn.example.pile.net.OpenEyeService;
import learn.example.pile.object.OpenEyes;
import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/5/25.
 */
public class VideoListFragment extends BaseListFragment implements IService.Callback<OpenEyeVideo> {


    //save state key
    private static final String KEY_NEXT_URL="next_url";
    private static final String KEY_NEXT_PUSH_TIME="next_push_time";


    private VideoListAdapter mAdapter;

    private OpenEyeService mService;
    private String nextUrl;
    private long nextPushTime;

    private CategoryViewHolder mCategoryViewHolder;;

    private boolean isRequireClear=false;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mAdapter=new VideoListAdapter();
        setAdapter(mAdapter);
        createCategoryHead();

        mService=new OpenEyeService();


        if (savedInstanceState==null)
        {
            setRefreshing(true);
            mService.getHotVideo(this);
            nextPushTime=TimeUtil.getNextDayTime(0);
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
            mList=new OpenEyeVideoFactory().parseIssueList(data.getIssueList());
        }else if (data.getItemList()!=null){
            mList=new ArrayList<>();
            new OpenEyeVideoFactory().parseItemList(data.getItemList(),mList);
        }

        nextUrl=data.getNextPageUrl();

        if (isRequireClear)
        {
            mAdapter.clear();
            isRequireClear=false;
        }
        mAdapter.addAll(mList);
        notifySuccess();
    }

    @Override
    public void onFailure(String message) {
        notifyError();
    }

    @Override
    public void onRefresh() {
        long second=nextPushTime-TimeUtil.getTime();

        //如果到下一次推送时间,或者当前的Adapter 没有元素,需要再次请求
        boolean requireRequest=second<0||mAdapter.getItemCount()==0;
        if (requireRequest)
        {
            isRequireClear=true;
            mService.getHotVideo(this);
            nextPushTime=TimeUtil.getNextDayTime(0);
        }else {
            setRefreshing(false);
            showTopView("下次更新时间为"+TimeUtil.formatYMD(nextPushTime/1000));
        }
    }

    @Override
    public void onLoadMore() {
        if (nextUrl!=null)
        {
            mService.nextVideoList(nextUrl,this);
        }else {
            notifyRequestEnd();
        }
    }

    private void createCategoryHead()
    {
        mCategoryViewHolder=new CategoryViewHolder();
        addHeadHolder(mCategoryViewHolder);
    }
    private void requestCategoryVideo(int categoryId)
    {
        setRefreshing(true);
        mService.getCategoryVideoDateSort(categoryId,VideoListFragment.this);
    }

    public class CategoryViewHolder extends HeadHolder implements View.OnClickListener{

        public CategoryViewHolder()
        {
            this(LayoutInflater.from(getContext()).inflate(R.layout.category,getRecyclerView(),false));
        }

        private CategoryViewHolder(View view) {
            super(view);
            ViewGroup viewGroup= (ViewGroup) view;
            int count=viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                viewGroup.getChildAt(i).setOnClickListener(this);
            }
        }

        @Override
        public void onBindHolder(RecyclerView.Adapter adapter) {

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
            isRequireClear=true;
        }
    }
}
