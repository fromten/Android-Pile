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

        System.out.println(HtmlTagBuild.imageTag(50,50,"www"));
        System.out.println(HtmlTagBuild.headTag("www"));
        System.out.println(HtmlTagBuild.headTag(null));
        System.out.println(HtmlTagBuild.linkTag("www"));
        System.out.println(HtmlTagBuild.linkTag(null));
        System.out.println(HtmlTagBuild.cssTag("www"));
        System.out.println(HtmlTagBuild.cssTag(null));
        System.out.println(HtmlTagBuild.jsTag("wwww"));
        System.out.println(HtmlTagBuild.jsTag(null));

    }



    public String scriptTag(String js)
    {
        return "<script src='"+js+"'>"+"</script>";
    }



}