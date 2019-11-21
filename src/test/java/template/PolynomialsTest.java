package template;


import org.junit.Assert;
import org.junit.Test;
import template.datastructure.IntList;
import template.math.Modular;
import template.math.Power;
import template.polynomial.Polynomials;
import template.utils.SequenceUtils;

/**
* Polynomials Tester. 
* 
* @author <Authors name> 
* @since <pre>十一月 9, 2019</pre> 
* @version 1.0 
*/ 
public class PolynomialsTest {
    Modular mod = new Modular(1e9 + 7);
    Power pow = new Power(mod);

    public boolean equal(IntList a, IntList b){
        int ra = Polynomials.rankOf(a);
        int rb = Polynomials.rankOf(b);
        if(ra != rb){
            return false;
        }
        for(int i = ra; i >= 0; i--){
            if(a.get(i) != b.get(i)){
                return false;
            }
        }
        return true;
    }

    @Test
    public void test(){
        IntList p = new IntList();
        p.addAll(SequenceUtils.wrapArray(0, 1));
        IntList remainder = new IntList();
        IntList expect = new IntList();
        expect.addAll(SequenceUtils.wrapArray(0));

        Polynomials.module(1, p, remainder, pow);
        Assert.assertTrue(equal(remainder, expect));
    }

    @Test
    public void test1(){
        IntList p = new IntList();
        p.addAll(SequenceUtils.wrapArray(1, 1));
        IntList remainder = new IntList();
        IntList expect = new IntList();
        expect.addAll(SequenceUtils.wrapArray(mod.valueOf(-1)));

        Polynomials.module(1, p, remainder, pow);
        Assert.assertTrue(equal(remainder, expect));
    }

    @Test
    public void test2(){
        IntList p = new IntList();
        p.addAll(SequenceUtils.wrapArray(1, 1));
        IntList remainder = new IntList();
        IntList expect = new IntList();
        expect.addAll(SequenceUtils.wrapArray(mod.valueOf(1)));

        Polynomials.module(0, p, remainder, pow);
        Assert.assertTrue(equal(remainder, expect));
    }

    @Test
    public void test3(){
        IntList p = new IntList();
        p.addAll(SequenceUtils.wrapArray(1, 1));
        IntList remainder = new IntList();
        IntList expect = new IntList();
        expect.addAll(SequenceUtils.wrapArray(mod.valueOf(1)));

        Polynomials.module(2, p, remainder, pow);
        Assert.assertTrue(equal(remainder, expect));
    }
} 