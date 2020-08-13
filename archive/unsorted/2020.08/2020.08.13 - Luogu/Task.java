package contest;

import template.geometry.HalfPlaneIntersection;
import template.geometry.Line2D;
import template.geometry.LineConvexHull;
import template.geometry.LinePolygon;
import template.geometry.Point2D;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class Task {
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

        HalfPlaneIntersection lch = new HalfPlaneIntersection(new LinePolygon(lines), true);
        if(!lch.hasSolution()){
            out.printf("%.3f", 0);
            return;
        }
        double ans = lch.getConvex().asPoints().area();
        out.printf("%.3f", ans);
    }
}
