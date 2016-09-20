package learn.example.pile.activity.normal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import learn.example.pile.R;
import learn.example.pile.activity.base.ToolBarActivity;
import learn.example.pile.adapters.JokeListAdapter;
import learn.example.pile.fragment.comment.JokeCommentFragment;
import learn.example.pile.jsonbean.JokeBean;
import learn.example.pile.ui.RecyclerViewImprove;
import learn.example.pile.util.ActivityLauncher;
import learn.example.pile.util.DeviceInfo;
import learn.example.pile.util.GlideUtil;
import learn.example.pile.video.VideoTextureView;

/**
 * Created on 2016/8/14.
 */
public class DetailJokeActivity extends ToolBarActivity {

    public  static final String KEY_GROUP_JSON="group_json";



    private FrameLayout mRoot;
    private VideoView mVideoView;
    private ExtraHeadCommentFragment mFragment;

    private JokeBean.DataBean.DataListBean.GroupBean group;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_joke);
        mRoot= (FrameLayout) findViewById(R.id.root);

        String groupJson=getIntent().getStringExtra(KEY_GROUP_JSON);
        if (groupJson!=null)
        {
            group=new Gson().fromJson(groupJson, JokeBean.DataBean.DataListBean.GroupBean.class);
            if(savedInstanceState==null)
            {
                showFragment();
            }
        }

    }

    private void  showFragment()
    {
        mFragment=new ExtraHeadCommentFragment();
        Bundle args=new Bundle();
        args.putString(JokeCommentFragment.KEY_GROUP_ID,group.getGroup_id());
        mFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment,mFragment)
                .commit();

    }



    /**
     * 改变视图的布局,进行复用
     */
    public static class JokeAdapterWrap extends JokeListAdapter{
        Context mContext;

        public JokeAdapterWrap(Context context) {
            super(context);
            mContext=context;
        }

        @Override
        public JokeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType==JokeListAdapter.TYPE_SINGLE)
            {
                return new FitSingleJokeViewHolder(parent,getItem(0).is_gif());
            }
            return super.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onClick(View v) {
            JokeBean.DataBean.DataListBean.GroupBean item= (JokeBean.DataBean.DataListBean.GroupBean) v.getTag(R.id.view_tag1);
            if (v.getId()==R.id.cover&&item!=null){
                if (item.is_video())
                {
                    String url=item.getMp4_url();
                    ActivityLauncher.startVideoActivity(mContext,url);
                }
            }
        }

        @Override
        public void onBindViewHolder(JokeViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            View bottomView=holder.itemView.findViewById(R.id.joke_bottom);
            if (bottomView!=null)
            {
                bottomView.setVisibility(View.GONE);
            }
        }
        @Override
        public void bindSingle(final JokeSingleViewHolder holder, JokeBean.DataBean.DataListBean.GroupBean item) {
            //显示和隐藏播放图标;
            int icVisible=item.is_video()?View.VISIBLE:View.INVISIBLE;
            holder.ic_play.setVisibility(icVisible);

            if (item.is_gif())
            {
                String url=item.getGifUrl();
                if (url!=null)
                {
                    ((FitSingleJokeViewHolder)holder).mVideoView.setDataSource(item.getGifUrl());
                }
            }else {
                String url=item.getImages().getFirstUrl();
                Glide.with(mContext)
                        .load(url)
                        .transform(new GlideUtil.MatchWidthTransformation(mContext))
                        .into(holder.cover);
                holder.cover.setTag(R.id.view_tag1,item);
            }


        }
        public static class FitSingleJokeViewHolder extends JokeSingleViewHolder{
            private VideoTextureView mVideoView;
            FitSingleJokeViewHolder(ViewGroup parent,boolean isGif)
            {
                this(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_joke_single, parent, false),isGif);
            }

            private FitSingleJokeViewHolder(View itemView,boolean isGif) {
                super(itemView);

                Context context=itemView.getContext();
                RelativeLayout container= (RelativeLayout) itemView.findViewById(R.id.content);
                RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (isGif)
                {
                    container.removeView(cover);
                    mVideoView=new VideoTextureView(context);

                    int targetHeight= (int) (new DeviceInfo((Activity) context).SCREEN_WIDTH*0.7f);
                    mVideoView.setMinimumWidth(300);
                    mVideoView.setMinimumHeight(targetHeight);
                    mVideoView.setMaxHeight(targetHeight);

                    mVideoView.setLoop(true);
                    params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                    container.addView(mVideoView,params);
                }
            }
        }
    }


    public static class ExtraHeadCommentFragment extends JokeCommentFragment {

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            JokeBean.DataBean.DataListBean.GroupBean group = ((DetailJokeActivity) getActivity()).group;
            //复用
            final JokeAdapterWrap wrap = new JokeAdapterWrap(getActivity());
            wrap.getList().add(group);
            int type = wrap.getItemViewType(0);
            final JokeListAdapter.JokeViewHolder holder = wrap.onCreateViewHolder(getRecyclerView(), type);

                //添加到Adapter头部
                addHeadHolder(new RecyclerViewImprove.HeadHolder(holder.itemView) {
                    @Override
                    public void viewAppear(RecyclerView.Adapter adapter) {
                        wrap.onBindViewHolder(holder, 0);
                    }
                });

        }

    }
}
