package learn.example.pile.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created on 2016/8/16.
 *
 * 与 SaveStateAbleAdapter 不同之处是,
 * 保存数据为Json格式,恢复时利用Gson 反序列保存的数据
 * @see #saveState()
 * @see #restoreSaveState(String)
 * @see #getActualClass()
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
     * @see #restoreSaveState(String)
     * @return 一个序列化后的Json格式数据
     */
    public String saveState()
    {
        return new Gson().toJson(mList,new ListOfSomething<T>(getActualClass()));
    }

    /**
     * 添加元素,可以使用toJson()保存状态后,使用此方法反序列化添加数据,
     * @param json 调用saveState()给予的序列化数据
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



    //由于泛型擦除,需要告诉Gson 真正的参数类型
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
