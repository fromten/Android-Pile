package learn.example.pile.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import learn.example.joke.R;
import learn.example.pile.WebViewActivity;
import learn.example.pile.jsonobject.GankCommonJson;

/**
 * Created on 2016/6/4.
 */
public class ReadListAdapter extends FooterAdapter<ReadListAdapter.ReadViewHolder> implements View.OnClickListener{

    private List<GankCommonJson.ResultsBean> mItemList;

    public ReadListAdapter() {
        this.mItemList = new ArrayList<>();
    }

    @Override
    public int getSelfItemSize() {
        return mItemList.size();
    }

    @Override
    public ReadViewHolder createSelfViewHolder(ViewGroup parent, int type) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_read_adpter_view,parent,false);
        view.setOnClickListener(this);
        return new ReadViewHolder(view);
    }

    @Override
    public void bindSelfViewHolder(ReadViewHolder holder, int position) {
        GankCommonJson.ResultsBean item=mItemList.get(position);
        holder.itemView.setTag(position);
        holder.desc.setText(item.getDesc());
    }

    public void addAllItem(List<GankCommonJson.ResultsBean> items)
    {
        mItemList.addAll(items);
        notifyItemInserted(getSelfItemSize());
    }

    public void clearItems()
    {
        mItemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int position= (int) v.getTag();
        GankCommonJson.ResultsBean item=mItemList.get(position);
        String url=item.getUrl();
        Intent intent=new Intent(v.getContext(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.KEY_URL,url);
        v.getContext().startActivity(intent);
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
