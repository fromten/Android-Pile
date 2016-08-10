package learn.example.pile.adapters;

import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.jsonbean.NetEaseNews;

/**
 * Created on 2016/6/29.
 */
public abstract class SaveStateAbleAdapter<VH extends RecyclerView.ViewHolder,D extends Parcelable> extends RecyclerView.Adapter<VH> {
    private List<D> mDataList;

    public SaveStateAbleAdapter() {
        mDataList=new ArrayList<D>();
    }

    public void addItem(D item)
    {
        if (item==null)return;
        mDataList.add(item);
        notifyItemInserted(getItemCount());
    }
    public void addAll(List<D> items)
    {
        if (items==null||items.isEmpty()) return;
        mDataList.addAll(items);
        notifyItemInserted(getItemCount());
    }

    public boolean removeItem(D item)
    {
        if (item==null)
        {
            throw new NullPointerException("item is null no can remove from list ");
        }
        boolean success=mDataList.remove(item);
        notifyDataSetChanged();
        return success;
    }
    public void clear()
    {
        mDataList.clear();
        notifyDataSetChanged();
    }
    public D getItem (int position)
    {
        return mDataList.get(position);
    }

    public List<D> getList()
    {
        return mDataList;
    }

    public List<D> saveState()
    {
        return mDataList;
    }


    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(VH holder, int position);

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
