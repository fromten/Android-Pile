package learn.example.pile.activity.normal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.gson.Gson;

import learn.example.pile.R;
import learn.example.pile.activity.base.FullScreenActivity;
import learn.example.pile.activity.base.ToolBarActivity;
import learn.example.pile.adapters.JokeListAdapter2;
import learn.example.pile.fragment.JokeCommentFragment;
import learn.example.pile.jsonbean.JokeBean;

/**
 * Created on 2016/8/14.
 */
public class DetailJokeActivity extends ToolBarActivity {

    public  static final String KEY_GROUP_JSON="group_json";



    private FrameLayout mCommentRoot;
    private ExtraHeadCommentFragment mFragment;

    private JokeBean.DataBean.DataListBean.GroupBean group;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_joke);
        mCommentRoot= (FrameLayout) findViewById(R.id.joke_comment);

        String groupJson=getIntent().getStringExtra(KEY_GROUP_JSON);
        if (groupJson!=null)
        {
            group=new Gson().fromJson(groupJson, JokeBean.DataBean.DataListBean.GroupBean.class);
            if (savedInstanceState==null)
            showCompleteFragment();
        }

    }

    private void showCompleteFragment()
    {

        mFragment=new ExtraHeadCommentFragment();
        Bundle args=new Bundle();
        args.putLong(JokeCommentFragment.KEY_GROUP_ID,group.getGroup_id());
        mFragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.joke_comment, mFragment).commit();

    }

    public static class ExtraHeadCommentFragment extends JokeCommentFragment{

        public final static String KEY_SAVE_SCROLL_POSITION="scrollposition";

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            JokeBean.DataBean.DataListBean.GroupBean group=((DetailJokeActivity)getActivity()).group;
            //复用
            JokeListAdapter2 adapter2=new JokeListAdapter2();
            adapter2.getList().add(group);
            int type=adapter2.getItemViewType(0);
            final JokeListAdapter2.JokeViewHolder holder=adapter2.onCreateViewHolder(getRecyclerView(),type);
            View bottomView=holder.itemView.findViewById(R.id.joke_bottom);
            adapter2.onBindViewHolder(holder,0);
            if (bottomView!=null)
            {    //移除底部
                ((ViewGroup)holder.itemView).removeView(bottomView);
            }
            //添加Adapter头部
            addHeadHolder(new HeadHolder(holder.itemView) {
                @Override
                public void onBindHolder(RecyclerView.Adapter adapter) {

                }
            });
        }
    }

}
