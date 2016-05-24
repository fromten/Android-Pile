package learn.example.pile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created on 2016/5/15.
 */
public class JokeDataBaseHelper extends SQLiteOpenHelper {


    public static final String JOKE_JSONDATA_COLUMN="jokejson";
    public static final String SPACE=" ";
    public static final String TABLE_NAME="joke";
    public static final String CREATE_SQL="CREATE TABLE"+SPACE+TABLE_NAME+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                          +JOKE_JSONDATA_COLUMN+SPACE+"TEXT)";
    public static final int VERSION=1;
    public static final String DATABASE_NAME="jokedata.db";
    public JokeDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
          db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
