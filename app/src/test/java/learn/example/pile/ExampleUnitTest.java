package learn.example.pile;





import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import learn.example.pile.object.NetEase;
import learn.example.pile.util.HtmlTagBuild;
import learn.example.pile.util.TextUtil;
import learn.example.pile.util.TimeUtil;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        String skipID="00AP0001|2189904";
        int line=skipID.indexOf("|");
        String first=skipID.substring(4,line);
        String second=skipID.substring(line+1,skipID.length());
        System.out.println(first+" "+second);

    }



    public String scriptTag(String js)
    {
        return "<script src='"+js+"'>"+"</script>";
    }



}