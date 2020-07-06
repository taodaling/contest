package template.math;

import org.junit.Test;
import template.rand.RandomWrapper;

import java.util.Random;

public class LongModular2305843009213693951Test {
    RandomWrapper rw = new RandomWrapper(new Random());

    public long next() {
        return rw.nextLong(0, 2305843009213693951L - 1);
    }

    @Test
    public void test1() {
        ILongModular mod = LongModular2305843009213693951.getInstance();
        BigLongModular other = new BigLongModular(2305843009213693951L);
        for (int i = 0; i < (int) 1e7; i++) {
            long a = next();
            long b = next();
            if(other.mul(a, b) != mod.mul(a, b)){
                throw new RuntimeException();
            }
        }
    }

    @Test
    public void test() {

    }
}