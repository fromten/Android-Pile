package learn.example.pile.adapters.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * Created on 2016/10/4.
 */

public abstract class AbsSaveableRVAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements List<T> {

    private List<T> mList;

    public AbsSaveableRVAdapter() {
        mList = new ArrayList<>();
    }

    public AbsSaveableRVAdapter(List<T> list) {
        mList = list;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public boolean add(T object) {
        if (object == null) return false;
        mList.add(object);
        notifyItemInserted(getItemCount());
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
       return addAll(getItemCount(),collection);
    }

    @Override
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public boolean contains(Object object) {
        return mList.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return mList.contains(collection);
    }

    @Override
    public boolean isEmpty() {
        return mList.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return mList.iterator();
    }

    @Override
    public boolean remove(Object object) {
        return mList.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return mList.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return mList.retainAll(collection);
    }

    @Override
    public int size() {
        return mList.size();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return mList.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(T1[] array) {
        return mList.toArray(array);
    }

    @Override
    public T get(int location) {
        return mList.get(location);
    }

    @Override
    public void add(int location, T object) {
        mList.add(location, object);
    }

    @Override
    public boolean addAll(int location, Collection<? extends T> collection) {
        if (collection == null || collection.isEmpty()) return false;
        boolean isSuccessful=mList.addAll(location,collection);
        notifyItemInserted(getItemCount());
        return isSuccessful;
    }

    @Override
    public int indexOf(Object object) {
        return mList.indexOf(object);
    }

    @Override
    public int lastIndexOf(Object object) {
        return mList.lastIndexOf(object);
    }

    @Override
    public ListIterator<T> listIterator() {
        return mList.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int location) {
        return mList.listIterator(location);
    }

    @Override
    public T remove(int location) {
        return mList.remove(location);
    }

    @Override
    public T set(int location, T object) {
        return mList.set(location, object);
    }

    @NonNull
    @Override
    public List<T> subList(int start, int end) {
        return mList.subList(start, end);
    }

    public List<T> getList(){
        return mList;
    }
}
