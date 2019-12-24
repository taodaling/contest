package on2019_12.on2019_12_24_.LUOGU1742;



import template.geometry.Circle;
import template.geometry.Point2D;
import template.io.FastInput;
import template.io.FastOutput;

public class LUOGU1742 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point2D[] pts = new Point2D[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        Circle c = Circle.minCircleCover(pts, 0, n - 1);
        out.printf("%.10f\n%.10f %.10f", c.radius, c.center.x, c.center.y);
    }
}
