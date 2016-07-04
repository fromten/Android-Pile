package learn.example.pile.adapters;

import android.content.Intent;
import android.os.Bundle;
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
public class ReadListAdapter extends SaveStateAbleAdapter<ReadListAdapter.ReadViewHolder,GankCommonJson.ResultsBean>{
    private static final String TAG = "ReadListAdapter";

    private View.OnClickListener mViewClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, v.getContext().toString());
            String url= (String) v.getTag();
            Bundle anim=ActivityLauncher.slideAnimation(v.getContext());
            ActivityLauncher.startInternalWebActivity(v.getContext(),url,anim);
        }
    };

    @Override
    public void updaterItemView(ReadViewHolder holder, int position) {
        GankCommonJson.ResultsBean item=getItem(position);
        holder.itemView.setTag(item.getUrl());
        holder.desc.setText(item.getDesc());
    }

    @Override
    public ReadViewHolder getItemViewHolder(ViewGroup parent, int type) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_read_adpter_view,parent,false);
        view.setOnClickListener(mViewClick);
        return new ReadViewHolder(view);
    }



    public static class ReadViewHolder extends RecyclerView.ViewHolder{
        public TextView desc;
        public ReadViewHolder(View itemView) {
            super(itemView);
            this.desc = (TextView) itemView;
        }
    }
}
