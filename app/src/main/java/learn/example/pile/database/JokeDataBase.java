package learn.example.pile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.jsonbean.JokeJsonData;

/**
 * Created on 2016/5/22.
 */
public class JokeDatabase {
    private SQLiteDatabase mDB;

    JokeDatabase(Context context)
    {
        JokeDatabaseHelper mDBHelper = new JokeDatabaseHelper(context, JokeDatabaseHelper.DATABASE_NAME, null, JokeDatabaseHelper.VERSION);
        mDB= mDBHelper.getWritableDatabase();
    }
    public void close(){
        mDB.close();
    }
    public void saveJoke(List<JokeJsonData.JokeResBody.JokeItem> data)
    {
        if(data==null||data.isEmpty())
            return;
        mDB.beginTransaction();
        ContentValues values=new ContentValues();
        for(JokeJsonData.JokeResBody.JokeItem item:data)
        {
            values.clear();
            values.put(JokeDatabaseHelper.JOKE_JSONDATA_COLUMN,item.toString());
            mDB.insert(JokeDatabaseHelper.TABLE_NAME,null,values);
        }
        mDB.setTransactionSuccessful();
        mDB.endTransaction();
    }
    public List<JokeJsonData.JokeResBody.JokeItem> readJoke(int start, int end )
    {
        Gson gson=new Gson();
        List<JokeJsonData.JokeResBody.JokeItem> list=new ArrayList<>();
        String query="SELECT * FROM "+ JokeDatabaseHelper.TABLE_NAME+" limit ?,?";
        Cursor cursor=mDB.rawQuery(query,new String[]{String.valueOf(start), String.valueOf(end)});
        while (cursor.moveToNext())
        {
            String json=cursor.getString(cursor.getColumnIndex(JokeDatabaseHelper.JOKE_JSONDATA_COLUMN));
            if(json!=null)
            {
                JokeJsonData.JokeResBody.JokeItem data=gson.fromJson(json,JokeJsonData.JokeResBody.JokeItem.class);
                list.add(data);
            }
        }
        cursor.close();
        return list;
    }
}
