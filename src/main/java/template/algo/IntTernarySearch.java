package template.algo;

import java.util.function.IntUnaryOperator;

/**
 * Used to find the minimum value of a lower convex.
 * Assume f(-inf)>...>f(ans)=f(ans+1)=...=f(ans+k)>...>f(inf)
 */
public class IntTernarySearch {
    private IntUnaryOperator operator;

    public IntTernarySearch(IntUnaryOperator operator) {
        this.operator = operator;
    }

    public int find(int l, int r) {
        while (r - l > 2) {
            int dist = (r - l) / 3;
            int ml = l + dist;
            int mr = r - dist;
            if (operator.applyAsInt(ml) < operator.applyAsInt(mr)) {
                l = ml;
            } else {
                r = mr;
            }
        }
        while (l < r) {
            if (operator.applyAsInt(l) < operator.applyAsInt(r)) {
                r--;
            } else {
                l++;
            }
        }
        return l;
    }
}
