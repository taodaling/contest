package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.primitve.generated.datastructure.IntegerSparseTable;
import template.utils.Debug;

public class BVasyaAndGoodSequences {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = Long.bitCount(in.readLong());
        }
        IntegerPreSum ps = new IntegerPreSum(i -> a[i], n);
        IntegerSparseTable maxST = new IntegerSparseTable(i -> a[i], n, Math::max);

        long ans = 0;
        //max 120
        int threshold = 61;
        for (int i = 0; i < n; i++) {
            int r = i;
            while (r + 1 < n && r + 1 - i + 1 < threshold) {
                r++;
                if (maxST.query(i, r) * 2 <= ps.intervalSum(i, r) && ps.intervalSum(i, r) % 2 == 0) {
                    ans++;
                    //debug.debug("lr", i + "+" + r);
                }
            }
        }

        int[] cnts = new int[n];
        cnts[0] = 1;
        for (int i = threshold - 1; i < n; i++) {
            if (i - threshold >= 0) {
                cnts[ps.intervalSum(0, i - threshold) % 2]++;
            }
            ans += cnts[ps.intervalSum(0, i) % 2];
        }

        out.println(ans);
    }
}
