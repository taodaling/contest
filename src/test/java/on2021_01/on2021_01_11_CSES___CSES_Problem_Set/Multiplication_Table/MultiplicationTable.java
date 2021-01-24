package on2021_01.on2021_01_11_CSES___CSES_Problem_Set.Multiplication_Table;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongPreSum;

import java.util.function.LongPredicate;

public class MultiplicationTable {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.ri();
        long half = DigitUtils.ceilDiv(n * n, 2);
        LongPredicate predicate = m -> {
            long sum = 0;
            for (long i = 1, to; i <= n; i = to + 1) {
                long v = m / i;
                if (v == 0) {
                    to = n;
                } else {
                    to = Math.min(n, m / v);
                }
                sum += (to - i + 1) * Math.min(v, n);
            }
            return sum >= half;
        };
        long ans = BinarySearch.firstTrue(predicate, 1, n * n);
        out.println(ans);
    }
}
