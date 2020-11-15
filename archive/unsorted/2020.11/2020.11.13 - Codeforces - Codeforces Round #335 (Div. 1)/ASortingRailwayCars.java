package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ASortingRailwayCars {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] p = new int[n];
        int[] inv = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt() - 1;
            inv[p[i]] = i;
        }
        int ans = 1;
        int last = 1;
        for (int i = 1; i < n; i++) {
            if (inv[i - 1] > inv[i]) {
                last = 1;
            } else {
                last++;
            }
            ans = Math.max(ans, last);
        }
        out.println(n - ans);
    }
}
