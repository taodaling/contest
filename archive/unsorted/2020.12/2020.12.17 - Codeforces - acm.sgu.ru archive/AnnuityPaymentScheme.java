package contest;

import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.KahamSummation;

import java.util.function.DoublePredicate;

public class AnnuityPaymentScheme {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int s = in.ri();
        int m = in.ri();
        double p = in.ri() / 100d;
        DoublePredicate predicate = mid -> {
            KahamSummation sum = new KahamSummation();
            for (int i = 0; i < m; i++) {
                double tax = Math.max(0, s - sum.sum()) * p;
                double remain = mid - tax;
                sum.add(remain);
            }
            return sum.sum() >= s;
        };
        double ans = BinarySearch.firstTrue(predicate, 0, s * 2, 1e-8, 1e-20);
        out.println(ans);
    }
}
