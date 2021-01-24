package contest;

import template.geometry.geo2.IntegerPoint2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;

public class LineSegmentIntersection {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Point2[] pts = new Point2[4];
        for (int i = 0; i < 4; i++) {
            pts[i] = new Point2(in.ri(), in.ri());
        }
        out.println(Point2.intersect(pts[0], pts[1], pts[2], pts[3]) == null ? "NO" : "YES");
    }
}
