package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;

public class Problem1AboveTheMedian {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int x = in.ri();
        int[] h = new int[n];
        in.populate(h);
        for (int i = 0; i < n; i++) {
            h[i] = h[i] < x ? -1 : 1;
        }
        int fix = n + 1;
        IntegerBIT bit = new IntegerBIT(fix + n);
        long ans = 0;
        int ps = 0;
        bit.update(ps + fix, 1);
        for (int i = 0; i < n; i++) {
            ps += h[i];
            ans += bit.query(ps + fix);
            bit.update(ps + fix, 1);
        }
        out.println(ans);
    }
}
