package learn.example.pile.fragment.base;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.adapters.base.AbsSaveableRVAdapter;
import learn.example.pile.adapters.base.GsonStateAdapter;
import learn.example.pile.adapters.base.ParcelableStateAdapter;

/**
 * Created on 2016/8/19.
 */
public class SaveStateAdapterFragment extends RVListFragment {

    public static final String STATE_PARCELABLE = "STATE_PARCELABLE";
    public static final String STATE_JSON = "STATE_JSON";

    private RecyclerView.Adapter mStateSaveAdapter;


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState!=null)
        {
            if (mStateSaveAdapter instanceof ParcelableStateAdapter)
            {  //恢复适配器状态
                List<? extends Parcelable> list=savedInstanceState.getParcelableArrayList(STATE_PARCELABLE);
                ((ParcelableStateAdapter) mStateSaveAdapter).addAll(list);
            }else if (mStateSaveAdapter instanceof GsonStateAdapter)
            {
                String json=savedInstanceState.getString(STATE_JSON);
                ((GsonStateAdapter) mStateSaveAdapter).restoreSaveState(json);
            }
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mStateSaveAdapter instanceof ParcelableStateAdapter)
        {   //保存适配器状态
            outState.putParcelableArrayList(STATE_PARCELABLE,
                    (ArrayList<? extends Parcelable>) ((ParcelableStateAdapter) mStateSaveAdapter).getList());
        }else if (mStateSaveAdapter instanceof GsonStateAdapter)
        {
            outState.putString(STATE_JSON,((GsonStateAdapter) mStateSaveAdapter).saveState());
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        //由于使用WrapAdapter之后, RecyclerView.getAdapter()返回不是真正的adapter
        mStateSaveAdapter=adapter;
    }

    /**
     * 设置适配器,这能够在Fragment销毁时保存状态,重新创建时回复状态
     * @see AbsSaveableRVAdapter;
     * @see GsonStateAdapter
     * @see ParcelableStateAdapter
     */
    public void setAdapter(AbsSaveableRVAdapter saveStateAdapter) {
        this.setAdapter((RecyclerView.Adapter) saveStateAdapter);
    }



}
