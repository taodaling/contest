package contest;

import template.geometry.Point2D;
import template.geometry.PointConvexHull;
import template.geometry.PointPolygon;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.KahanSummation;
import template.utils.Debug;
import template.utils.GeometryUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CHoles {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point2D[] pts = new Point2D[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2D(in.readInt(), in.readInt());
        }

        if (n == 2) {
            for (int i = 0; i < 2; i++) {
                out.println(0.5);
            }
            return;
        }

        PointPolygon pointPolygon = new PointPolygon(Arrays.asList(pts));
        PointConvexHull ch = PointConvexHull.grahamScan(pointPolygon);
        List<Point2D> outside = ch.getData();
        Map<Point2D, Double> prob = new HashMap<>(n);
        KahanSummation sum = new KahanSummation();
        if (outside.size() == 2) {
            for (Point2D pt : outside) {
                prob.put(pt, 0.5);
            }
            sum.add(1);
        } else {
            for (int i = 0; i < outside.size(); i++) {
                Point2D a = outside.get(i);
                Point2D b = outside.get(DigitUtils.mod(i - 1, outside.size()));
                Point2D c = outside.get(DigitUtils.mod(i + 1, outside.size()));
                double ab = a.distanceBetween(b);
                double ac = a.distanceBetween(c);
                double bc = b.distanceBetween(c);
                double ans = Math.PI - GeometryUtils.triangleAngle(bc, ab, ac);
                prob.put(a, ans);
                sum.add(ans);
            }
        }

        debug.debug("sum", sum);
        for (Point2D pt : pts) {
            double ans = prob.getOrDefault(pt, 0D);
            ans /= sum.sum();
            out.println(ans);
        }
    }


}
