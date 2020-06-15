package contest;

import template.algo.DoubleBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class AVoltageKeepsake {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int p = in.readInt();
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point();
            pts[i].a = in.readInt();
            pts[i].b = in.readInt();
            pts[i].threshold = pts[i].b / (double) pts[i].a;
        }

        long sum = Arrays.stream(pts).mapToLong(x -> x.a).sum();
        if (sum <= p) {
            out.println(-1);
            return;
        }
        Arrays.sort(pts, (a, b) -> Double.compare(a.threshold, b.threshold));
        long sumA = 0;
        long sumB = 0;
        double ans = pts[0].threshold;
        for (int i = 0; i < n; i++) {
            sumA += pts[i].a;
            sumB += pts[i].b;
            if (sumA <= p) {
                ans = Math.max(ans, pts[i + 1].threshold);
            } else {
                double local = (double) sumB / (sumA - p);
                if(i + 1 < n) {
                    local = Math.min(local, pts[i + 1].threshold);
                }
                if(local >= pts[i].threshold) {
                    ans = Math.max(ans, local);
                }
            }
        }

        out.println(ans);
    }
}

class Point {
    int a;
    int b;
    double threshold;
}
