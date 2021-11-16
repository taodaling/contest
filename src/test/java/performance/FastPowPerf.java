package performance;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import template.math.FastPow2;
import template.math.FastPow4;

import java.lang.reflect.InvocationTargetException;

public class FastPowPerf {
    static int mod = (int) 1e9 + 7;
    static FastPow2 cp = new FastPow2(10, mod);
    static FastPow4 fp = new FastPow4(10, mod);
    static int round = (int) 1e6;

    public static void main(String[] args) throws RunnerException, InvocationTargetException, IllegalAccessException {
        Options options = new OptionsBuilder()
                .include(FastPowPerf.class.getSimpleName())
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