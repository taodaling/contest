package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;

public class RangeMinimumQueriesI {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        RMQ rmq = new RMQ(n);
        rmq.reset(0, n - 1, (i, j) -> Integer.compare(a[i], a[j]));
        for(int i = 0; i < m; i++){
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            out.println(a[rmq.query(l, r)]);
        }
    }
}
