package contest;

import template.FastInput;
import template.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] dists = new int[n + 1];
        dists[0] = in.readInt();
        int[] ds = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            ds[i] = in.readInt();
            dists[i] = Math.min(dists[i - 1], Math.abs(dists[i - 1] - ds[i]));
        }

        long minimumFailIndex = 1;
        boolean[] ableToFail = new boolean[n + 1];
        for (int i = n; i >= 1; i--) {
            ableToFail[i] = dists[i - 1] >= minimumFailIndex;
            long next = minimumFailIndex + ds[i];
//            if (ds[i] - minimumFailIndex >= minimumFailIndex) {
//                next = Math.min(next, ds[i] - minimumFailIndex);
//            }
            if (ds[i] >= 2 * minimumFailIndex) {
                next = Math.min(next, minimumFailIndex);
            }
            minimumFailIndex = next;
        }

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int qi = in.readInt();
            out.println(ableToFail[qi] ? "YES" : "NO");
        }
    }
}
