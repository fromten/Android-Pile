package learn.example.pile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.database.JokeDataBase;
import learn.example.pile.database.JokeDataBaseHelper;
import learn.example.pile.jsonobject.BaseJokeData;

/**
 * Created on 2016/5/15.
 */
public class DataBaseManager {

    public static JokeDataBase openJokeDataBase(Context context)
    {
        return new JokeDataBase(context);
    }

}
