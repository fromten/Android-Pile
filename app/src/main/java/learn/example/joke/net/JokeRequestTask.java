package learn.example.joke.net;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import learn.example.joke.jsonobject.BaseJokeData;
import learn.example.joke.jsonobject.JokeJsonData;
import learn.example.joke.util.UrlCheck;

/**
 * Created on 2016/5/5.
 */
//Params==完整请求Url地址, Progress==void, Result==BaseJokeData
public class JokeRequestTask extends AsyncTask<String,Void,List<BaseJokeData>>
{

    private TaskCompleteListener mTaskCompleteListener;
    public JokeRequestTask()
    {

    }
    @Override
    protected List<BaseJokeData> doInBackground(String... params) {
        final int lenght=params.length;
        String[] jsonstr=new String[lenght];
        for(int i=0;i<lenght;++i)
        {
            String[] completeurl= UrlCheck.cropUrl(params[i]);
            jsonstr[i]=StringRequest.request(completeurl[0],completeurl[1]);
        }
        Gson gson=new Gson();
        List<BaseJokeData> items=new ArrayList<>();
        for (String res:jsonstr)
        {
            JokeJsonData data;
            try{
                data=gson.fromJson(res,JokeJsonData.class);
            }catch (JsonSyntaxException exception)
            {
                exception.printStackTrace();
                continue;
            }
            if(data!=null&&data.showapi_res_code==0)
            {
                  items.addAll(parseData(data));
            }
        }
        Collections.shuffle(items);//混乱数据
        return items;
    }

    public List<BaseJokeData> parseData(JokeJsonData jsonData)
    {
         int page=jsonData.showapi_res_body.currentPage;
         List<BaseJokeData> list=new ArrayList<>();
         for(JokeJsonData.ShowResBody.ContentList item:jsonData.showapi_res_body.contentlist)
         {
             int type=item.type==1?BaseJokeData.TYPE_TEXT:BaseJokeData.TYPE_PNG;
             BaseJokeData data=new BaseJokeData();
             data.setImgUrl(item.img);
             data.setLastTime(item.ct);
             data.setText(item.text);
             data.setTitle(item.title);
             data.setType(type);
             data.setCurrentPage(page);
             list.add(data);
         }
        return list;
    }
    @Override
    protected void onPostExecute(List<BaseJokeData> result) {
        if(result.isEmpty())
        {
            String msg="empty Data ";
            if(mTaskCompleteListener!=null)
            mTaskCompleteListener.taskFail(msg);
        }else if(mTaskCompleteListener!=null)
        {
            mTaskCompleteListener.taskComplete(result);
        }
    }

    public TaskCompleteListener getTaskCompleteListener() {
        return mTaskCompleteListener;
    }

    public void setTaskCompleteListener(TaskCompleteListener mTaskCompleteListener) {
        this.mTaskCompleteListener = mTaskCompleteListener;
    }

    public interface TaskCompleteListener{
         void taskComplete(List<BaseJokeData> data);
         void taskFail(String msg);
    }
}
