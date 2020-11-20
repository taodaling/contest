package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;

public class StonePile {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int[] subsum = new int[1 << n];
        for (int i = 1; i < 1 << n; i++) {
            int lb = Integer.lowestOneBit(i);
            subsum[i] = subsum[i - lb] + a[Log2.ceilLog(lb)];
        }
        int ans = (int) 1e9;
        for (int i = 0; i < 1 << n; i++) {
            ans = Math.min(ans, Math.abs(subsum[i] - subsum[(1 << n) - 1 - i]));
        }
        out.println(ans);
    }
}
