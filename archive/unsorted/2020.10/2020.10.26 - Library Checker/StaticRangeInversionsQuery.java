package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToLongFunction;
import template.primitve.generated.datastructure.LongArrayList;
import template.problem.IntervalInversePairProblem;

public class StaticRangeInversionsQuery {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        IntervalInversePairProblem prob = new IntervalInversePairProblem(i -> a[i], n);
        for (int i = 0; i < m; i++) {
            int l = in.readInt();
            int r = in.readInt() - 1;
            long ans = prob.query(l, r);
            out.println(ans);
        }
    }
}
