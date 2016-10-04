package learn.example.pile.database;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.List;


/**
 * Created on 2016/10/4.
 */

public class AADatabaseStore{
    private Class<? extends Model> mModel;

    public AADatabaseStore(Class<? extends Model> model) {
        mModel = model;
    }

    public Class<? extends Model> getModel() {
        return mModel;
    }

    public void setModel(Class<? extends Model> model) {
        mModel = model;
    }

    /**
     * 保存数据到数据库
     * @param items {@link com.activeandroid.Model}
     * @return true 保存成功,else wise
     */
    public <T extends Model> boolean save(List<T> items) {
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

    /**
     * @param start 开始行位置
     * @param length 相对起始位置的长度
     * @return list
     */
    public <T extends Model> List<T> getItems(int start, int length) {
        From from=new Select().from(mModel);
        from.orderBy("id DESC")
                .limit(start+","+length);
        return from.execute();
    }

    public void deleteAll() {
         new Delete().from(mModel).execute();
    }

    public int rowCount() {
       return new Select().from(mModel).count();
    }
}
