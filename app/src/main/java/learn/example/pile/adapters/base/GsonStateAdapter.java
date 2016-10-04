package learn.example.pile.adapters.base;

import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created on 2016/10/4.
 */

public abstract class GsonStateAdapter<T,VH extends RecyclerView.ViewHolder> extends AbsSaveableRVAdapter<T,VH> {


    /**
     * @see #restoreSaveState(String)
     * @return 一个序列化后的Json格式数据
     */
    public String saveState()
    {
        return new Gson().toJson(this,new GsonStateAdapter.ListOfSomething<T>(getActualClass()));
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
        List<T> list=new Gson().fromJson(json,new GsonStateAdapter.ListOfSomething<T>((getActualClass())));
        addAll(list);
    }

    /**
     * 由于泛型擦除,必须得到实际的T class
     * @return T.class;
     */
    public abstract Class<T> getActualClass();


    //由于泛型擦除,需要告诉Gson 真正的参数类型
    public static class ListOfSomething<X> implements ParameterizedType {

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