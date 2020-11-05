package on2020_11.on2020_11_05_Codeforces___Codeforces_Round__339__Div__1_.A__Peter_and_Snow_Blower;



import template.datastructure.Segment;
import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.HighPrecision;

import java.math.BigDecimal;

public class APeterAndSnowBlower {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point2 center = new Point2(in.readInt(), in.readInt());
        Point2 last = null;
        double maxR = -1;
        double minR = (long) 1e18;
        Point2 first = null;
        for (int i = 0; i < n; i++) {
            Point2 cur = new Point2(in.readInt(), in.readInt());
            double r = Point2.dist(center, cur);
            maxR = Math.max(r, maxR);
            minR = Math.min(r, minR);

            if (last != null) {
                Line2 line = new Line2(last, cur);
                Point2 pt = line.projection(center);
                if (Point2.onSegment(last, cur, pt)) {
                    minR = Math.min(minR, Point2.dist(pt, center));
                }
            }else{
                first = cur;
            }

            last = cur;
        }
        Line2 line = new Line2(last, first);
        Point2 pt = line.projection(center);
        if (Point2.onSegment(last, first, pt)) {
            minR = Math.min(minR, Point2.dist(pt, center));
        }

        double ans = (maxR * maxR - minR * minR) * Math.PI;
        out.println(ans);
    }


    public long dist2(long x, long y, long a, long b) {
        long dx = x - a;
        long dy = y - b;
        return dx * dx + dy * dy;
    }
}
