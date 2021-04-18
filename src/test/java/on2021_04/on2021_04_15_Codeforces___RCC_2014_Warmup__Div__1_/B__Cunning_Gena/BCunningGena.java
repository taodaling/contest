package on2021_04.on2021_04_15_Codeforces___RCC_2014_Warmup__Div__1_.B__Cunning_Gena;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;

public class BCunningGena {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long b = in.ri();
        long[] prev = new long[1 << m];
        long[] next = new long[1 << m];
        long inf = (long) 2e18;
        Arrays.fill(prev, inf);
        prev[0] = 0;
        Friend[] friends = new Friend[n];
        for (int i = 0; i < n; i++) {
            friends[i] = new Friend();
            friends[i].extra = in.ri();
            friends[i].k = in.ri();
            int num = in.ri();
            for (int j = 0; j < num; j++) {
                friends[i].p |= 1 << in.ri() - 1;
            }
        }
        Arrays.sort(friends, Comparator.comparingInt(x -> x.k));
        int lastK = 0;
        long best = inf;
        for (Friend f : friends) {
            debug.debug("prev", prev);
            long forMonitor = (f.k - lastK) * b;
            lastK = f.k;
            Arrays.fill(next, inf);
            for (int i = 0; i < 1 << m; i++) {
                int to = i | f.p;
                next[to] = Math.min(next[to], prev[i] + f.extra + forMonitor);
                next[i] = Math.min(next[i], prev[i] + forMonitor);
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
            best = Math.min(best, prev[(1 << m) - 1]);
        }

        debug.debug("prev", prev);
        long ans = best;
        if (ans >= inf) {
            out.println(-1);
            return;
        }
        out.println(ans);
    }
}

class Friend {
    int p;
    int k;
    int extra;

}
