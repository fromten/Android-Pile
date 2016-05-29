package learn.example.joke;



import com.google.gson.Gson;

import org.junit.Test;


import learn.example.pile.MyURI;
import learn.example.pile.jsonobject.JokeJsonData;
import learn.example.pile.net.StringRequest;
import learn.example.pile.util.UrlCheck;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        String res=StringRequest.request(MyURI.IMAGE_JOKE_REQUEST_URL,"");
        System.out.println(res);
    }

    public void getException() throws Exception {
         throw new Exception();
    }


}