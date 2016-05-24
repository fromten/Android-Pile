package learn.example.pile.net;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.MyURI;
import learn.example.pile.jsonobject.BaseNewsData;
import learn.example.pile.jsonobject.NewsJsonData;

/**
 * Created on 2016/5/7.
 */
public class NewsRequestTask extends AsyncTask<String,Void,List<BaseNewsData>>{

    private TaskCompleteListener mTaskCompleteListener;
    @Override
    protected List<BaseNewsData> doInBackground(String... params) {
        String resdata=StringRequest.request(MyURI.NEW_DATA_REQUEST_URL,null);
        Gson gson=new Gson();
        NewsJsonData data=null;
        try{
             data=gson.fromJson(resdata,NewsJsonData.class);
             return paresData(data);
        }catch (JsonSyntaxException exception)
        {
            exception.printStackTrace();
        }
        return null;
    }
    public List<BaseNewsData> paresData(NewsJsonData newsJsonData)
    {
        if(newsJsonData==null)
            return null;
        int len=newsJsonData.resData.size();
        List<NewsJsonData.resData> resData=newsJsonData.resData;
        List<BaseNewsData> list=new ArrayList<>();
        for(int i=0;i<len;++i )
        {
            BaseNewsData data=new BaseNewsData();
            data.setNewsDesc(resData.get(i).newsDes);
            data.setNewsUrl(resData.get(i).newsUrl);
            data.setNewsIMgUrl(resData.get(i).imageUrl);
            data.setNewsTitle(resData.get(i).title);
            list.add(data);
        }
        return list;
    }
    public TaskCompleteListener getNewsCompleteListener() {
        return mTaskCompleteListener;
    }

    public void setNewsCompleteListener(TaskCompleteListener mTaskCompleteListener) {
        this.mTaskCompleteListener = mTaskCompleteListener;
    }

    @Override
    protected void onPostExecute(List<BaseNewsData> data) {
          if(data !=null&&!data.isEmpty())
          {
              if(mTaskCompleteListener !=null)
                  mTaskCompleteListener.taskComplete(data);
          }else if(mTaskCompleteListener !=null)
          {
              String msg="data request faller";
              mTaskCompleteListener.taskFail(msg);
          }
    }

    public interface TaskCompleteListener
    {
        void taskComplete(List<BaseNewsData> data);
        void taskFail(String msg);
    }
}
