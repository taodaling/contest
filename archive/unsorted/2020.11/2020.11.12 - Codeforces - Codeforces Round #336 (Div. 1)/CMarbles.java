package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.HashData;
import template.rand.IntRangeHash;
import template.rand.RangeHash;
import template.utils.SequenceUtils;

public class CMarbles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt() - 1;
        char[] a = new char[n];
        char[] b = new char[n];
        in.readString(a, 0);
        in.readString(b, 0);
        SequenceUtils.reverse(a);
        char[] rev = new char[128];
        rev['W'] = 'E';
        rev['E'] = 'W';
        rev['N'] = 'S';
        rev['S'] = 'N';
        for (int i = 0; i < n; i++) {
            a[i] = rev[a[i]];
        }
        HashData d1 = new HashData(n);
        HashData d2 = new HashData(n);
        IntRangeHash ah = new IntRangeHash(d1, d2, n);
        IntRangeHash bh = new IntRangeHash(d1, d2, n);
        ah.populate(i -> a[i], n);
        bh.populate(i -> b[i], n);
        for (int i = 1; i <= n; i++) {
            int l = 0;
            int r = i - 1;
            int rr = n - 1;
            int ll = rr - i + 1;
            if (ah.hash(l, r) == bh.hash(ll, rr)) {
                out.println("NO");
                return;
            }
        }
        out.println("YES");

    }
}
