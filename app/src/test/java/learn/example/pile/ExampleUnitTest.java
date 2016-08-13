package learn.example.pile;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.TypeAdapter;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import learn.example.net.OkHttpRequest;
import learn.example.pile.jsonbean.JokeBean;
import learn.example.pile.net.GsonService;
import learn.example.pile.net.IService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    public static final String json="{ 'urls':[{\"url\": " +
            "\"http://p3.pstatp.com/w356/78f002033060a15d691\"}, " +
            "{\"url\": \"http://pb2.pstatp.com/w356/78f002033060a15d691\"}, " +
            "{\"url\": \"http://pb3.pstatp.com/w356/78f002033060a15d691\"}]}";
    private JsonArray urls;

    @Test
    public void addition_isCorrect() {


    }




    public String scriptTag(String js)
    {
        return "<script src='"+js+"'>"+"</script>";
    }



    public static class UrlBean {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}