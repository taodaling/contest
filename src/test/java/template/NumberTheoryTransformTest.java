package template;

import org.junit.Assert;
import org.junit.Test;
import template.math.Modular;
import template.polynomial.NumberTheoryTransform;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SequenceUtils;

public class NumberTheoryTransformTest {

    @Test
    public void test() {
        Modular mod = new Modular(998244353);
        int[] p = new int[]{0, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.module(0, p, remainder, 3);

        Assert.assertArrayEquals(new int[]{1, 0, 0, 0, 0, 0, 0, 0}, remainder);
    }

    @Test
    public void test2() {
        Modular mod = new Modular(998244353);
        int[] p = new int[]{1, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.module(0, p, remainder, 3);

        Assert.assertArrayEquals(new int[]{1, 0, 0, 0, 0, 0, 0, 0}, remainder);
    }

    @Test
    public void test3() {
        Modular mod = new Modular(998244353);
        int[] p = new int[]{0, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.module(1, p, remainder, 3);

        Assert.assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, remainder);
    }

    @Test
    public void test4() {
        Modular mod = new Modular(998244353);
        int[] p = new int[]{1, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.module(1, p, remainder, 3);

        Assert.assertArrayEquals(new int[]{mod.valueOf(-1), 0, 0, 0, 0, 0, 0, 0}, remainder);
    }

    @Test
    public void test5() {
        Modular mod = new Modular(998244353);
        int[] p = new int[]{0, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.module(2, p, remainder, 3);

        Assert.assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, remainder);
    }

    @Test
    public void test6() {
        Modular mod = new Modular(998244353);
        int[] p = new int[]{0, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.module(2, p, remainder, 3);

        Assert.assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, remainder);
    }

    @Test
    public void test7() {
        Modular mod = new Modular(998244353);
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
        Modular mod = new Modular(998244353);
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.dft(p, m);
        ntt.idft(p, m);


        Assert.assertArrayEquals(clone, p);
    }

    @Test
    public void test9(){
        Modular mod = new Modular(998244353);
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);

        IntegerList[] lists = new IntegerList[3];
        lists[0] = new IntegerList();
        lists[0].addAll(SequenceUtils.wrapArray(1, 0, 1));
        lists[1] = new IntegerList();
        lists[1].addAll(SequenceUtils.wrapArray(0, 1, 0));
        lists[2] = new IntegerList();
        lists[2].addAll(SequenceUtils.wrapArray(2));


        IntegerList ans = new IntegerList();
        IntegerList exp = new IntegerList();
        exp.addAll(SequenceUtils.wrapArray(0, 2, 0, 2));
        ntt.dacMul(lists, ans);
        Assert.assertEquals(exp, ans);
    }
}
