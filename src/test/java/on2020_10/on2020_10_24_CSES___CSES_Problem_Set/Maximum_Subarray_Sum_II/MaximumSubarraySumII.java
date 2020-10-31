package on2020_10.on2020_10_24_CSES___CSES_Problem_Set.Maximum_Subarray_Sum_II;



import template.io.FastInput;
import template.primitve.generated.datastructure.LongComparator;
import template.primitve.generated.datastructure.LongMinQueue;
import template.primitve.generated.datastructure.LongPreSum;

import java.io.PrintWriter;

public class MaximumSubarraySumII {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        int[] x = new int[n];
        in.populate(x);
        LongPreSum lps = new LongPreSum(i -> x[i], n);
        long ans = -(long) 1e18;
        LongMinQueue mq = new LongMinQueue(n + 1, LongComparator.NATURE_ORDER);
        for (int i = 0; i < n; i++) {
            if (i - a >= -1) {
                mq.addLast(lps.prefix(i - a));
            }
            while (mq.size() > b - a + 1) {
                mq.removeFirst();
            }
            if(!mq.isEmpty()) {
                ans = Math.max(ans, lps.prefix(i) - mq.min());
            }
        }
        out.println(ans);
    }
}
