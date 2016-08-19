package learn.example.pile.fragment.base;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.adapters.GsonSaveStateAdapter;
import learn.example.pile.adapters.SaveStateAbleAdapter;

/**
 * Created on 2016/8/19.
 */
public class SaveAdapterStateFragment extends RVListFragment {

    public static final String KEY_SAVE_PARCELABLE = "base_parcelable";
    public static final String KEY_SAVE_JSON = "base_json";

    private RecyclerView.Adapter mStateSaveAdapter;


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState!=null)
        {
            if (mStateSaveAdapter instanceof SaveStateAbleAdapter)
            {  //恢复适配器状态
                List<? extends Parcelable> list=savedInstanceState.getParcelableArrayList(KEY_SAVE_PARCELABLE);
                ((SaveStateAbleAdapter) mStateSaveAdapter).addAll(list);
            }else if (mStateSaveAdapter instanceof GsonSaveStateAdapter)
            {
                String json=savedInstanceState.getString(KEY_SAVE_JSON);
                ((GsonSaveStateAdapter) mStateSaveAdapter).restoreSaveState(json);
            }
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mStateSaveAdapter instanceof SaveStateAbleAdapter)
        {   //保存适配器状态
            outState.putParcelableArrayList(KEY_SAVE_PARCELABLE, (ArrayList<? extends Parcelable>) ((SaveStateAbleAdapter) mStateSaveAdapter).saveState());
        }else if (mStateSaveAdapter instanceof GsonSaveStateAdapter)
        {
            outState.putString(KEY_SAVE_JSON,((GsonSaveStateAdapter) mStateSaveAdapter).saveState());
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
     * @param saveStateAdapter 继承SaveStateAbleAdapter
     * @see  #setAdapter(GsonSaveStateAdapter)
     */
    public void setAdapter(SaveStateAbleAdapter saveStateAdapter) {
        this.setAdapter((RecyclerView.Adapter) saveStateAdapter);
    }


    /**
     * 设置适配器,这能够在Fragment销毁时保存状态,重新创建时回复状态
     * @param gsonSateAdapter 继承GsonSaveStateAdapter
     * @see  #setAdapter(SaveStateAbleAdapter)
     */
    public void setAdapter(GsonSaveStateAdapter gsonSateAdapter) {
        this.setAdapter((RecyclerView.Adapter) gsonSateAdapter);
    }


}
