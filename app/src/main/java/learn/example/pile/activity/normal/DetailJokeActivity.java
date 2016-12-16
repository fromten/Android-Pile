package learn.example.pile.activity.normal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import learn.example.pile.R;
import learn.example.pile.activity.base.ToolBarActivity;
import learn.example.pile.adapters.JokeListAdapter;
import learn.example.pile.fragment.comment.JokeCommentFragment;
import learn.example.pile.jsonbean.JokeBean;
import learn.example.pile.pojo.Joke;
import learn.example.pile.ui.RecyclerViewImprove;
import learn.example.pile.util.ActivityLauncher;
import learn.example.pile.util.DeviceInfo;
import learn.example.pile.util.GlideUtil;
import learn.example.pile.video.VideoTextureView;

/**
 * Created on 2016/8/14.
 */
public class DetailJokeActivity extends ToolBarActivity {

    public  static final String EXTRA_GROUP_JSON ="EXTRA_GROUP_JSON";

    private FrameLayout mRoot;
    private ExtraHeadCommentFragment mFragment;

    private Joke.Item group;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_joke);
        mRoot= (FrameLayout) findViewById(R.id.root);

        String groupJson=getIntent().getStringExtra(EXTRA_GROUP_JSON);
        if (groupJson!=null)
        {
            group=new Gson().fromJson(groupJson, Joke.Item.class);
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
        args.putString(JokeCommentFragment.ARGUMENT_GROUP_ID,group.getId_str());
        mFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment,mFragment)
                .commit();

    }





    public static class ExtraHeadCommentFragment extends JokeCommentFragment {

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            Joke.Item group = ((DetailJokeActivity) getActivity()).group;
            //复用
            final JokeAdapterWrap wrapAdapter= new JokeAdapterWrap(getActivity());
            wrapAdapter.getList().add(group);
            int type = wrapAdapter.getItemViewType(0);
            final JokeListAdapter.JokeViewHolder holder = wrapAdapter.onCreateViewHolder(getRecyclerView(), type);

                //添加Adapter头部
            addHeadHolder(new RecyclerViewImprove.HeadHolder(holder.itemView) {
                    @Override
                    public void viewAppear(RecyclerView.Adapter adapter) {
                        wrapAdapter.onBindViewHolder(holder, 0);
                    }
                });

        }

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
            if (viewType==JokeListAdapter.TYPE_SINGLE_VIDEO
                    ||viewType==JokeListAdapter.TYPE_SINGLE_IMAGE)
            {
                return new FitSingleJokeViewHolder(parent,get(0).isGif());
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
            //移除底部的视图
            View bottomView=holder.itemView.findViewById(R.id.joke_bottom);
            if (bottomView!=null)
            {
                bottomView.setVisibility(View.GONE);
            }
        }
        @Override
        public void bindSingle(final JokeSingleViewHolder holder,Joke.Item item) {
            //显示和隐藏播放图标;
            int icVisible=item.isVideo()?View.VISIBLE:View.INVISIBLE;
            holder.ic_play.setVisibility(icVisible);

            if (item.isGif())
            {
                String url=item.getVideo().getUrl();
                if (url!=null)
                {
                    ((FitSingleJokeViewHolder)holder).mVideoView.setDataSource(url);
                }
            }else {
                String url=item.getImage().getUrl();
                Glide.with(mContext)
                        .load(url)
                        .transform(new GlideUtil.MatchTransformation(mContext))
                        .into(holder.cover);
                holder.cover.setTag(R.id.view_tag1,item);
            }
        }
    }
    public static class FitSingleJokeViewHolder extends JokeListAdapter.JokeSingleViewHolder {
        private VideoTextureView mVideoView;
        FitSingleJokeViewHolder(ViewGroup parent,boolean isGif)
        {
            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joke_single, parent, false),isGif);
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

                int targetHeight= (int) (new DeviceInfo( context).SCREEN_WIDTH*0.85f);
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
