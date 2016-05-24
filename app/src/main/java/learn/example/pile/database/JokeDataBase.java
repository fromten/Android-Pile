package learn.example.pile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.jsonobject.BaseJokeData;

/**
 * Created on 2016/5/22.
 */
public class JokeDataBase {
    private SQLiteDatabase mDB;
    JokeDataBase(Context context)
    {
        JokeDataBaseHelper mDBHelper = new JokeDataBaseHelper(context, JokeDataBaseHelper.DATABASE_NAME, null, JokeDataBaseHelper.VERSION);
        mDB= mDBHelper.getWritableDatabase();
    }
    public void close(){
        mDB.close();
    }
    public void saveJoke(List<BaseJokeData> data)
    {
        mDB.beginTransaction();
        ContentValues values=new ContentValues();
        for(BaseJokeData item:data)
        {
            values.clear();
            values.put(JokeDataBaseHelper.JOKE_JSONDATA_COLUMN,item.toString());
            mDB.insert(JokeDataBaseHelper.TABLE_NAME,null,values);
        }
        mDB.setTransactionSuccessful();
        mDB.endTransaction();
    }
    public List<BaseJokeData> readJoke(int start,int end )
    {
        Gson gson=new Gson();
        List<BaseJokeData> list=new ArrayList<>();
        String query="SELECT * FROM "+ JokeDataBaseHelper.TABLE_NAME+" limit ?,?";
        Cursor cursor=mDB.rawQuery(query,new String[]{String.valueOf(start), String.valueOf(end)});
        while (cursor.moveToNext())
        {
            String json=cursor.getString(cursor.getColumnIndex(JokeDataBaseHelper.JOKE_JSONDATA_COLUMN));
            if(json!=null)
            {
                BaseJokeData data=gson.fromJson(json,BaseJokeData.class);
                list.add(data);
            }
        }
        cursor.close();
        return list;
    }
}