package learn.example.pile.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import learn.example.joke.R;
import learn.example.pile.WebViewActivity;
import learn.example.pile.jsonobject.GankCommonJson;
import learn.example.pile.util.ActivityLauncher;

/**
 * Created on 2016/6/4.
 */
public class ReadListAdapter extends FooterAdapter<ReadListAdapter.ReadViewHolder>{
    private static final String TAG = "ReadListAdapter";
    private List<GankCommonJson.ResultsBean> mItemList;

    private View.OnClickListener mViewClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, v.getContext().toString());
            String url= (String) v.getTag();
            ActivityLauncher.startInternalWebActivity(v.getContext(),url);
        }
    };

    public ReadListAdapter() {
        this.mItemList = new ArrayList<>();
    }

    @Override
    public int getItemSize() {
        return mItemList.size();
    }

    @Override
    public ReadViewHolder createSelfViewHolder(ViewGroup parent, int type) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_read_adpter_view,parent,false);
        view.setOnClickListener(mViewClick);
        return new ReadViewHolder(view);
    }

    @Override
    public void bindSelfViewHolder(ReadViewHolder holder, int position) {
        GankCommonJson.ResultsBean item=mItemList.get(position);
        holder.itemView.setTag(item.getUrl());
        holder.desc.setText(item.getDesc());
    }

    public void addAllItem(List<GankCommonJson.ResultsBean> list)
    {
        if (list==null||list.isEmpty())
        {
            return;
        }
        mItemList.addAll(list);
        notifyItemInserted(getItemSize());
    }

    public void clearItems()
    {
        mItemList.clear();
        notifyDataSetChanged();
    }

    public List<GankCommonJson.ResultsBean> getList()
    {
        return mItemList;
    }

    public static class ReadViewHolder extends RecyclerView.ViewHolder{
        public TextView desc;
        public ReadViewHolder(View itemView) {
            super(itemView);
            this.desc = (TextView) itemView;
        }
    }
}
