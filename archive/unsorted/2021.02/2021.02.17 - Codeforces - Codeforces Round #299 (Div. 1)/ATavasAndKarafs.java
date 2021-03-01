package contest;

import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.IntMath;
import template.primitve.generated.datastructure.LongPreSum;

import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

public class ATavasAndKarafs {
    long A;
    long B;

    long get(long i) {
        return A + i * B;
    }

    long inf = (long) 2e18;

    long intervalSum(long l, long r) {
        return DigitUtils.mul(A, (r - l + 1), inf) + DigitUtils.mul(IntMath.sumOfInterval(l, r), B, inf);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        A = in.ri();
        B = in.ri();
        int n = in.ri();
        for (int i = 0; i < n; i++) {
            int l = in.ri() - 1;
            long t = in.ri();
            long m = in.ri();
            LongPredicate predicate = x -> {
                return t * m >= intervalSum(l, x) && t >= get(x);
            };
            long ans = BinarySearch.lastTrue(predicate, l, (long) 1e15);
            if (ans < l) {
                out.println(-1);
            } else {
                out.println(ans + 1);
            }
        }
    }
}
