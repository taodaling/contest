package contest;

import template.datastructure.RMQBySegment;
import template.io.FastInput;
import template.io.FastOutput;

public class FTheNumberOfSubpermutations {
    long[] perm;
    long[] ps;
    int[] a;

    public long interval(int l, int r) {
        if (l == 0) {
            return ps[r];
        }
        return MultiSetHash.remove(ps[r], ps[l - 1]);
    }

    RMQBySegment rmq;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        a = new int[n];
        ps = new long[n];
        perm = new long[n + 1];
        MultiSetHash hash = new MultiSetHash(n);
        for (int i = 1; i <= n; i++) {
            perm[i] = hash.hashcode(i);
        }
        for (int i = 0; i < n; i++) {
            a[i] = in.ri();
            ps[i] = perm[a[i]];
        }
        for (int i = 2; i <= n; i++) {
            perm[i] = MultiSetHash.merge(perm[i - 1], perm[i]);
        }
        for (int i = 1; i < n; i++) {
            ps[i] = MultiSetHash.merge(ps[i], ps[i - 1]);
        }
        rmq = new RMQBySegment(n, (i, j) -> -Integer.compare(a[i], a[j]));

        long ans = dfs(0, n - 1);
        out.println(ans);
    }

    public long dfs(int l, int r) {
        if (l > r) {
            return 0;
        }
        int m = rmq.query(l, r);
        int max = a[m];
        long ans = 0;
        for (int i = Math.max(m - max + 1, l); i <= m && i + max - 1 <= r; i++) {
            if (interval(i, i + max - 1) == perm[max]) {
                ans++;
            }
        }
        ans += dfs(l, m - 1);
        ans += dfs(m + 1, r);
        return ans;
    }
}
