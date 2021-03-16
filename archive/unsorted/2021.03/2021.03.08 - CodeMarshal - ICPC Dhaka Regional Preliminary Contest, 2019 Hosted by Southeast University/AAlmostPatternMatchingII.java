package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.HashData;
import template.rand.IntRangeHash;

public class AAlmostPatternMatchingII {
    int limit = (int) 1.5e6;
    char[] s = new char[limit];
    char[] t = new char[limit];
    HashData[] hd = HashData.doubleHashData(limit);
    IntRangeHash rhs = new IntRangeHash(hd[0], hd[1], limit);
    IntRangeHash rht = new IntRangeHash(hd[0], hd[1], limit);

    private boolean match(int l, int m, int k) {
        for (int i = 0; i < m; i++) {
            if (s[i + l] != t[i]) {
                k--;
            }
        }
        return k >= 0;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.rs(s);
        int m = in.rs(t);
        int k = in.ri();
        rhs.populate(i -> s[i], n);
        rht.populate(i -> t[i], m);
        int ans = 0;
        for (int i = 0; i + m - 1 < n; i++) {
            int err = 0;
            int lastNotMatch = -1;
            while (err <= k && lastNotMatch + 1 < m) {
                int l = lastNotMatch + 1;
                int L = l;
                int r = m - 1;
                while (l < r) {
                    int mid = (l + r) >> 1;
                    if (IntRangeHash.equal(rhs, i + L, i + mid, rht, L, mid)) {
                        l = mid + 1;
                    } else {
                        r = mid;
                    }
                }
                if (!IntRangeHash.equal(rhs, i + L, i + l, rht, L, l)) {
                    err++;
                    lastNotMatch = l;
                } else {
                    break;
                }
            }
            assert match(i, m, k) == (err <= k);
            if (err <= k) {
                ans++;
            }
        }
        out.append("Case ").append(testNumber).append(": ").append(ans).println();
    }
}
