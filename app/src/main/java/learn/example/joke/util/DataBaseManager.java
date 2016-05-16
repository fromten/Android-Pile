package learn.example.joke.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import learn.example.joke.database.JokeDataBaseHelper;
import learn.example.joke.jsonobject.BaseJokeData;

/**
 * Created on 2016/5/15.
 */
public class DataBaseManager {
    private JokeDataBaseHelper mJokeDataBaseHelper;
    private SQLiteDatabase mDataBase;

    public DataBaseManager(Context context)
    {
        mJokeDataBaseHelper=new JokeDataBaseHelper(context,JokeDataBaseHelper.DATABASE_NAME,null,JokeDataBaseHelper.VERSION);
        mDataBase=mJokeDataBaseHelper.getWritableDatabase();
    }
    public void closeDB()
    {
        mDataBase.close();
    }
    public void saveJokeToDataBase(List<BaseJokeData> data)
    {
        mDataBase.beginTransaction();
        ContentValues values=new ContentValues();
        for(BaseJokeData item:data)
        {
            values.clear();
            values.put(JokeDataBaseHelper.JOKE_JSONDATA_COLUMN,item.toString());
            mDataBase.insert(JokeDataBaseHelper.TABLE_NAME,null,values);
        }
        mDataBase.setTransactionSuccessful();
        mDataBase.endTransaction();
    }
    public List<BaseJokeData> readJokeFromDataBase(int start,int end )
    {
        Gson gson=new Gson();
        List<BaseJokeData> list=new ArrayList<>();
        String query="SELECT * FROM "+ JokeDataBaseHelper.TABLE_NAME+" limit ?,?";
        Cursor cursor=mDataBase.rawQuery(query,new String[]{String.valueOf(start), String.valueOf(end)});
//        if(start>cursor.getCount())
//        {
//            cursor.close();
//            return null;
//        }
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
    public int getDBRowCount()
    {
        String sql="select count(*) from "+JokeDataBaseHelper.TABLE_NAME;
        Cursor cursor= mDataBase.rawQuery(sql,null);
        int count=cursor.getCount();
        cursor.close();
        return count;
    }
}
