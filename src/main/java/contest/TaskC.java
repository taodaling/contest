package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] x = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = in.readInt();
        }
        int ans = (int) 1e9;
        for (int i = 0; i + k - 1 < n; i++) {
            int l = i;
            int r = i + k - 1;
            if (x[r] <= 0) {
                ans = Math.min(ans, -x[l]);
            } else if (x[l] >= 0) {
                ans = Math.min(ans, x[r]);
            } else {
                ans = Math.min(ans, x[r] - x[l] * 2);
                ans = Math.min(ans, x[r] * 2 - x[l]);
            }
        }

        out.println(ans);
    }
}
