package performance;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import template.datastructure.PerfectHashing;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.RandomWrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.stream.LongStream;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 1)
@Fork(1)
public class MapPerf {
    public static void main(String[] args) throws RunnerException, InvocationTargetException, IllegalAccessException {
        Options options = new OptionsBuilder()
                .include(MapPerf.class.getSimpleName())
                .jvmArgsAppend("-XX:TieredStopAtLevel=1")
                .build();
        new Runner(options).run();
    }

    static final int n = (int) 1e6;
    static final int T = 10;
    public static long[] keys = new long[n];

    static {
        LongHashMap map = new LongHashMap(n, false);
        for (int i = 0; i < n; i++) {
//            while (true) {
//                keys[i] = RandomWrapper.INSTANCE.nextLong(0, (long) 1e18);
//                if (map.containKey(keys[i])) {
//                    continue;
//                }
//                map.put(keys[i], 0);
//                break;
//            }
            keys[i] = i;
        }
    }

    public static LongHashMap lhm = new MapPerf().writeLongHashMap();
    public static HashMap hm = new MapPerf().writeHashMap();
    public static PerfectHashing ph = new MapPerf().writePerfectHash();

    @Benchmark
    public LongHashMap writeLongHashMap() {
        LongHashMap map = new LongHashMap(n, false);
        for (int i = 0; i < n; i++) {
            map.put(keys[i], i);
        }
        return map;
    }

    @Benchmark
    public HashMap writeHashMap() {
        HashMap map = new HashMap(n);
        for (int i = 0; i < n; i++) {
            map.put(keys[i], i);
        }
        return map;
    }


    @Benchmark
    public PerfectHashing writePerfectHash() {
        long[] key = keys.clone();
        Long[] values = LongStream.range(0, n).boxed().toArray(x -> new Long[x]);
        PerfectHashing map = new PerfectHashing(key, values);
        return map;
    }

    @Benchmark
    public void readLongHashMapHit() {
        LongHashMap map = lhm;
        for (int t = 0; t < T; t++) {
            for (int i = 0; i < n; i++) {
                map.get(keys[i]);
            }
        }
    }

    @Benchmark
    public void readHashMapHit() {
        HashMap map = hm;

        for (int t = 0; t < T; t++) {
            for (int i = 0; i < n; i++) {
                map.get(keys[i]);
            }
        }
    }

    @Benchmark
    public void readArray() {
        long ans = 0;
        for (int t = 0; t < T; t++) {
            for (int i = 0; i < n; i++) {
                ans += keys[i];
            }
        }
    }

    @Benchmark
    public void readPerfectHashHit() {
        PerfectHashing map = ph;

        for (int t = 0; t < T; t++) {
            for (int i = 0; i < n; i++) {
                map.get(keys[i]);
            }
        }
    }

    @Benchmark
    public void readLongHashMapMiss() {
        LongHashMap map = lhm;
        for (int t = 0; t < 10; t++) {
            for (int i = 0; i < n; i++) {
                map.get(i + n);
            }
        }
    }

    @Benchmark
    public void readHashMapMiss() {
        HashMap map = hm;

        for (int t = 0; t < T; t++) {
            for (int i = 0; i < n; i++) {
                map.get(i + n);
            }
        }
    }


    @Benchmark
    public void readPerfectHashMiss() {
        PerfectHashing map = ph;

        for (int t = 0; t < T; t++) {
            for (int i = 0; i < n; i++) {
                map.get(i + n);
            }
        }
    }
}
