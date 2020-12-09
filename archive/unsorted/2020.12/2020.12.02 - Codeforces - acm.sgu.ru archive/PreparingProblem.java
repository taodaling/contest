package contest;

import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class PreparingProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int a = in.ri();
        int b = in.ri();
        IntBinarySearch bs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                return mid / a + mid / b >= n;
            }
        };
        int time = bs.binarySearch(0, (int) 1e9);
        if (time % a != 0) {
            time += a - time % a;
        } else if (time % b != 0) {
            time += b - time % b;
        }
        out.println(Math.min(n + 1, time / a + time / b));
        out.println(time);
    }
}
