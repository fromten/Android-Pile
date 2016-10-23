package learn.example.pile.fragment.base;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;


import java.util.List;

import learn.example.pile.adapters.base.AbsSaveableRVAdapter;
import learn.example.pile.database.DatabaseStore;

/**
 * Created on 2016/10/4.
 */

public abstract class PersistentFragment extends BaseListFragment {

    public static final String KEY_DB_INDEX="db_index";
    private RecyclerView.Adapter mAdapter;
    private DatabaseStore mDatabaseStore;
    private int mDatabaseIndex;
    public static final int MAX_READ_NUMBER=10;


    /**
     * 子类创建正确的AADatabaseStore
     * @return AADatabaseStore
     */
    public abstract DatabaseStore onCreateDataStore();

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState!=null)
        {
            mDatabaseIndex=savedInstanceState.getInt(KEY_DB_INDEX,0);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_DB_INDEX,mDatabaseIndex);
        super.onSaveInstanceState(outState);
    }


    public void saveDataToDB(List<?> list)
    {
        check();
        mDatabaseStore.saveItems(list);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        super.setAdapter(adapter);
    }

    public void addDBItemsToAdapter()
    {
        addDBItemsToAdapter(mDatabaseIndex,MAX_READ_NUMBER);
    }

    public void addDBItemsToAdapter(int start,int length)
    {
        check();
        List<?> list=mDatabaseStore.getItems(start,length);
        if (list!=null&&list.size()>0)
        {
            if (mAdapter instanceof AbsSaveableRVAdapter)
            {
               ((AbsSaveableRVAdapter) mAdapter).addAll(list);
               mDatabaseIndex+=list.size();
            }
        }
    }

    public void check()
    {
        if (mDatabaseStore==null)
        {
            mDatabaseStore= onCreateDataStore();
        }
        int currentCount=mDatabaseStore.getRowCount();
        int max=getMaxColumnCount();
        if (max<currentCount)
        {
            mDatabaseStore.deleteAll();
        }
    }

    public int getMaxColumnCount()
    {
        return 2000;
    }
}
