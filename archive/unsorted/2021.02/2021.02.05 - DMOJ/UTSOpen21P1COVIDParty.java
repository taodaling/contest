package contest;

import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.function.LongPredicate;

public class UTSOpen21P1COVIDParty {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        LongPredicate lp = m -> {
            long hu = (m + 1) / 2;
            long hd = m / 2;
            return hu * hu + hd * hd >= n;
        };
        long ans = BinarySearch.firstTrue(lp, 1, (int)1e9);
        out.println(ans - 1);
    }
}
