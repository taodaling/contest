package template;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.junit.Assert;
import org.junit.Test;

public class NumberTheoryTransformTest {

    @Test
    public void test() {
        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        int[] p = new int[]{0, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.module(0, p, remainder, 3);

        Assert.assertArrayEquals(new int[]{1, 0, 0, 0, 0, 0, 0, 0}, remainder);
    }

    @Test
    public void test2() {
        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        int[] p = new int[]{1, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.module(0, p, remainder, 3);

        Assert.assertArrayEquals(new int[]{1, 0, 0, 0, 0, 0, 0, 0}, remainder);
    }

    @Test
    public void test3() {
        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        int[] p = new int[]{0, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.module(1, p, remainder, 3);

        Assert.assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, remainder);
    }

    @Test
    public void test4() {
        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        int[] p = new int[]{1, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.module(1, p, remainder, 3);

        Assert.assertArrayEquals(new int[]{mod.valueOf(-1), 0, 0, 0, 0, 0, 0, 0}, remainder);
    }

    @Test
    public void test5() {
        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        int[] p = new int[]{0, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.module(2, p, remainder, 3);

        Assert.assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, remainder);
    }

    @Test
    public void test6() {
        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        int[] p = new int[]{0, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.module(2, p, remainder, 3);

        Assert.assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, remainder);
    }

    @Test
    public void test7() {
        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        int[] p = new int[]{1, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.module(2, p, remainder, 3);

        Assert.assertArrayEquals(new int[]{1, 0, 0, 0, 0, 0, 0, 0}, remainder);
    }

    @Test
    public void test8(){
        int m = 3;
        int[] p = new int[1 << m];
        p[1] = 1;

        int[] clone = p.clone();
        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        int[] r = new int[1 << m];
        ntt.prepareReverse(r, m);
        ntt.dft(r, p, m);
        ntt.idft(r, p, m);


        Assert.assertArrayEquals(clone, p);
    }
}
