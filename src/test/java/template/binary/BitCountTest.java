package template.binary;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import template.math.BarrettTest;

import java.lang.reflect.InvocationTargetException;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Fork(2)
public class BitCountTest {
    static final int invokeTime = 100000000;

    public static void main(String[] args) throws RunnerException, InvocationTargetException, IllegalAccessException {
        Options options = new OptionsBuilder()
                .include(BitCountTest.class.getSimpleName())
                .jvmArgsAppend("-XX:TieredStopAtLevel=1")
                .build();
        new Runner(options).run();
    }

    @Benchmark
    public int testStd() {
        int ans = 0;
        for (int i = 0; i < invokeTime; i++) {
            ans += Integer.bitCount(i);
        }
        return ans;
    }

    @Benchmark
    public int testBC() {
        int ans = 0;
        for (int i = 0; i < invokeTime; i++) {
            ans += BitCount.count(i);
        }
        return ans;
    }

}