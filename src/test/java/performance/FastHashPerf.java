package performance;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import template.string.FastHash;
import template.string.FastLongHash;

import java.lang.reflect.InvocationTargetException;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Fork(2)
public class FastHashPerf {
    public static void main(String[] args) throws RunnerException, InvocationTargetException, IllegalAccessException {
        Options options = new OptionsBuilder()
                .include(FastHashPerf.class.getSimpleName())
                .jvmArgsAppend("-XX:TieredStopAtLevel=1")
                .build();
        new Runner(options).run();
    }

    static int round = (int) 1e8;

    @Benchmark
    public long intHash() {
        FastHash fh = new FastHash();
        long sum = 0;
        for(int i = 0; i < round; i++){
            sum += fh.hash(i, i + 1, i + 2, i + 3);
        }
        return sum;
    }


    @Benchmark
    public long longHash() {
        FastLongHash fh = new FastLongHash();
        long sum = 0;
        for(int i = 0; i < round; i++){
            sum += fh.hash(i, i + 1, i + 2, i + 3);
        }
        return sum;
    }

}