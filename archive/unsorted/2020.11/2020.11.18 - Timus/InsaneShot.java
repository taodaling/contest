package contest;

import template.geometry.geo2.Circle2;
import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class InsaneShot {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        Point2 center = new Point2(in.readInt(), in.readInt());
        double r = in.readInt();
        Circle2 c = new Circle2(center, r);

        Point2 a = new Point2(in.readInt(), in.readInt());
        Point2 b = new Point2(in.readInt(), in.readInt());
        if (c.intersect(new Line2(a, b), new ArrayList<>()) > 0) {
            out.println("No way");
            return;
        }

        Point2 middle = Point2.middle(a, b);

        List<Point2> collect = new ArrayList<>();
        c.intersect(new Line2(center, middle), collect);
        for (Point2 pt : collect) {
            if (Point2.onSegment(center, middle, pt)) {
                out.append(pt.x).append(' ').append(pt.y);
                return;
            }
        }
    }
}
