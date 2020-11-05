package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;

public class RangeSumQueriesI {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        LongPreSum lps = new LongPreSum(i -> a[i], n);
        for(int i = 0; i < m; i++){
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            out.println(lps.intervalSum(l, r));
        }
    }
}
