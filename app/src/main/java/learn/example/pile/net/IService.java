package learn.example.pile.net;

/**
 * Created on 2016/7/11.
 */
public interface IService {


     void cancel(String tag);
     void cancelAll();

     interface Callback<T>{
          void onSuccess(T data);
          void onFailure(String message);
     }
}
