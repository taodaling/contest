package template;


import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import template.math.Modular;
import template.math.Power;
import template.polynomial.*;
import template.utils.PrimitiveBuffers;
import template.utils.SequenceUtils;

/**
 * Polynomials Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十一月 9, 2019</pre>
 */
public class PolynomialsTest {

    public Modular getMod() {
        return new Modular(998244353);
    }

    public IntPoly getPoly() {
        return new IntPolyNTT(getMod().getMod());
    }

    public boolean equal(int[] a, int[] b) {
        int ra = Polynomials.rankOf(a);
        int rb = Polynomials.rankOf(b);
        if (ra != rb) {
            return false;
        }
        for (int i = ra; i >= 0; i--) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }


    @After
    public void afterTest() {
        PrimitiveBuffers.check();
    }


    @Test
    public void test() {
        int[] p = PrimitiveBuffers.allocIntPow2(SequenceUtils.wrapArray(0, 1));
        int[] expect = new int[1];

        int[] remainder = getPoly().module(1, p);
        Assert.assertTrue(equal(remainder, expect));

        PrimitiveBuffers.release(remainder, p);
    }

    @Test
    public void test1() {
        int[] p = PrimitiveBuffers.allocIntPow2(SequenceUtils.wrapArray(1, 1));
        int[] expect = new int[]{getMod().valueOf(-1)};

        int[] remainder = getPoly().module(1, p);
        Assert.assertTrue(equal(remainder, expect));

        PrimitiveBuffers.release(remainder, p);
    }

    @Test
    public void test2() {
        int[] p = PrimitiveBuffers.allocIntPow2(SequenceUtils.wrapArray(1, 1));
        int[] expect = new int[]{getMod().valueOf(1)};

        int[] remainder = getPoly().module(0, p);
        Assert.assertTrue(equal(remainder, expect));


        PrimitiveBuffers.release(remainder, p);
    }

    @Test
    public void test3() {
        int[] p = PrimitiveBuffers.allocIntPow2(SequenceUtils.wrapArray(1, 1));
        int[] expect = new int[]{getMod().valueOf(1)};

        int[] remainder = getPoly().module(2, p);
        Assert.assertTrue(equal(remainder, expect));

        PrimitiveBuffers.release(remainder, p);
    }


    @Test
    public void test12() {
        int[] p = new int[]{0, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = getPoly().module(0, p);

        Assert.assertTrue(equal(new int[]{1, 0, 0, 0, 0, 0, 0, 0}, remainder));

        PrimitiveBuffers.release(remainder);
    }

    @Test
    public void test10() {
        int[] p = new int[]{1, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = getPoly().module(0, p);

        Assert.assertTrue(equal(new int[]{1, 0, 0, 0, 0, 0, 0, 0}, remainder));
        PrimitiveBuffers.release(remainder);
    }

    @Test
    public void test11() {
        int[] p = new int[]{0, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = getPoly().module(1, p);

        Assert.assertTrue(equal(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, remainder));
        PrimitiveBuffers.release(remainder);
    }

    @Test
    public void test4() {
        int[] p = new int[]{1, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = getPoly().module(1, p);
        Assert.assertTrue(equal(new int[]{getMod().valueOf(-1), 0, 0, 0, 0, 0, 0, 0}, remainder));
        PrimitiveBuffers.release(remainder);
    }

    @Test
    public void test5() {
        int[] p = new int[]{0, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = getPoly().module(1, p);

        Assert.assertTrue(equal(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, remainder));
        PrimitiveBuffers.release(remainder);
    }

    @Test
    public void test6() {
        int[] p = new int[]{0, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = getPoly().module(1, p);

        Assert.assertTrue(equal(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, remainder));
        PrimitiveBuffers.release(remainder);
    }

    @Test
    public void test7() {
        int[] p = new int[]{1, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = getPoly().module(2, p);

        Assert.assertTrue(equal(new int[]{1, 0, 0, 0, 0, 0, 0, 0}, remainder));
        PrimitiveBuffers.release(remainder);
    }

    @Test
    public void test8() {
        int m = 2;
        int[] p = new int[1 << m];
        p[1] = 1;

        int[] clone = p.clone();
        NumberTheoryTransform.ntt(p, false, getMod().getMod(), 3, new Power(getMod().getMod()));
        NumberTheoryTransform.ntt(p, true, getMod().getMod(), 3, new Power(getMod().getMod()));

        Assert.assertTrue(equal(clone, p));
    }

    @Test
    public void test9() {

        int[][] lists = new int[3][];
        lists[0] = PrimitiveBuffers.allocIntPow2(SequenceUtils.wrapArray(1, 0, 1));
        lists[1] = PrimitiveBuffers.allocIntPow2(SequenceUtils.wrapArray(0, 1, 0));
        lists[2] = PrimitiveBuffers.allocIntPow2(SequenceUtils.wrapArray(2));

        int[] ans = getPoly().dacMul(lists);
        Assert.assertTrue(equal(SequenceUtils.wrapArray(0, 2, 0, 2), ans));
        PrimitiveBuffers.release(ans);
        PrimitiveBuffers.release(lists);
    }

    @Test
    public void test13() {
        int[] p = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] inv = getPoly().inverse(p, 8);
        int[] c = getPoly().modmul(p, inv, 8);
        Assert.assertTrue(equal(SequenceUtils.wrapArray(1), c));
        PrimitiveBuffers.release(inv);
        PrimitiveBuffers.release(c);
    }

    @Test
    public void testInv() {
        int[] p = new int[]{1, 0};
        int[] inv = getPoly().inverse(p, 2);
        int[] c = getPoly().modmul(p, inv, 2);
        Assert.assertTrue(equal(SequenceUtils.wrapArray(1), c));
        PrimitiveBuffers.release(inv);
        PrimitiveBuffers.release(c);
    }

    @Test
    public void test14() {
        int[] p = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] inv = getPoly().inverse(p, 8);
        PrimitiveBuffers.release(inv);
    }

    @Test
    public void convolution() {
        int[] a = new int[]{0, 1};
        int[] b = new int[]{2, 3};
        int[] ans = new int[]{0, 2, 3, 0};

        int[] cv = getPoly().convolution(a, b);
        Assert.assertTrue(equal(ans, cv));

        PrimitiveBuffers.release(cv);
    }

    @Test
    public void multiApply() {
        int[] p = new int[]{0, 1, 2, 0, 0, 0, 0, 1};
        int[] x = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] y = new int[8];

        getPoly().multiApply(p, x, y, 8);
        for (int i = 0; i < 8; i++) {
            Assert.assertEquals(getPoly().apply(p, x[i]), y[i]);
        }
    }

    @Test
    public void pow() {
        int[] p = new int[]{1, 1};
        int[] ans = getPoly().modpow(p, 4, 5);
        int[] ans2 = getPoly().modpowByLnExp(p, 4, 5);
        Assert.assertTrue(equal(ans, ans2));
        PrimitiveBuffers.release(ans, ans2);
    }

    @Test
    public void deltaConvolution() {
        int[] a = new int[]{1, 2, 3, 0};
        int[] b = new int[]{3, 2, 1, 0};
        int[] ans = new int[]{1 * 3 + 2 * 2 + 3 * 1, 2 * 3 + 3 * 2, 3 * 3};

        int[] dc = getPoly().deltaConvolution(a, b);
        Assert.assertTrue(equal(ans, dc));
        PrimitiveBuffers.release(dc);
    }
}
