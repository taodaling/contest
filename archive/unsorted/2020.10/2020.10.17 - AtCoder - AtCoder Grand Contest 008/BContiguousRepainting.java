package contest;

import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.primitve.generated.datastructure.LongPreSum;

import java.io.PrintWriter;

public class BContiguousRepainting {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        LongPreSum posps = new LongPreSum(i -> Math.max(a[i], 0), n);
        LongPreSum allps = new LongPreSum(i -> a[i], n);
        long ans = 0;
        for (int i = 0; i + k - 1 < n; i++) {
            int l = i;
            int r = i + k - 1;
            ans = Math.max(ans, Math.max(0, allps.intervalSum(l, r)) + posps.prefix(l - 1) + posps.post(r + 1));
        }
        out.println(ans);
    }
}
