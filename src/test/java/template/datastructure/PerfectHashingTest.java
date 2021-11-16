package template.datastructure;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import performance.MapPerf;
import template.rand.RandomWrapper;
import template.rand.UniversalHashFunction;

import java.util.stream.LongStream;

public class PerfectHashingTest {
    @Before
    public void before() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
    }

    @Test
    public void test0() {
        int n = (int) 1e0;
        long[] key = LongStream.range(0, n).toArray();
        Long[] values = LongStream.range(0, n).boxed().toArray(x -> new Long[x]);
        PerfectHashing<Long> ph = new PerfectHashing<>(key, values);
        for (int i = 0; i < n; i++) {
            Assert.assertEquals(values[i], ph.get(i));
        }
        for (int i = n; i < n + n; i++) {
            Assert.assertEquals(null, ph.get(i));
        }
    }

    @Test
    public void test1() {
        int n = (int) 1e1;
        long[] key = LongStream.range(0, n).toArray();
        Long[] values = LongStream.range(0, n).boxed().toArray(x -> new Long[x]);
        PerfectHashing<Long> ph = new PerfectHashing<>(key, values);
        for (int i = 0; i < n; i++) {
            Assert.assertEquals(values[i], ph.get(i));
        }
        for (int i = n; i < n + n; i++) {
            Assert.assertEquals(null, ph.get(i));
        }
    }

    @Test
    public void test2() {
        int n = (int) 1e2;
        long[] key = LongStream.range(0, n).toArray();
        Long[] values = LongStream.range(0, n).boxed().toArray(x -> new Long[x]);
        PerfectHashing<Long> ph = new PerfectHashing<>(key, values);
        for (int i = 0; i < n; i++) {
            Assert.assertEquals(values[i], ph.get(i));
        }
        for (int i = n; i < n + n; i++) {
            Assert.assertEquals(null, ph.get(i));
        }
    }

    @Test
    public void test3() {
        int n = (int) 1e3;
        long[] key = LongStream.range(0, n).toArray();
        Long[] values = LongStream.range(0, n).boxed().toArray(x -> new Long[x]);
        PerfectHashing<Long> ph = new PerfectHashing<>(key, values);
        for (int i = 0; i < n; i++) {
            Assert.assertEquals(values[i], ph.get(i));
        }
        for (int i = n; i < n + n; i++) {
            Assert.assertEquals(null, ph.get(i));
        }
    }

    @Test
    public void test6() {

        PerfectHashing<Long> ph = MapPerf.ph;
        long sum = 0;
        for (long k : MapPerf.keys) {
            sum += ph.getOrDefault(k, 0L);
        }
        System.out.println("numberOfHighest = " + UniversalHashFunction.numberOfInstance);
    }


}