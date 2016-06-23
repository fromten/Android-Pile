package learn.example.pile;



import android.text.format.Formatter;

import com.google.gson.JsonIOException;

import org.junit.Test;


import java.io.ByteArrayInputStream;
import java.io.Serializable;

import learn.example.pile.util.UrlCheck;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {

    }
    public void getException() throws Exception {
         throw new JsonIOException("msg");
    }


}