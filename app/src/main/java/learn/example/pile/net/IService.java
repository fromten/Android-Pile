package learn.example.pile.net;

import okhttp3.Request;

/**
 * Created on 2016/7/11.
 */
public interface IService {


     void cancel(String tag);
     void cancelAll();

     public interface Callback<T>{
          void onSuccess(T data);
          void onFailure(String message);
     }
}
