package template.math;

import org.junit.Assert;
import org.junit.Test;

public class DigitUtilsTest {
    @Test
    public void test() {
        long a = (long)4e17;
        long b = (long)4e17 - 1;
        long mod = ((long)1e18);

        LongModular modular = new LongModular(mod);
        Assert.assertEquals(modular.mul(a, b), DigitUtils.mulMod(a, b, mod));
    }
}