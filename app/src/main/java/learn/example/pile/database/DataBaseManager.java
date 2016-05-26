package learn.example.pile.database;

import android.content.Context;

/**
 * Created on 2016/5/15.
 */
public class DataBaseManager {

    public static JokeDataBase openJokeDataBase(Context context)
    {
        return new JokeDataBase(context);
    }

}
