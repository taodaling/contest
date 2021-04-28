package on2021_04.on2021_04_20_Codeforces___Codeforces_Round__202__Div__1_.A__Mafia;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.function.LongPredicate;

public class AMafia {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        LongPredicate predicate = k -> {
            long sum = 0;
            for (int x : a) {
                if (x > k) {
                    return false;
                }
                sum += Math.max(0, k - x);
            }
            return sum >= k;
        };
        long pos = BinarySearch.firstTrue(predicate, 0, (long) 1e10);
        out.println(pos);
    }
}
