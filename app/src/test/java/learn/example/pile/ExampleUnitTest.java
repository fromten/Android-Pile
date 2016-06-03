package learn.example.joke;



import android.text.format.Formatter;

import com.google.gson.JsonIOException;

import org.junit.Test;


import learn.example.pile.util.UrlCheck;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        String url="http://wwevd.com/.gifsdsd.giff";
        System.out.println(url.endsWith("gif"));
    }

    public void getException() throws Exception {
         throw new JsonIOException("msg");
    }


}