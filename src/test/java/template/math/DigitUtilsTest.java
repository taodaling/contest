package template.math;

import org.junit.Assert;
import org.junit.Test;
import template.rand.RandomWrapper;

public class DigitUtilsTest {
//    @Test
//    public void test() {
//        long a = (long) 4e17;
//        long b = (long) 4e17 - 1;
//        long mod = ((long) 1e18);
//
//        LongModular modular = new LongModular(mod);
//        Assert.assertEquals(modular.mul(a, b), DigitUtils.modmul(a, b, mod));
//    }


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

    @Test
    public void test5() {
        long mod = (long) 1e18;
        ILongModular modular = ILongModular.getInstance(mod);
        RandomWrapper random = new RandomWrapper(1);
        for (int i = 0; i < 1000000; i++) {
            long a = random.nextLong(mod);
            long b = random.nextLong(mod);
            long exp = modular.mul(a, b);
            long act = modular.mul(a, b);
            assert exp == act;
            Assert.assertEquals(exp, act);
        }
    }

    @Test
    public void test6() {
        long mod = 4503599627370517L;
        ILongModular modular = ILongModular.getInstance(mod);
        long a = 2999428912328810L;
        long b = 3451657199285664L;
        long exp = modular.mul(a, b);
        long act = modular.mul(a, b);
        assert exp == act;
        Assert.assertEquals(exp, act);
    }
}