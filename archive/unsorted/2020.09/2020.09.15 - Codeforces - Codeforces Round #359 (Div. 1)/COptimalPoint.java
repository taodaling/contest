package contest;

import template.algo.LongBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

public class COptimalPoint {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        if(n == 1){
            out.append(in.readLong()).append(' ')
                    .append(in.readLong()).append(' ')
                    .append(in.readLong()).println();
            return;
        }
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point();
            pts[i].x = in.readLong();
            pts[i].y = in.readLong();
            pts[i].z = in.readLong();
        }

        LongBinarySearch lbs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                return COptimalPoint.this.check(pts, mid) != null;
            }
        };

        long ans = lbs.binarySearch(0, (long) 3e18);
        long[] center = check(pts, ans);
        for (long x : center) {
            out.append(x).append(' ');
        }
        out.println();
    }

    public void update(long[] lr, long l, long r) {
        lr[0] = Math.max(lr[0], l);
        lr[1] = Math.min(lr[1], r);
    }

    public long[] possible(long[][] systems) {
        for (int i = 0; i < 4; i++) {
            if (systems[i][0] > systems[i][1]) {
                return null;
            }
        }

        long floor = 0;
        long ceil = 0;
        for (int i = 1; i < 4; i++) {
            floor += systems[i][0];
            ceil += systems[i][1];
        }

        if (floor > systems[0][1] || ceil < systems[0][0]) {
            return null;
        }

        long[] cur = new long[]{
                systems[1][0],
                systems[2][0],
                systems[3][0]
        };
        for (int i = 1; i <= 3 && floor < systems[0][0]; i++) {
            long add = Math.min(systems[0][0] - floor, systems[i][1] - systems[i][0]);
            floor += add;
            cur[i - 1] += add;
        }

        return cur;
    }

    long[][] odd = new long[4][2];
    long[][] even = new long[4][2];
    public long[] check(Point[] pts, long radis) {
        //0 +++
        //1 -++
        //2 +-+
        //3 ++-
        long inf = (long) 4e18;
        for (int i = 0; i < 4; i++) {
            even[i][0] = odd[i][0] = -inf;
            even[i][1] = odd[i][1] = inf;
        }

        for (Point pt : pts) {
            //+++
            //---
            long sum1 = pt.x + pt.y + pt.z;
            long sum2 = -pt.x + pt.y + pt.z;
            long sum3 = pt.x - pt.y + pt.z;
            long sum4 = pt.x + pt.y - pt.z;

            update(odd[0], sum1 - radis,
                    sum1 + radis);

            //-++
            //+--
            update(odd[1], sum2 - radis, sum2 + radis);

            //+-+
            //-+-
            update(odd[2], sum3 - radis, sum3 + radis);

            //++-
            //--+
            update(odd[3], sum4 - radis, sum4 + radis);
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                even[i][j] = odd[i][j];
            }
            long sub = i == 0 ? 3 : 1;
            even[i][0] = DigitUtils.ceilDiv(even[i][0], 2);
            even[i][1] = DigitUtils.floorDiv(even[i][1], 2);
            odd[i][0] = DigitUtils.ceilDiv(odd[i][0] - sub, 2);
            odd[i][1] = DigitUtils.floorDiv(odd[i][1] - sub, 2);
        }

        long[] ans = possible(even);
        if (ans == null) {
            ans = possible(odd);
            if (ans == null) {
                return null;
            }
            for (int i = 0; i < 3; i++) {
                ans[i] = ans[i] * 2 + 1;
            }
        } else {
            for (int i = 0; i < 3; i++) {
                ans[i] = ans[i] * 2;
            }
        }

        long a = ans[0];
        long b = ans[1];
        long c = ans[2];
        return new long[]{(b + c) / 2,
                (a + c) / 2,
                (a + b) / 2};
    }

}

class Point {
    long x;
    long y;
    long z;
}
