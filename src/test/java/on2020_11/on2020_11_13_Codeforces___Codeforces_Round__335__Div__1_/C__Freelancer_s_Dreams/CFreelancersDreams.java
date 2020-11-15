package on2020_11.on2020_11_13_Codeforces___Codeforces_Round__335__Div__1_.C__Freelancer_s_Dreams;



import template.geometry.geo2.ConvexHull2;
import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DualLinearProgramming;
import template.math.LinearProgramming;

import java.util.Arrays;

public class CFreelancersDreams {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int p = in.readInt();
        int q = in.readInt();
        Point2 target = new Point2(p, q);
        Line2 to = new Line2(Point2.ORIGIN, target);
        Point2[] pts = new Point2[n + 1];
        double ans = 1e18;
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2(in.readInt(), in.readInt());
            ans = Math.min(Math.max(target.x / pts[i].x, target.y / pts[i].y), ans);
        }
        pts[n] = Point2.ORIGIN;
        Point2[] ch = ConvexHull2.grahamScan(Arrays.asList(pts)).toArray(new Point2[0]);
        for (int i = 0; i < ch.length; i++) {
            Point2 cur = ch[i];
            Point2 next = ch[(i + 1) % ch.length];
            if (cur == Point2.ORIGIN || next == Point2.ORIGIN) {
                continue;
            }
            Line2 line = new Line2(cur, next);
            if (to.side(cur) != to.side(next)) {
                Point2 pt = Line2.intersect(to, line);
                ans = Math.min(ans, Math.max(target.x / pt.x, target.y / pt.y));
            }
        }
        out.println(ans);
    }
}
