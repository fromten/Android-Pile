package learn.example.pile.database;

import android.content.Context;

/**
 * Created on 2016/5/15.
 */
public class DatabaseManager {

    public static JokeDatabase openJokeDatabase(Context context)
    {
        return new JokeDatabase(context);
    }

}
