package contest;

import template.FastInput;
import template.FastOutput;
import template.GeometryUtils;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.readInt();
        GeometryUtils.Point2D[] triangles = new GeometryUtils.Point2D[3];
        double centerX = 0;
        double centerY = 0;
        for (int i = 0; i < 3; i++) {
            in.readInt();
            triangles[i] = new GeometryUtils.Point2D(in.readInt(), in.readInt());
            centerX += triangles[i].x;
            centerY += triangles[i].y;
        }

        GeometryUtils.DynamicConvexHull dch = new GeometryUtils.DynamicConvexHull(new GeometryUtils.Point2D(centerX / 3, centerY / 3));
        for (int i = 0; i < 3; i++) {
            dch.add(triangles[i]);
        }

        for (int i = 3; i < q; i++) {
            int t = in.readInt();
            GeometryUtils.Point2D pt = new GeometryUtils.Point2D(in.readInt(), in.readInt());
            if (t == 1) {
                dch.add(pt);
            } else {
                boolean contain = dch.contain(pt, true);
                out.println(contain ? "YES" : "NO");
            }
        }
    }
}
