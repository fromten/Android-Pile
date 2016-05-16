package learn.example.joke.net;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.List;

import learn.example.joke.MyURI;
import learn.example.joke.jsonobject.NewsJsonData;

/**
 * Created on 2016/5/7.
 */
public class NewsRequestTask extends AsyncTask<String,Void,NewsJsonData>{

    private TaskCompleteListener mTaskCompleteListener;
    @Override
    protected NewsJsonData doInBackground(String... params) {
        String strdata=StringRequest.request(MyURI.NEW_DATA_REQUEST_URL,null);
        Gson gson=new Gson();
        NewsJsonData data=null;
        try{
             data=gson.fromJson(strdata,NewsJsonData.class);
        }catch (JsonSyntaxException exception)
        {
            exception.printStackTrace();
        }
        return data;
    }

    public TaskCompleteListener getNewsCompleteListener() {
        return mTaskCompleteListener;
    }

    public void setNewsCompleteListener(TaskCompleteListener mTaskCompleteListener) {
        this.mTaskCompleteListener = mTaskCompleteListener;
    }

    @Override
    protected void onPostExecute(NewsJsonData newsJsonData) {
          if(newsJsonData !=null&& newsJsonData.errNum==0)
          {
              if(mTaskCompleteListener !=null)
                  mTaskCompleteListener.taskComplete(newsJsonData.resData);
          }else if(mTaskCompleteListener !=null)
          {
              mTaskCompleteListener.taskFail(null);
          }
    }

    public interface TaskCompleteListener
    {
        void taskComplete(List<NewsJsonData.resData> data);
        void taskFail(String msg);
    }
}
