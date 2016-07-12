package learn.example.pile;





import org.junit.Test;





/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {

        System.out.print(getMyCss());
    }

    public String getMyCss()
    {
        String str="<style type='text/css'>\n" +
                ".headline .img-place-holder {\n" +
                "  height: 200px;\n" +
                "}"+
                "\n</style>";
        return str;
    }

    public String scriptTag(String js)
    {
        return "<script src='"+js+"'>"+"</script>";
    }



}