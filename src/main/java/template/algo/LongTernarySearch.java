package template.algo;

import template.math.DigitUtils;
import java.util.function.LongUnaryOperator;

/**
 * Used to find the minimum value of a lower convex.
 * Assume f(-inf)<...<f(ans)=f(ans+1)=...=f(ans+k)>...>f(inf)
 */
public class LongTernarySearch {
    private LongUnaryOperator operator;

    public LongTernarySearch(LongUnaryOperator operator) {
        this.operator = operator;
    }

    public long find(long l, long r) {
        while (r - l > 2) {
            long ml = l + DigitUtils.floorDiv(r - l, 3);
            long mr = r - DigitUtils.ceilDiv(r - l, 3);
            if (operator.applyAsLong(ml) < operator.applyAsLong(mr)) {
                l = ml;
            } else {
                r = mr;
            }
        }
        while (l < r) {
            if (operator.applyAsLong(l) >= operator.applyAsLong(r)) {
                r--;
            } else {
                l++;
            }
        }
        return l;
    }
}
