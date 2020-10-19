package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class CTetrominoTiling {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int[] a = new int[7];
        in.populate(a);
        long ans = solve(a);
        if (a[0] > 0 && a[3] > 0 && a[4] > 0) {
            long cand = 3;
            a[0]--;
            a[3]--;
            a[4]--;
            cand += solve(a);
            ans = Math.max(ans, cand);
        }
        out.println(ans);
    }

    public long solve(int[] a) {
        long ans = (long)a[1] + a[0] / 2 * 2 + a[3] / 2 * 2 + a[4] / 2 * 2 +
                (a[0] % 2) * (a[3] % 2) * (a[4] % 2) * 3;
        return ans;
    }
}
