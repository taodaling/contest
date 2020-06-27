package contest;

import template.datastructure.SegmentBeatExt;
import template.io.FastInput;
import template.io.FastOutput;

public class RangeChminChmaxAddRangeSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        long[] a = new long[n];
        in.populate(a);

        SegmentBeatExt ext = new SegmentBeatExt(0, n - 1, i -> a[i]);
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            int l = in.readInt();
            int r = in.readInt() - 1;
            if (t == 0) {
                //set min
                long b = in.readLong();
                ext.updateMin(l, r, 0, n - 1, b);
            } else if (t == 1) {
                //set max
                long b = in.readLong();
                ext.updateMax(l, r, 0, n - 1, b);
            } else if (t == 2) {
                //update
                long b = in.readLong();
                ext.update(l, r, 0, n - 1, b);
            } else {
                //query
                long sum = ext.querySum(l, r, 0, n - 1);
                out.println(sum);
            }
        }
    }
}
