package on2021_04.on2021_04_11_Codeforces___Codeforces_Round__213__Div__1_.D__Ghd;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.math.GCDs;
import template.primitve.generated.datastructure.LongArrayList;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.RandomWrapper;

import java.util.Arrays;

public class DGhd {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] a = in.rl(n);
        if (n <= 2) {
            long max = Arrays.stream(a).max().orElse(-1);
            out.println(max);
            return;
        }

        long ans = 0;
        long end = System.currentTimeMillis() + 3500;
        while (System.currentTimeMillis() < end) {
            long x = RandomWrapper.INSTANCE.range(a);
            long cand = search((n + 1) / 2, a, x);
            ans = Math.max(ans, cand);
        }

        out.println(ans);
    }

    LongHashMap map = new LongHashMap((int) 1e4, false);

    LongArrayList factors = new LongArrayList(10000);

    public long search(int atLeast, long[] a, long g) {
        factors.clear();
        Factorization.factorizeNumber(g, factors);
        factors.sort();
        long[] fs = factors.toArray();
        int[] cnt = new int[fs.length];


        map.clear();
        for (long x : a) {
            map.modify(GCDs.gcd(g, x), 1);
        }

        for (int i = fs.length - 1; i >= 0; i--) {
            int c = (int) map.get(fs[i]);
            if (c == 0) {
                continue;
            }
            for (int j = i; j >= 0; j--) {
                if (fs[i] % fs[j] == 0) {
                    cnt[j] += c;
                }
            }
            if (cnt[i] >= atLeast) {
                return fs[i];
            }
        }
        return 1;
    }
}
