package learn.example.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created on 2016/7/3.
 */
public class PhotoProgressTask extends AsyncTask<Request,Integer,byte[]> {
    private ProgressListener mListener;
    private int contentLength;
    public PhotoProgressTask(ProgressListener listener) {
        mListener = listener;
    }

    public ProgressListener getListener() {
        return mListener;
    }

    public void setListener(ProgressListener listener) {
        mListener = listener;
    }

    public void removeListener()
    {
        mListener=null;
    }

    @Override
    protected byte[] doInBackground(Request... params) {

        InputStream in=null;
        ByteArrayOutputStream out=null;
        try {
            Response response= OkHttpRequest.getInstance().syncRequest(params[0]);
            contentLength= (int) response.body().contentLength();
            Log.i("TAG", String.valueOf(contentLength));
             in=response.body().byteStream();
             out=new ByteArrayOutputStream();
            byte[] b=new byte[1024];
            int len=-1;
            int count=0;
            int tenPercent=contentLength/10;
            while ((len=in.read(b))>-1)
            {
                out.write(b,0,len);
                count+=len;
                if (count>=tenPercent)
                {
                  publishProgress(10);
                    count=0;
                }
            }
            byte[] all=out.toByteArray();
            return all;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in!=null)
            {
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
          if (mListener!=null&&bytes!=null)
          {
              mListener.onComplete(bytes);
          }
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        if (mListener!=null)
        {
            mListener.onProgressChanged(values[0]);
        }
    }

    public interface ProgressListener{
        void onProgressChanged(int value);
        void onComplete(byte[] bytes);
    }
}
