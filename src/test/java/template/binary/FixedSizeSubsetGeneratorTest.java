package template.binary;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.reflect.InvocationTargetException;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Fork(2)
public class FixedSizeSubsetGeneratorTest {
    public static void main(String[] args) throws RunnerException, InvocationTargetException, IllegalAccessException {
        Options options = new OptionsBuilder()
                .include(FixedSizeSubsetGeneratorTest.class.getSimpleName())
                .jvmArgsAppend("-XX:TieredStopAtLevel=1")
                .build();
        new Runner(options).run();
    }

    @Benchmark
    public long gospersHach() {
        GospersHack gh = new GospersHack();
        gh.init((1 << 30) - 1, 15);
        long ans = 0;
        while (gh.hasMore()) {
            ans += gh.next();
        }
        return ans;
    }

    @Benchmark
    public long fixedSizeSubsetGenerator() {
        FixedSizeSubsetGenerator gh = new FixedSizeSubsetGenerator();
        gh.init((1 << 30) - 1, 15);
        long ans = 0;
        while (gh.next()) {
            ans += gh.mask();
        }
        return ans;
    }
}