package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class CollectingNumbers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] x = new int[n];
        in.populate(x);
        int[] inv = new int[n + 1];
        for (int i = 0; i < n; i++) {
            inv[x[i]] = i;
        }
        int ans = 1;
        for (int i = 2; i <= n; i++) {
            if (inv[i] < inv[i - 1]) {
                ans++;
            }
        }
        out.println(ans);
    }
}
