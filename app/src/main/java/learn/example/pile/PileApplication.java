package learn.example.pile;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

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
