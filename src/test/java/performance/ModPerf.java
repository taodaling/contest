package performance;

import org.junit.Assert;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import template.math.*;

import java.lang.reflect.InvocationTargetException;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Fork(1)
public class ModPerf {
    public static void main(String[] args) throws RunnerException, InvocationTargetException, IllegalAccessException {
        Options options = new OptionsBuilder()
                .include(ModPerf.class.getSimpleName())
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

    static DoubleModular mod = new DoubleModular((int) 5e4);
    static int round = (int) 1e6;

    @Benchmark
    public long small() {
        long ans = 0;
        ILongModular mod = new SmallLongModular((int)1e9);
        for (int i = 0; i < round; i++) {
            ans += mod.mul(i, i + 1);
        }
        return ans;
    }

    @Benchmark
    public long dangerMod() {
        long ans = 0;
        ILongModular mod = new LongModularDanger((long) 1e14);
        for (int i = 0; i < round; i++) {
            ans += mod.mul(i, i + 1);
        }
        return ans;
    }

    @Benchmark
    public long bigMod() {
        long ans = 0;
        ILongModular mod = new BigLongModular((long) 1e14);
        for (int i = 0; i < round; i++) {
            ans += mod.mul(i, i + 1);
        }
        return ans;
    }

    @Benchmark
    public long handyMod() {
        long ans = 0;
        ILongModular mod = new HandyLongModular((long) 1e14);
        for (int i = 0; i < round; i++) {
            ans += mod.mul(i, i + 1);
        }
        return ans;
    }

    @Benchmark
    public long longMod() {
        long ans = 0;
        ILongModular mod = new LongModular((long) 1e14);
        for (int i = 0; i < round; i++) {
            ans += mod.mul(i, i + 1);
        }
        return ans;
    }

    @Benchmark
    public long bitMod() {
        long ans = 0;
        ILongModular mod = LongModular2305843009213693951.getInstance();
        for (int i = 0; i < round; i++) {
            ans += mod.mul(i, i + 1);
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