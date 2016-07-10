package learn.example.net;

/**
 * Created on 2016/7/1.
 */
public class Service<T> {

    private ServiceListener<T> mListener;

    public Service(ServiceListener<T> listener) {
        mListener = listener;
    }



    public void setListener(ServiceListener<T> listener) {
        mListener = listener;
    }

    public void removeListener(ServiceListener listener)
    {
        mListener=null;
    }

    public ServiceListener<T> getListener()
    {
        return mListener;
    }
    public void success(T data)
    {
        if (mListener!=null)
        {
            mListener.onSuccess(data);
        }
    };

    public void failure(String msg)
    {
        if (mListener!=null)
        {
            mListener.onFailure(msg);
        }
    }

    public interface ServiceListener<T> {
        void onSuccess(T data);
        void onFailure(String msg);
    }
}
