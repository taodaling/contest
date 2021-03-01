package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;

public class RiolkusMockCCCS4ClumsyCindy {
    public void handleLazy(int[] prev) {
        for (int i = 1; i < prev.length; i++) {
            prev[i] = Math.min(prev[i], prev[i - 1]);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] c = in.ri(n);
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.ri();
            b[i] = in.ri();
        }
        int limit = 5000;
        int[] prev = new int[limit + 1];
        int[] next = new int[limit + 1];
        int inf = (int) 1e8;
        Arrays.fill(prev, inf);
        prev[DigitUtils.ceilDiv(c[0], a[0])] = 0;
        for (int i = 0; i < n; i++) {
            Arrays.fill(next, inf);
            handleLazy(prev);
            if (i == n - 1) {
                break;
            }
            for (int j = 0; j <= limit; j++) {
                int remain = Math.max(0, c[i + 1] - j * b[i]);
                int slot = DigitUtils.ceilDiv(remain, a[i + 1]);
                next[slot] = Math.min(next[slot], prev[j] + j);
            }
            int[] tmp = prev;
            prev = next;
            next = tmp;
        }
        int ans = inf;
        for(int i = 0; i <= limit; i++){
            ans = Math.min(ans, prev[i] + i);
        }
        out.println(ans);
    }
}
