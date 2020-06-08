package contest;

import template.algo.LongBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.BigIntLinearFunctionCut;
import template.math.DigitUtils;
import template.math.ExtGCD;
import template.math.LinearFunctionCut;
import template.utils.Debug;

import java.math.BigInteger;

public class ASuccessRate {
    Debug debug = new Debug(false);
    ExtGCD extGCD = new ExtGCD();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long x = in.readLong();
        long y = in.readLong();
        long p = in.readLong();
        long q = in.readLong();

        if (p == 0) {
            if (x == 0) {
                out.println(0);
            } else {
                out.println(-1);
            }
            return;
        }

        if (p == q) {
            if (x == y) {
                out.println(0);
            } else {
                out.println(-1);
            }
            return;
        }

        long ans;
        if (compare(x, y, p, q) <= 0) {
            LongBinarySearch lbs = new LongBinarySearch() {
                @Override
                public boolean check(long mid) {
                    return compare(x + mid, y + mid, p, q) >= 0;
                }
            };
            ans = lbs.binarySearch(0, (long) 1e18);
        } else {
            LongBinarySearch lbs = new LongBinarySearch() {
                @Override
                public boolean check(long mid) {
                    return compare(x, y + mid, p, q) <= 0;
                }
            };
            ans = lbs.binarySearch(0, (long) 1e18);
        }
        ans = DigitUtils.topmod(ans + y, q) - y;
        out.println(ans);
    }

    public int compare(long a, long b, long x, long y) {
        return BigInteger.valueOf(a).multiply(BigInteger.valueOf(y)).subtract(BigInteger.valueOf(b).multiply(BigInteger.valueOf(x)))
                .signum();
    }
}
