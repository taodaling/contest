package on2021_09.on2021_09_15_CS_Academy___Virtual_Round__41.Cinema_Seats;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;

import java.util.function.IntPredicate;

public class CinemaSeats {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] state = in.rs().toCharArray();
        int n = state.length;
        IntegerPreSum ps = new IntegerPreSum(i -> state[i] - '0', n);

        IntPredicate pred = m -> {
            for (int i = 0; i + m - 1 < n; i++) {
                int l = i;
                int r = i + m - 1;
                if (ps.intervalSum(l, r) <= 1) {
                    return true;
                }
            }
            return false;
        };
        int ans = BinarySearch.lastTrue(pred, 0, n);
        out.println(ans);
    }
}
