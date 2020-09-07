package on2020_08.on2020_08_31_Codeforces___Codeforces_Round__666__Div__1_.C__Monster_Invaders;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;

public class CMonsterInvaders {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long r1 = in.readInt();
        long r2 = in.readInt();
        long r3 = in.readInt();
        long d = in.readLong();
        int[] a = new int[n];
        in.populate(a);
        long inf = (long) 1e18;
        long[] last = new long[3];
        long[] next = new long[3];
        Arrays.fill(last, inf);
        last[0] = 0;
        for (int i = 0; i < n; i++) {
            int cnt = a[i];
            Arrays.fill(next, inf);

            if (i < n - 1) {
                long clearCost = Math.min(r1 * cnt + r1 * 2, r2 + r1);
                clearCost = Math.min(clearCost, r1 * cnt + r3);
                next[0] = Math.min(next[0], last[0] + r1 * cnt + r3 + d);
                next[1] = Math.min(next[1], Math.min(last[1], last[0]) + clearCost + d * 3);
                next[2] = Math.min(next[1], Math.min(last[0], last[2]) + clearCost + d * 2);
                next[0] = Math.min(next[0], last[1] + clearCost + d);
            } else {
                long ans = inf;
                ans = Math.min(ans, Math.min(last[0], last[2]) + r1 * cnt + r3);
                ans = Math.min(ans, Math.min(last[0], last[2]) + r2 + r1 + 2 * d);
                ans = Math.min(ans, Math.min(last[0], last[2]) + r1 * cnt + r1 * 2 + 2 * d);

                ans = Math.min(ans, last[1] + r1 * cnt + r3);
                ans = Math.min(ans, last[1] + r2 + r1);
                ans = Math.min(ans, last[1] + r1 * cnt + r1 * 2);

                out.println(ans);
            }

            long[] tmp = last;
            last = next;
            next = tmp;
        }
    }
}
