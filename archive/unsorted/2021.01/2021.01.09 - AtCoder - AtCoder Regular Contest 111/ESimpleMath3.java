package contest;

import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;

import java.util.function.LongPredicate;

public class ESimpleMath3 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.rl();
        long b = in.rl();
        long c = in.rl();
        long d = in.rl();
        LongPredicate predicate = i -> {
            return (a + i * c) - (a + i * b) + 1 >= d;
        };
        long lim = BinarySearch.lastFalse(predicate, 0, d);
        predicate.test(lim);
        long x = IntMath.sumFloorArithmeticSequence(lim + 1, d, c, a);
        long y = IntMath.sumFloorArithmeticSequence(lim + 1, d, b, a - 1);
        long ans = lim - (x - y);
        out.println(ans);
    }
}
