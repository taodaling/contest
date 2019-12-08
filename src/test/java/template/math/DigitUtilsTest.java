package template.math;

import org.junit.Assert;
import org.junit.Test;

public class DigitUtilsTest {
    @Test
    public void test() {
        long a = Long.MAX_VALUE / 10;
        long b = Long.MAX_VALUE / 10 - 1;
        long mod =  ((long)1e18 + 7);

        LongModular modular = new LongModular(mod);
        Assert.assertEquals(modular.mul(a, b), DigitUtils.mulMod(a, b, mod));
    }
}