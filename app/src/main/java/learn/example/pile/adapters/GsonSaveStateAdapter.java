package learn.example.pile.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import learn.example.pile.jsonbean.JokeBean;

/**
 * Created on 2016/8/16.
 */
public abstract class GsonSaveStateAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{
    private List<T> mList;

    public GsonSaveStateAdapter() {
        mList=new ArrayList<>();
    }

    public void addList(List<T> list)
    {
        if (list!=null&&!list.isEmpty())
        {
            mList.addAll(list);
            notifyItemInserted(getItemCount());
        }
    }
    public List<T> getList()
    {
        return mList;
    }

    public void clear()
    {
        if (!mList.isEmpty())
        {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    public T getItem(int position)
    {
        return mList.get(position);
    }


    /**
     * 返后一个序列化后的数据
     * @see #restoreSaveState(String)
     * @return 序列化字符串
     */
    public String saveState()
    {
        return new Gson().toJson(mList,new ListOfSomething<T>(getActualClass()));
    }

    /**
     * 添加元素,可以使用toJson()保存状态后,使用此方法序列化添加数据,
     * @param json 调用saveState()给定的序列化后的数据
     * @see #saveState()
     */
    public void  restoreSaveState(String json)
    {
        if (json==null)
        {
            return;
        }
        List<T> list=new Gson().fromJson(json,new ListOfSomething<T>((getActualClass())));
        addList(list);
    }

    /**
     * 由于泛型擦除,必须得到实际的T class
     * @return T.class;
     */
    public abstract Class<T> getActualClass();

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType) ;

    @Override
    public abstract void onBindViewHolder(VH holder, int position);

    @Override
    public final int getItemCount(){
        return mList.size();
    }

    private static class ListOfSomething<X> implements ParameterizedType {

        private Class<?> wrapped;

        public ListOfSomething(Class<X> wrapped) {
            this.wrapped = wrapped;
        }

        public Type[] getActualTypeArguments() {
            return new Type[] {wrapped};
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }

    }
}
