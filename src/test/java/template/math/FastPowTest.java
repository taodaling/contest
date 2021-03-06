package template.math;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.reflect.InvocationTargetException;

public class FastPowTest {
    static int mod = (int) 1e9 + 7;
    static CachedPow2 cp = new CachedPow2(10, mod);
    static FastPow fp = new FastPow(10, mod);
    static int round = (int) 1e6;

    public static void main(String[] args) throws RunnerException, InvocationTargetException, IllegalAccessException {
        Options options = new OptionsBuilder()
                .include(FastPowTest.class.getSimpleName())
                .jvmArgsAppend("-XX:TieredStopAtLevel=1")
                .build();
        new Runner(options).run();
    }

    @Benchmark
    public int testCachedPow() {
        int ans = 0;
        for (int i = 0; i < round; i++) {
            ans += cp.pow(i);
        }
        return ans;
    }

    @Benchmark
    public int testFastPow() {
        int ans = 0;
        for (int i = 0; i < round; i++) {
            ans += fp.pow(i);
        }
        return ans;
    }
}