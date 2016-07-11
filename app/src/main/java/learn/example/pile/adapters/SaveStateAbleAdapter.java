package learn.example.pile.adapters;

import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import learn.example.joke.R;
import learn.example.uidesign.CommonRecyclerView;

/**
 * Created on 2016/6/29.
 */
public abstract class SaveStateAbleAdapter<VH extends RecyclerView.ViewHolder,D extends Parcelable> extends CommonRecyclerView.FooterViewAdapter<VH> {
    private List<D> mDataList;

    public SaveStateAbleAdapter() {
        mDataList=new ArrayList<D>();
    }

    public void addItem(D item)
    {
        if (item==null)return;
        mDataList.add(item);
        notifyItemInserted(getItemSize());
    }
    public void addAll(List<D> items)
    {
        if (items==null||items.isEmpty())return;
        mDataList.addAll(items);
        notifyItemInserted(getItemSize());
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

    public List<D> getAll()
    {
        return mDataList;
    }

    public List<D> saveState()
    {
        return mDataList;
    }

    @Override
    public void updateFooterView(RecyclerView.ViewHolder holder, int position) {
          if (mDataList.isEmpty())
          {
              holder.itemView.setVisibility(View.INVISIBLE);
          }else {
              View view= holder.itemView.findViewById(R.id.footer_text);
             if (view!=null)
             {
                 TextView textView= (TextView) view;
                 textView.setText(null);
             }
              holder.itemView.setVisibility(View.VISIBLE);
          }
    }

    @Override
    protected final View onCreateFooterView(ViewGroup parent) {
        return super.onCreateFooterView(parent);
    }

    @Override
    public int getItemSize() {
        return mDataList.size();
    }

}
