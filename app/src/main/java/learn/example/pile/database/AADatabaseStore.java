package learn.example.pile.database;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created on 2016/10/18.
 */

public class AADatabaseStore implements DatabaseStore<Model,Model> {

    private Class<? extends Model> mModelClass;

    public AADatabaseStore(Class<? extends Model> modelClass) {
        mModelClass = modelClass;
    }

    @Override
    public List<? extends Model> getItems(int start, int length) {
        From from=new Select().from(mModelClass);
        from.orderBy("id DESC")
                .limit(start+","+length);
        return from.execute();
    }

    @Override
    public boolean saveItems(List<? extends Model> items) {
        if (items==null||items.isEmpty())return false;
        ActiveAndroid.beginTransaction();
        try{
            int count=items.size();
            for (int i = 0; i < count; i++) {
                items.get(i).save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }
        return true;
    }



    @Override
    public boolean delete(Model item) {
        if (item!=null)
        {
            item.delete();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAll() {
        new Delete().from(mModelClass).execute();
        return true;
    }

    @Override
    public boolean update(Model item) {
        if (item!=null)
        {
            item.save();
            return true;
        }
        return false;
    }

    @Override
    public int getRowCount() {
        return new Select().from(mModelClass).count();
    }
}
