package on2021_07.on2021_07_18_AtCoder___AtCoder_Regular_Contest_123.E___Training;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.EuclidLikeFunction;

import java.util.Comparator;
import java.util.function.LongPredicate;

public class ETraining {
    long[] a;
    long[] b;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        a = in.rl(2);
        b = in.rl(2);

        if (Comparator.<long[]>comparingLong(x -> x[0]).thenComparingLong(x -> x[1]).compare(a, b) > 0) {
            long[] tmp = a;
            a = b;
            b = tmp;
        }

        if (a[0] < b[0] && a[1] >= b[1]) {
            out.println(0);
            return;
        }
        if (a[0] == b[0] && a[1] == b[1]) {
            out.println(n);
            return;
        }
        LongPredicate firstMeet = m -> {
            return a[0] + m >= b[0] + m * a[1] / b[1];
        };
        LongPredicate secondMeet = m -> {
            return a[0] + m > b[0] + m * a[1] / b[1];
        };
        LongPredicate thirdMeet = m -> {
            return a[0] + m > b[0] + m * a[1] / b[1] + 1;
        };
        long first = BinarySearch.firstTrue(firstMeet, 0, n / a[1]);
        long second = BinarySearch.firstTrue(secondMeet, 0, n / a[1]);
        long third = BinarySearch.firstTrue(thirdMeet, 0, n / a[1]);
        long ans = sum(first * a[1], second * a[1] - 1, a, b);
        ans += sum(second * a[1], third * a[1] - 1, a, b);
        out.println(ans);
    }

    long n;

    public long sum(long l, long r, long[] a, long[] b) {
        l = Math.max(l, 1);
        r = Math.min(r, n);
        if (l > r) {
            return 0;
        }
        long aSum = EuclidLikeFunction.f(l, r, 1, 0, a[1]) + a[0] * (r - l + 1);
        long bSum = EuclidLikeFunction.f(l, r, 1, 0, b[1]) + b[0] * (r - l + 1);
        long delta = Math.abs(aSum - bSum);
        return (r - l + 1) - delta;
    }
}
