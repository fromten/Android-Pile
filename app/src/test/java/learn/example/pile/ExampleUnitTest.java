package learn.example.pile;





import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;

import learn.example.pile.object.NetEase;
import learn.example.pile.util.TextUtil;
import learn.example.pile.util.TimeUtil;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        String str= "nihao.zx。asdasdasd。asd";

        System.out.println(Arrays.toString(TextUtil.sentenceSub(str)));
    }


    public String scriptTag(String js)
    {
        return "<script src='"+js+"'>"+"</script>";
    }



}