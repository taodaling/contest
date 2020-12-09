package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;

public class Hippopotamus {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        long[] prev = new long[1 << m];
        long[] next = new long[1 << m];
        prev[0] = 1;
        int mask = (1 << m) - 1;
        for (int i = 0; i <= n; i++) {
            if (i >= m) {
                for (int j = 0; j < 1 << m; j++) {
                    if (Integer.bitCount(j) < k) {
                        prev[j] = 0;
                    }
                }
            }

            debug.debug("i", i);
            debug.debug("prev", prev);
            if (i == n) {
                break;
            }

            Arrays.fill(next, 0);
            for (int j = 0; j < 1 << m; j++) {
                if(prev[j] == 0){
                    continue;
                }
                next[(j << 1) & mask] += prev[j];
                next[((j << 1) | 1) & mask] += prev[j];
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }

        long ans = Arrays.stream(prev).sum();
        out.println(ans);
    }
}
