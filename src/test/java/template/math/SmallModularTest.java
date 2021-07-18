package template.math;

import org.junit.Assert;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.reflect.InvocationTargetException;
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Fork(2)
public class SmallModularTest {
    public static void main(String[] args) throws RunnerException, InvocationTargetException, IllegalAccessException {
        Options options = new OptionsBuilder()
                .include(SmallModularTest.class.getSimpleName())
                .jvmArgsAppend("-XX:TieredStopAtLevel=1")
                .build();
        new Runner(options).run();
    }

    @Test
    public void test() {
        for (int i = 1; (long) i * i <= Integer.MAX_VALUE; i++) {
            System.out.println(i);
            DoubleModular sm = new DoubleModular(i);
            for (int j = 0; j < i; j++) {
                for (int k = 0; k < i; k++) {
                    Assert.assertEquals(j * k % i, sm.mul(j, k));
                }
            }
        }
    }

    @Test
    public void test2() {
        DoubleModular sm = new DoubleModular(49);
        Assert.assertEquals(0, sm.mul(7, 7));
    }

    static DoubleModular mod = new DoubleModular((int)5e4);
    static int round = (int) 1e8;

    @Benchmark
    public int small() {
        int ans = 0;
        for (int i = 0; i < round; i++) {
            ans += mod.mod((long) 1e9 + i);
        }
        return ans;
    }

//    @Benchmark
//    public int simpleModular() {
//        int ans = 0;
//        for (int i = 0; i < round; i++) {
//            ans += ((long) 1e9 + i) % 50000;
//        }
//        return ans;
//    }
}