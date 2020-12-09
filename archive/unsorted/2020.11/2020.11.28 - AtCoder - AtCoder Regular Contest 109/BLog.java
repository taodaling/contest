package contest;

import template.algo.LongBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;

public class BLog {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        long ans = n;
        LongBinarySearch lbs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                return IntMath.sumOfInterval(1, mid) > n + 1;
            }
        };
        long replace = lbs.binarySearch(1, (long) 2e9, true);
        ans += 1 - replace;
        out.println(ans);
    }
}
