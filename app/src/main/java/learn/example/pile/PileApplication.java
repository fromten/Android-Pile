package learn.example.pile;

import com.squareup.leakcanary.LeakCanary;

import java.nio.charset.Charset;


/**
 * Created on 2016/6/9.
 */
public class PileApplication extends com.activeandroid.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        LeakCanary.install(this);
       // Stetho.initializeWithDefaults(this);
    }

}
