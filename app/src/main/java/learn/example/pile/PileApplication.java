package learn.example.pile;

import android.app.Application;

import learn.example.net.OkHttpRequest;


/**
 * Created on 2016/6/9.
 */
public class PileApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //LeakCanary.install(this);
       // Stetho.initializeWithDefaults(this);

    }

}
