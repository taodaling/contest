package contest;

import template.datastructure.PreXor;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class BXORGun {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        in.populate(a);
        if (n > 100) {
            out.println(1);
            return;
        }
        int ans = inf;
        PreXor xor = new PreXor(a);
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                for (int t = j + 1; t < n; t++) {
                    for (int k = t; k < n; k++) {
                        if (xor.intervalSum(i, j) > xor.intervalSum(t, k)) {
                            ans = Math.min(ans, j - i + k - t);
                        }
                    }
                }
            }
        }
        out.println(ans == inf ? -1 : ans);
    }

    int inf = (int) 1e9;

}
