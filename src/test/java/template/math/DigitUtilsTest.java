package template.math;

import org.junit.Assert;
import org.junit.Test;

public class DigitUtilsTest {
    @Test
    public void test() {
        long a = (long) 4e17;
        long b = (long) 4e17 - 1;
        long mod = ((long) 1e18);

        LongModular modular = new LongModular(mod);
        Assert.assertEquals(modular.mul(a, b), DigitUtils.modmul(a, b, mod));
    }


    @Test
    public void test3() {
        Assert.assertEquals(DigitUtils.floorAverage(Integer.MAX_VALUE, Integer.MAX_VALUE),
                Integer.MAX_VALUE);
        Assert.assertEquals(DigitUtils.floorAverage(Integer.MIN_VALUE, Integer.MIN_VALUE),
                Integer.MIN_VALUE);
        Assert.assertEquals(DigitUtils.floorAverage(Integer.MIN_VALUE, Integer.MIN_VALUE + 1),
                Integer.MIN_VALUE);
    }

    @Test
    public void test4() {
        Assert.assertEquals(DigitUtils.ceilAverage(Integer.MAX_VALUE, Integer.MAX_VALUE),
                Integer.MAX_VALUE);
        Assert.assertEquals(DigitUtils.ceilAverage(Integer.MIN_VALUE, Integer.MIN_VALUE),
                Integer.MIN_VALUE);
        Assert.assertEquals(DigitUtils.ceilAverage(Integer.MIN_VALUE, Integer.MIN_VALUE + 1),
                Integer.MIN_VALUE + 1);
    }
}