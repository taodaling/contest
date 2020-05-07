package on2020_05.on2020_05_07_Codeforces___Codeforces_Round__639__Div__1_.D__R_sum__Review0;




import template.algo.LongBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

public class DRsumReview {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long k = in.readLong();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        int[] cur;
        LongBinarySearch lbs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                long cnt = 0;
                for (int i = 0; i < n; i++) {
                    int index = bs(a[i], mid, 1, a[i]);
                    if (diff(a[i], index - 1) < mid) {
                        index--;
                    }
                    cnt += Math.min(a[i], index);
                }
                return cnt < k;
            }
        };

        long mid = lbs.binarySearch((long) -4e18, (long) 4e18);
        if (lbs.check(mid)) {
            mid--;
        }

        cur = new int[n];
        for (int i = 0; i < n; i++) {
            int index = bs(a[i], mid, 1, a[i]);
            if (diff(a[i], index - 1) < mid) {
                index--;
            }
            cur[i] = Math.min(a[i], index);
        }

        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += cur[i];
        }

        for (int i = 0; i < n && sum > k; i++) {
            while (sum > k && cur[i] > 0 && diff(a[i], cur[i] - 1) == mid) {
                sum--;
                cur[i]--;
            }
        }

        for (int i = 0; i < n; i++) {
            out.append(cur[i]).append(' ');
        }

        //
//        for (int i = 0; i <= 100; i++) {
//            for (int j = 0; j <= 100; j++) {
//                if (apply(i, j + 1) - apply(i, j) != diff(i, j)) {
//                    throw new RuntimeException();
//                }
//            }
//        }
    }

    public long apply(long a, long b) {
        return b * (a - b * b);
    }

    //return apply(a, b + 1) - apply(a, b)
    public long diff(long a, long b) {
        //(b+1) * (a - (b+1)^2)
        return a - 3 * b * b - 3 * b - 1;
    }

    //apply(a + 1, b + 1) - apply(a, b) <= step
    public int bs(int a, long step, int l, int r) {
        while (l < r) {
            int m = (l + r + 1) >>> 1;
            if (diff(a, m - 1) >= step) {
                l = m;
            } else {
                r = m - 1;
            }
        }

        return l;
    }

}
