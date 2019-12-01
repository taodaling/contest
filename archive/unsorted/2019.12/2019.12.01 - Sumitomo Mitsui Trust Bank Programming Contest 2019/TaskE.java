package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Modular mod = new Modular(1e9 + 7);
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int ans = 1;
        int[] cnt = new int[n];
        for (int i = 0; i < n; i++) {
            if (a[i] > 0) {
                ans = mod.mul(ans, cnt[a[i] - 1] - cnt[a[i]]);
            } else {
                ans = mod.mul(ans, 3 - cnt[a[i]]);
            }
            cnt[a[i]]++;
        }

        out.println(ans);
    }
}
