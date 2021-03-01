package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongBIT;
import template.primitve.generated.datastructure.LongBITExt;

public class CPekoraAndTrampoline {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        LongBITExt bit = new LongBITExt(n);
        long ans = 0;
        for (int i = 0; i < n; i++) {
            long access = bit.query(i, i);
            if (a[i] - access > 1) {
                ans += a[i] - 1 - access;
                access = a[i] - 1;
            }
            bit.update(i + 2, Math.min(i + a[i], n), 1);
            bit.update(i + 1, i + 1, access - (a[i] - 1));
        }
        out.println(ans);
    }
}
