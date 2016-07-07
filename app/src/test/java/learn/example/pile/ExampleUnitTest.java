package learn.example.pile;





import org.junit.Test;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

import learn.example.net.OkHttpRequest;
import learn.example.pile.net.StringRequest;
import learn.example.pile.util.UrlCheck;
import learn.example.pile.util.VideoParser;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
            String html= StringRequest.request("http://www.miaopai.com/show/1BOODZOgVQpB9kbTn9XcOw__.htm");
            System.out.print(VideoParser.match(html,"video","src"));

    }



}