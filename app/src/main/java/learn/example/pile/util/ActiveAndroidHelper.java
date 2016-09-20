package learn.example.pile.util;


import android.content.ClipData;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.List;


/**
 * Created on 2016/9/19.
 */
public class ActiveAndroidHelper {

    public  static boolean saveItemsToDatabase(List<? extends Model> items)
    {
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

    public static <T extends Model> List<T> getItemsFromDatabase(Class<T> items,boolean desc,int start, int length)
    {
        From from=new Select().from(items);
        if (desc)
        {
            from.orderBy("id DESC");
        }
        from.where("id between "+start).and(String.valueOf(length));
        return from.execute();
    }

    public static int rowSize(Class<? extends Model> model)
    {
       return new Select().from(model).count();
    }

    public static void deleteAll(Class<? extends Model> model)
    {
        new Delete().from(model).execute();
    }
}
