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
        Assert.assertEquals(modular.mul(a, b), DigitUtils.mulMod(a, b, mod));
    }

    @Test
    public void test1() {
        for (int i = -1000; i <= 1000; i++) {
            for (int j = -1000; j <= 1000; j++) {
                Assert.assertEquals(DigitUtils.average(i, j),
                        DigitUtils.floorDiv(i + j, 2));
            }
        }
    }

    @Test
    public void test2() {
        Assert.assertEquals(DigitUtils.average(Integer.MAX_VALUE, Integer.MAX_VALUE),
                Integer.MAX_VALUE);
        Assert.assertEquals(DigitUtils.average(Integer.MIN_VALUE, Integer.MIN_VALUE),
                Integer.MIN_VALUE);
        Assert.assertEquals(DigitUtils.average(Integer.MIN_VALUE, Integer.MIN_VALUE + 1),
                Integer.MIN_VALUE);
    }
}