package contest;

import template.datastructure.XorBIT;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerGenericBIT;
import template.primitve.generated.datastructure.LongGenericBIT;

public class RangeXorQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        IntegerGenericBIT bit = new IntegerGenericBIT(n, (x, y) -> x ^ y, 0);
        for (int i = 0; i < n; i++) {
            bit.update(i + 1, a[i]);
        }
        for (int i = 0; i < m; i++) {
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            int ans = bit.query(r + 1) ^ bit.query(l);
            out.println(ans);
        }
    }
}
