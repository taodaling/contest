package contest;

import template.algo.LongBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.ExpressionSolver;

public class IncrementalHouseOfPancakes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);
        long l = in.readLong();
        long r = in.readLong();
        long delta = Math.abs(l - r);

        LongBinarySearch lbs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                return take(mid) > delta;
            }
        };
        long n = lbs.binarySearch(1, (long) 2e9) - 1;
        if (l > r) {
            l -= take(n);
        } else {
            r -= take(n);
        }

        while (l < r && r >= n + 1) {
            r -= n + 1;
            n++;
        }

        long round = n;
        long finalL = l;
        LongBinarySearch left = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                return finalLeft(mid, round, finalL) < 0;
            }
        };

        long finalR = r;
        LongBinarySearch right = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                return finalRight(mid, round, finalR) < 0;
            }
        };

        long lend = left.binarySearch(round, (long) 2e9) - 1;
        long rend = right.binarySearch(round, (long) 2e9) - 1;

        n = Math.min(lend, rend);
        l = finalLeft(n, round, finalL);
        r = finalRight(n, round, finalR);

        out.append(n).append(' ').append(l).append(' ').append(r).println();
    }

    public long finalLeft(long mid, long round, long l) {
        if(mid - round - 1 < 0){
            return l;
        }
        long time = (mid - round - 1) / 2;
        long req = (time + 1) * (round + 1) + 2 * take(time);
        return l - req;
    }

    public long finalRight(long mid, long round, long r) {
        if(mid - round - 2 < 0){
            return r;
        }
        long time = (mid - round - 2) / 2;
        long req = (time + 1) * (round + 2) + 2 * take(time);
        return r - req;
    }

    public long take(long n) {
        return (n + 1) * n / 2;
    }
}
