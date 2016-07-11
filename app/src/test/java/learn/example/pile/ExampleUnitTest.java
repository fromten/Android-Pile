package learn.example.pile;





import org.junit.Test;





/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {

        System.out.print(linkTag("www.qq.."));
        System.out.print(scriptTag("www.qq.."));
    }

    public String linkTag(String css)
    {
        return "<link rel='stylesheet' type='text/css' href='"+css+"'>";
    }

    public String scriptTag(String js)
    {
        return "<script src='"+js+"'>"+"</script>";
    }



}