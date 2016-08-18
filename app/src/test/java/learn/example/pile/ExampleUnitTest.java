package learn.example.pile;


import com.google.gson.JsonArray;

import org.junit.Test;

import java.util.List;

import learn.example.pile.factory.CommentFactory;
import learn.example.pile.factory.OpenEyeCommentFactory;
import learn.example.pile.object.Comment;
import learn.example.pile.object.NetEase;


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