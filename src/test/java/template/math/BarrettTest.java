package template.math;

import org.junit.Assert;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import template.rand.RandomWrapper;

import java.lang.reflect.InvocationTargetException;


@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Fork(2)
public class BarrettTest {
    public static void main(String[] args) throws RunnerException, InvocationTargetException, IllegalAccessException {
        Options options = new OptionsBuilder()
                .include(BarrettTest.class.getSimpleName())
                .jvmArgsAppend("-XX:TieredStopAtLevel=1")
                .build();
        new Runner(options).run();
    }

    final static int mod = (int) 1e9 + 7;
    final static int round = 1000000;

    @Benchmark
    public void barrett() {
        Barrett barrett = new Barrett(mod);
        for (int i = 0; i < round; i++) {
            long x = (long) i * (mod - i);
            long res = barrett.reduce(x);
        }
    }

    @Benchmark
    public void modular() {
        for (int i = 0; i < round; i++) {
            long x = (long) i * (mod - i);
            long res = x % mod;
        }
    }

    @Test
    public void testEqual(){
        Barrett bt = new Barrett(mod);
        for(int i = 0; i < 1000000; i++){
            int a = RandomWrapper.INSTANCE.nextInt(0, mod - 1);
            int b = RandomWrapper.INSTANCE.nextInt(0, mod - 1);
            Assert.assertEquals((long)a * b % mod, bt.reduce((long)a * b));
        }
    }
}