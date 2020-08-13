package on2020_08.on2020_08_13_Luogu.P2283__HNOI2003____;



import template.geometry.HalfPlaneIntersection;
import template.geometry.geo2.HalfPlaneIntersection2;
import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Collection;

public class P2283HNOI2003 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point2[] pts = new Point2[n];
        for(int i = 0; i < n; i++){
            pts[i] = new Point2(in.readInt(), in.readInt());
        }
        SequenceUtils.reverse(pts);
        Line2[] lines = Line2.asLinePolygon(pts);
        Collection<Line2> convex = HalfPlaneIntersection2.halfPlaneIntersection(lines, true);
        if(convex == null){
            out.printf("%.2f", 0d);
            return;
        }
        Point2[] convexPoints = Point2.asPointPolygon(convex.toArray(new Line2[0]));
        double area = Point2.area(convexPoints);
        out.printf("%.2f", area);
    }
}
