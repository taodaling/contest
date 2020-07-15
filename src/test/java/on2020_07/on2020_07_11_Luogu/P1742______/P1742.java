package on2020_07.on2020_07_11_Luogu.P1742______;



import template.geometry.geo2.Circle2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;

public class P1742 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point2[] pts = new Point2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2(in.readDouble(), in.readDouble());
        }
        Circle2 c = Circle2.minCircleCover(pts, 0, pts.length - 1);
        out.printf("%.10f\n%.10f %.10f", c.r, c.center.x, c.center.y);
    }
}
