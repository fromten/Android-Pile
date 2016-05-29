package learn.example.pile.net;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import learn.example.pile.jsonobject.BaseVideoData;
import learn.example.pile.jsonobject.VideoJsonData;
import learn.example.pile.util.UrlCheck;

/**
 * Created on 2016/5/25.
 */
public class VideoRequestTask extends AsyncTask<String,Void,List<BaseVideoData>> {

    private VideoTaskListener mTaskListener;
    @Override
    protected List<BaseVideoData> doInBackground(String... params) {
        List<BaseVideoData> result=null;
        try {
            String resData=StringRequest.request(params[0]);
            VideoJsonData data=new Gson().fromJson(resData,VideoJsonData.class);
            result=parserData(data);
            for(BaseVideoData item:result)
            {
                String[] arr=getVideoWithImgUrl(item.getHtmlUrl());
                if (arr==null) continue;//没有可获得的数据时继续循环
                if(arr[0]!=null) item.setVideoUrl(arr[0]);//设置视频Url
                if(arr[1]!=null) item.setImgUrl(arr[1]);//设置图片Url
            }
        }catch (JsonSyntaxException exception)
        {
            exception.printStackTrace();
        }
        return result;
    }
    //如果成功返回一个数组,str[0] 是视频源地址,str[1] 是图片源url
    private String[] getVideoWithImgUrl(String url)
    {
        if(!url.contains("weibo.com")&&!url.contains("miaopai.com"))
            return null;//遇到不是微博,秒拍视频链接时,return null
        Log.e("task",url);
        String res=StringRequest.request(url);
        Document document=Jsoup.parse(res);
            //如果来源是微博或秒拍,提取视频文件真正地址
            if(url.contains("weibo.com"))
            {String[] result= UrlCheck.parserWeiboHtml(document);
                Log.d("task",result[0]+" img "+result[1]);
                return result;
            }else if(url.contains("miaopai.com"))
            {
                String[] result= UrlCheck.parserMiaopaiHtml(document);
                Log.d("task",result[0]+" img "+result[1]);
                return result;
            }
        return null;
    }
    private List<BaseVideoData> parserData(VideoJsonData data)
    {
        List<BaseVideoData> list=new ArrayList<>();
        if(data!=null&&!data.isError())
        {
            List<VideoJsonData.ResultsBean> result=data.getResults();
            int len=result.size();
            for(int i=0;i<len;++i)
            {
                VideoJsonData.ResultsBean item=result.get(i);
                list.add(new BaseVideoData(item.getUrl(),item.getDesc(),item.getCreatedAt()));
            }
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<BaseVideoData> datas) {
        if(datas==null||datas.isEmpty())
        {
            if(mTaskListener!=null)
                mTaskListener.taskError("data is empty or is null");
        }else
        {
            if(mTaskListener!=null)
                mTaskListener.taskComplete(datas);
        }
    }

    public VideoTaskListener getTaskListener() {
        return mTaskListener;
    }

    public void setTaskListener(VideoTaskListener mTaskListener) {
        this.mTaskListener = mTaskListener;
    }

    public interface VideoTaskListener {
        void taskComplete(List<BaseVideoData> data);
        void taskError(String msg);
    };
}
