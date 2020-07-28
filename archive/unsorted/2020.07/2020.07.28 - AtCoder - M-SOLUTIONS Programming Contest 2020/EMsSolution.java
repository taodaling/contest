package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class EMsSolution {
    Point[] pts;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        pts = new Point[n];

        for (int i = 0; i < n; i++) {
            pts[i] = new Point();
            pts[i].x = in.readInt();
            pts[i].y = in.readInt();
            pts[i].w = in.readInt();
        }
        Arrays.sort(pts, (a, b) -> Integer.compare(a.y, b.y));


        long[][] dp = new long[n][n + 1];
        long inf = (long) 1e18;


        long[] ans = new long[n + 1];
        Arrays.fill(ans, inf);
        for (int i = 0; i < 1 << n; i++) {
            for (int j = 0; j < n; j++) {
                pts[j].minNow = Math.min(Math.abs(pts[j].x), Math.abs(pts[j].y));
            }


            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 0) {
                    continue;
                }
                for (int k = 0; k < n; k++) {
                    pts[k].minNow = Math.min(pts[k].minNow, Math.abs(pts[k].x - pts[j].x));
                }
            }
            long sum = 0;
            for (Point point : pts) {
                sum += point.minNow * point.w;
            }
            int bitCount = Integer.bitCount(i);
            ans[bitCount] = Math.min(ans[bitCount], sum);

            //dp
            SequenceUtils.deepFill(dp, inf);
            for (int j = 0; j < n; j++) {
                dp[j][1] = 0;
                for (int k = 0; k <= j; k++) {
                    long cost = Math.min(pts[k].minNow, pts[j].y - pts[k].y);
                    dp[j][1] += cost * pts[k].w;
                }

                for (int t = 0; t < j; t++) {
                    //t + 1 to j
                    long cost = 0;
                    for (int k = t + 1; k <= j; k++) {
                        cost += Math.min(pts[k].minNow, Math.min(pts[j].y - pts[k].y, pts[k].y - pts[t].y)) * pts[k].w;
                    }
                    for (int k = 2; k <= n; k++) {
                        dp[j][k] = Math.min(dp[j][k], dp[t][k - 1] + cost);
                    }
                }

                //if it's the last one
                long cost = 0;
                for (int k = j + 1; k < n; k++) {
                    cost += Math.min(pts[k].minNow, pts[k].y - pts[j].y) * pts[k].w;
                }
                for (int k = 1; k + bitCount <= n; k++) {
                    ans[k + bitCount] = Math.min(ans[k + bitCount], cost + dp[j][k]);
                }
            }
        }

        for(int i = 0; i <= n; i++){
            out.println(ans[i]);
        }
    }


}

class Point {
    int x;
    int y;
    int minNow;
    long w;
}