package on2021_07.on2021_07_14_DMOJ___DMOPC__20_Contest_2.P6___Unfair_Game;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;

import java.util.Arrays;

public class P6UnfairGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n + q);
        long[] prev = new long[2];
        long[] next = new long[2];
        long inf = (long) 1e18;
        Arrays.fill(prev, -inf);
        prev[1] = 0;
        int last = 0;
        long ps = 0;
        for (int i = 0; i < n + q; i++) {
            ps += a[i];
            Arrays.fill(next, -inf);
            for (int j = 0; j < 2; j++) {
                //do nothing
                next[0] = Math.max(next[0], prev[j]);
                //add
                if (j == 0) {
                    next[1] = Math.max(next[1], prev[j] + Math.min(last, a[i]));
                }
            }
            last = a[i];
            long[] tmp = prev;
            prev = next;
            next = tmp;
            if (i >= n - 1) {
                long b = Math.max(prev[0], prev[1]);
                long remain = ps - b;
                out.println(remain);
            }
        }
    }
}
