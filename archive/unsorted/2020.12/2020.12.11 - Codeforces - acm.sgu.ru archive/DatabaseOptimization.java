package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.HashData;
import template.rand.RollingHash;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DatabaseOptimization {

    Map<String, Integer> index = new HashMap<>((int) 1e6);

    public int replace(String s) {
        if (!index.containsKey(s)) {
            index.put(s, index.size());
        }
        return index.get(s);
    }

    public int[] readKV(FastInput in) {
        String[] kv = in.rs().split("=");
        assert kv.length == 2;
        return new int[]{replace(kv[0]), replace(kv[1])};
    }

    public int[][] readPairs(FastInput in) {
        int k = in.ri();
        int[][] kvs = new int[k][];
        for (int j = 0; j < k; j++) {
            int[] kv = readKV(in);
            kvs[j] = kv;
        }
        Arrays.sort(kvs, (a, b) -> Integer.compare(a[0], b[0]));
        return kvs;
    }

    HashData hd1 = new HashData((int) 1e6, (int) 1e9 + 7, 31);
    HashData hd2 = new HashData((int) 1e6, (int) 1e9 + 7, 13);
    RollingHash rh = new RollingHash(hd1, hd2, (int) 1e6);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        LongHashMap recorder = new LongHashMap((int) 1e6, false);
        for (int i = 0; i < n; i++) {
            int[][] kvs = readPairs(in);
            int k = kvs.length;
            for (int j = 0; j < 1 << k; j++) {
                rh.clear();
                for (int t = 0; t < k; t++) {
                    if (Bits.get(j, t) == 1) {
                        rh.addLast(kvs[t][0]);
                        rh.addLast(kvs[t][1]);
                    }
                }
                long hash = rh.hashV();
                recorder.put(hash, recorder.getOrDefault(hash, 0) + 1);
            }
        }
        int m = in.ri();
        for (int i = 0; i < m; i++) {
            int[][] kvs = readPairs(in);
            int k = kvs.length;
            rh.clear();
            for (int t = 0; t < k; t++) {
                rh.addLast(kvs[t][0]);
                rh.addLast(kvs[t][1]);
            }
            long hash = rh.hashV();
            long ans = recorder.get(hash);
            out.println(ans);
        }
    }
}
