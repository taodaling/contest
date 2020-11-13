package on2020_11.on2020_11_12_CSES___CSES_Problem_Set.Writing_Numbers;



import template.algo.LongBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitCount;
import template.math.DigitUtils;
import template.math.IntRadix;
import template.math.LongRadix;

public class WritingNumbers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long ans = Math.min(find(n, 0), find(n, 1));
        out.println(ans);
    }

    public long find(long n, int digit) {
        DigitCount dc = new DigitCount();
        LongBinarySearch lbs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                return dc.count(1, mid, digit) > n;
            }
        };
        long ans = lbs.binarySearch(1, (long) 1e18, true);
        return ans;
    }
}
