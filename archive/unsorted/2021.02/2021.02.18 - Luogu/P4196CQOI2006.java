package contest;

import template.geometry.geo2.HalfPlaneIntersection2;
import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.geometry.old.HalfPlaneIntersection;
import template.geometry.old.Line2D;
import template.geometry.old.LinePolygon;
import template.geometry.old.Point2D;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class P4196CQOI2006 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        List<Line2D> lines = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int m = in.readInt();
            Point2D[] pts = new Point2D[m];
            for (int j = 0; j < m; j++) {
                pts[j] = new Point2D(in.readInt(), in.readInt());
            }
            for (int j = 0; j < m; j++) {
                lines.add(new Line2D(pts[j], pts[(j + 1) % m]));
            }
        }
        HalfPlaneIntersection hpi = new HalfPlaneIntersection(new LinePolygon(lines), true, false);
        double area = 0;
        if (hpi.hasSolution()) {
            area = hpi.getConvex().asPoints().area();
        }
        out.printf("%.3f", area);
    }
}
