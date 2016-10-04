package learn.example.pile.util;


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


    public static int rowSize(Class<? extends Model> model)
    {
       return new Select().from(model).count();
    }


}
