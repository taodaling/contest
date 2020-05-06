package contest;

import template.algo.LongBinarySearch;
import template.algo.LongTernarySearch;
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
        int[] top = new int[n];

        long topSum = 0;
        for (int i = 0; i < n; i++) {
            double pos = Math.sqrt(a[i] / 3.0);
            int up = (int) Math.ceil(pos);
            int bot = (int) Math.floor(pos);
            top[i] = up;
            if (apply(a[i], top[i]) < apply(a[i], bot)) {
                top[i] = bot;
            }

            topSum += top[i];
        }

        int[] cur;

        if (topSum >= k) {
            LongBinarySearch lbs = new LongBinarySearch() {
                @Override
                public boolean check(long mid) {
                    int cnt = 0;
                    for (int i = 0; i < n; i++) {
                        int index = bs(a[i], mid, 0, top[i]);
                        if (diff(a[i], index - 1) < mid) {
                            index--;
                        }
                        cnt += index;
                    }
                    return cnt >= k;
                }
            };

            long mid = lbs.binarySearch((long)-4e18, (long)4e18);
            cur = new int[n];
            for (int i = 0; i < n; i++) {
                int index = bs(a[i], mid, 0, top[i]);
                long d = diff(a[i], index - 1);
                if (diff(a[i], index - 1) < mid) {
                    index--;
                }
                cur[i] = index;
            }

            fix(a, cur, k, mid);
        }else{
            LongBinarySearch lbs = new LongBinarySearch() {
                @Override
                public boolean check(long mid) {
                    mid = -mid;
                    int cnt = 0;
                    for (int i = 0; i < n; i++) {
                        int index = bs(a[i], mid, top[i], a[i]);
                        if (diff(a[i], index - 1) < mid) {
                            index--;
                        }
                        cnt += index;
                    }
                    return cnt >= k;
                }
            };

            long mid = -lbs.binarySearch((long)-4e18, (long)4e18);
            cur = new int[n];
            for (int i = 0; i < n; i++) {
                int index = bs(a[i], mid, top[i], a[i]);
                if (diff(a[i], index - 1) < mid) {
                    index--;
                }
                cur[i] = index;
            }

            fix(a, cur, k, mid);
        }

        for(int i = 0; i < n; i++){
            out.append(cur[i]).append(' ');
        }
    }

    public void fix(int[] a, int[] cur, long k, long d) {
        int n = cur.length;
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += cur[i];
        }

        for (int i = 0; i < n && sum > k; i++) {
            while (sum > k && diff(a[i], cur[i] - 1) == d) {
                sum--;
                cur[i]--;
            }
        }
    }

    public long apply(long a, long b) {
        return b * (a - b * b);
    }

    //return apply(a, b + 1) - apply(a, b)
    public long diff(long a, long b) {
        //b * (a - (b+1)^2)
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
