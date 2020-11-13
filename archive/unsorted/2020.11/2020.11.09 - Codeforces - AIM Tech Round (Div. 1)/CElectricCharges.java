package contest;

import template.algo.IntTernarySearch;
import template.algo.LongBinarySearch;
import template.algo.LongTernarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.List;
import java.util.function.LongUnaryOperator;

public class CElectricCharges {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point(in.readInt(), in.readInt());
        }

        Arrays.sort(pts, (a, b) -> Integer.compare(a.y, b.y));
        this.pts = pts;
        preMax = new int[n];
        preMin = new int[n];
        postMax = new int[n];
        postMin = new int[n];
        for (int i = 0; i < n; i++) {
            preMin[i] = preMax[i] = pts[i].x;
            if (i > 0) {
                preMin[i] = Math.min(preMin[i - 1], preMin[i]);
                preMax[i] = Math.max(preMax[i - 1], preMax[i]);
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            postMax[i] = postMin[i] = pts[i].x;
            if (i + 1 < n) {
                postMin[i] = Math.min(postMin[i + 1], postMin[i]);
                postMax[i] = Math.max(postMax[i + 1], postMax[i]);
            }
        }

        LongBinarySearch lbs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                long ans = Math.min(pow2(preMax[n - 1] - preMin[n - 1]),
                        pow2(pts[n - 1].y - pts[0].y));
                for (int i = 0, j = 0; i < n && pts[i].y <= 0; i++) {
                    j = Math.max(i, j);
                    while (j + 1 < n && Math.abs(pts[j + 1].y) <= Math.abs(pts[i].y) && pow2(pts[j + 1].y - pts[i].y) <= mid) {
                        j++;
                    }
                    while (j > i && Math.abs(pts[j].y) > Math.abs(pts[i].y)) {
                        j--;
                    }
                    ans = Math.min(ans, interval(i, j));
                }
                for (int i = n - 1, j = n - 1; i >= 0 && pts[i].y >= 0; i--) {
                    j = Math.min(i, j);
                    while (j - 1 >= 0 && Math.abs(pts[j - 1].y) <= Math.abs(pts[i].y) && pow2(pts[i].y - pts[j - 1].y) <= mid) {
                        j--;
                    }
                    while (j < i && Math.abs(pts[j].y) > Math.abs(pts[i].y)) {
                        j++;
                    }
                    ans = Math.min(ans, interval(j, i));
                }
                return ans <= mid;
            }
        };


        lbs.check(10);
        long ans = lbs.binarySearch(0, (long) 1e18);
        out.println(ans);
    }


    Point[] pts;
    int[] preMin;
    int[] preMax;
    int[] postMin;
    int[] postMax;
    long inf = (long) 1e9;
    int n;

    public long interval(int l, int r) {
        long dy = pts[r].y - pts[l].y;
        long lx = inf;
        long rx = -inf;

        if (l > 0) {
            lx = Math.min(lx, preMin[l - 1]);
            rx = Math.max(rx, preMax[l - 1]);
        }
        if (r + 1 < n) {
            lx = Math.min(lx, postMin[r + 1]);
            rx = Math.max(rx, postMax[r + 1]);
        }
        long ans = pow2(dy);
        if (lx <= rx) {

            long dx = rx - lx;
            ans = Math.max(ans, pow2(dx));
            long h = Math.max(Math.abs(lx), Math.abs(rx));
            long v = Math.max(Math.abs(pts[r].y), Math.abs(pts[l].y));
            ans = Math.max(ans, dist2(h, v));
        }
        return ans;
    }

    public long pow2(long x) {
        return x * x;
    }

    public long dist2(long x, long y) {
        return pow2(x) + pow2(y);
    }
}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
