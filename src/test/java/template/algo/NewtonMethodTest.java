package template.algo;

import org.junit.Assert;
import org.junit.Test;

public class NewtonMethodTest {
    @Test
    //x^2 - y = 0
    public void testSqrt() {
        double y = 10;
        NewtonMethod nm = new NewtonMethod(1e-12);
        double ans = nm.search(x -> x * x - y, x -> 2 * x, y);
        ans = Math.abs(ans);

        Assert.assertEquals(Math.sqrt(y), ans, 1e-10);
    }

    //(x-1)^2=0
    @Test
    public void testTop() {
        //2(x-1)=0
        NewtonMethod nm = new NewtonMethod(1e-12);
        double ans = nm.search(x -> 2 * (x - 1), x -> 2, 0);
        Assert.assertEquals(ans, 1, 1e-10);
    }
}