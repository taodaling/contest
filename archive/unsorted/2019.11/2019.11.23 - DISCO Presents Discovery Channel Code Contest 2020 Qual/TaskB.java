package contest;

import template.algo.PreSum;
import template.io.FastInput;
import template.io.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = in.readInt();
        }
        PreSum ps = new PreSum(a);
        long ans = (long)1e18;
        for(int i = 0; i < n - 1; i++){
            ans = Math.min(ans, Math.abs(ps.intervalSum(0, i) -
                    ps.intervalSum(i + 1, n - 1)));
        }

        out.println(ans);
    }
}
