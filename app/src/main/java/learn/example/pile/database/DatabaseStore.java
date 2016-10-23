package learn.example.pile.database;

import java.util.List;

/**
 * Created on 2016/10/18.
 */

public interface DatabaseStore<I,O> {
    List<? extends O>  getItems(int start,int length);
    boolean saveItems(List<? extends I> items);
    boolean delete(I item);
    boolean deleteAll();
    boolean update(I item);
    int getRowCount();
}
