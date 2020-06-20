package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.datastructure.LongPreSum;
import template.utils.Debug;

public class CEugeneAndAnArray {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        LongPreSum lps = new LongPreSum(a);
        LongHashMap lhm = new LongHashMap(n + 1, false);
        lhm.put(0, n);
        int[] rights = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            long suffix = lps.post(i);
            rights[i] = (int) lhm.getOrDefault(suffix, n + 1) - 1;
            lhm.put(suffix, i);
        }

        IntegerRMQ rmq = new IntegerRMQ(rights, Integer::compare);
        int r = 0;
        long ans = 0;
        for (int i = 0; i < n; i++) {
            r = Math.max(r, i);
            int until = rights[rmq.query(i, r)];
            while (r + 1 < n && r + 1 < until && a[r + 1] != 0) {
                r++;
                until = (int) Math.min(until, rights[r]);
            }
            if(r < until) {
                ans += r - i + 1;
            }
        }

        debug.debug("rights", rights);
        out.println(ans);
    }
}
