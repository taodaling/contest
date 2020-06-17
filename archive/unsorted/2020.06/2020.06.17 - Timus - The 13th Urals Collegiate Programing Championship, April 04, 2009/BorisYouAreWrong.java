package contest;



import template.geometry.GeoConstant;
import template.geometry.Triangle;
import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;

public class BorisYouAreWrong {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Point2 a = new Point2(in.readInt(), in.readInt());
        Point2 b = new Point2(in.readInt(), in.readInt());
        Point2 c = new Point2(in.readInt(), in.readInt());

        Line2 ac = new Line2(a, c);
        Point2 proj = ac.projection(b);
        if (Point2.SORT_BY_XY.compare(c, proj) == 0 || GeoConstant.compare(Point2.dist2(proj, a), Point2.dist2(proj, c)) <= 0) {
            out.println("YES");
            return;
        }

        Point2 mirror = Point2.mirror(proj, c);
        out.println("NO");
        for (Point2 pt : new Point2[]{a, b, mirror}) {
            out.append(pt.x).append(' ').append(pt.y).println();
        }
    }
}
