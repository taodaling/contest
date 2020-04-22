package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;

public class APride {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        int[] a = new int[n];

        int cnt = 0;
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            if (a[i] == 1) {
                cnt++;
            }
        }

        if (cnt > 0) {
            out.println(n - cnt);
            return;
        }

        int inf = (int) 1e8;
        int len = inf;
        for (int i = 0; i < n; i++) {
            int v = a[i];
            int j = i;
            while (v != 1 && j + 1 < n) {
                v = GCDs.gcd(v, a[j + 1]);
                j++;
            }
            if (v == 1) {
                len = Math.min(len, j - i + 1);
            }
        }

        if (len == inf) {
            out.println(-1);
            return;
        }

        int ans = n - 2 + len;
        out.println(ans);
    }
}
