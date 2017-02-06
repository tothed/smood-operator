import org.junit.Assert;
import wasdev.sample.servlet.Hello;

public class HelloTest {
    @org.junit.Test
    public void doIt() throws Exception {
        Assert.assertTrue(Hello.doIt(4) > 4);
        Assert.assertTrue(Hello.doIt(4) != 0);
    }
}