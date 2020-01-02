package on2020_01.on2020_01_01_Codeforces_Round__516__Div__1__by_Moscow_Team_Olympiad_.D__Candies_for_Children0;




import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.ExpressionSolver;
import template.math.LinearIntegerRange;

public class DCandiesForChildren {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long l = in.readLong();
        long r = in.readLong();
        long k = in.readLong();
        long dist = DigitUtils.mod(r - l, n) + 1;

        long max = -1;
        int threshold = 1000000;

        LinearIntegerRange range = new LinearIntegerRange();
        if (n < threshold) {
            for (int i = 0; i <= n; i++) {
                range.reset(0, k);
                range.between(-(i + n), k - dist, 0, Math.min(i, dist));
                range.between(i + n, i - (k - dist), 0, n - dist);
                if(range.valid()){
                    max = Math.max(max, i);
                }
            }
        } else {
            if (dist <= k && k <= dist * 2) {
                max = Math.max(max, k - dist + n - dist);
            }
            for (int i = 1; i * n <= k; i++) {
                long remain = k - i * n - dist;
                if (remain < 0) {
                    continue;
                }
                range.reset(0, n);

                range.lessThanOrEqual(i, remain);
                range.greaterThanOrEqual(i + 1, remain);
                range.greaterThanOrEqual(-i, remain, 0);
                range.lessThanOrEqual(-i, remain, dist);
                range.greaterThanOrEqual(i + 1, -remain, 0);
                range.lessThanOrEqual(i + 1, -remain, n - dist);


                if (range.valid()) {
                    max = Math.max(range.getR(), max);
                }
            }
        }

        //take one last time
        k++;
        if (n < threshold) {
            for (int i = 1; i <= n; i++) {
                range.reset(0, k);
                range.between(-(i + n), k - dist, 1, Math.min(i, dist));
                range.between(i + n, i - (k - dist), 0, n - dist);
                if(range.valid()){
                    max = Math.max(max, i);
                }
            }
        } else {
            if (dist + 1 <= k && k <= dist * 2) {
                max = Math.max(max, k - dist + n - dist);
            }
            for (int i = 1; i * n <= k; i++) {
                long remain = k - i * n - dist;
                if (remain < 0) {
                    continue;
                }
                range.reset(0, n);

                range.lessThanOrEqual(i, remain);
                range.greaterThanOrEqual(i + 1, remain);
                range.greaterThanOrEqual(-i, remain, 1);
                range.lessThanOrEqual(-i, remain, dist);
                range.greaterThanOrEqual(i + 1, -remain, 0);
                range.lessThanOrEqual(i + 1, -remain, n - dist);

                if (range.valid()) {
                    max = Math.max(range.getR(), max);
                }
            }
        }

        out.println(max);
    }
}
