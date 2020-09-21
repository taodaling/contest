package contest;

import template.algo.DoubleBinarySearch;
import template.geometry.geo2.CircleCoverDetect;
import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.utils.SequenceUtils;

public class EllysIncinerator {
    double eps = 1e-12;

    public double getMax(double[] X, double[] Y, double[] R) {
        int n = X.length;
        double ans = 0;

        Point2[] pts = new Point2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2(X[i], Y[i]);
        }
        CircleCoverDetect ccd = new CircleCoverDetect(eps);
        Point2[] corners = new Point2[]{
                new Point2(0, 0),
                new Point2(1000, 0),
                new Point2(1000, 1000),
                new Point2(0, 1000)
        };

        Line2[] lines = new Line2[4];
        for (int i = 0; i < 4; i++) {
            lines[i] = new Line2(corners[i], corners[(i + 1) % 4]);
        }
        
        for (int i = 0; i < n; i++) {
            int t = i;
            DoubleBinarySearch dbs = new DoubleBinarySearch(eps, eps) {
                @Override
                public boolean check(double mid) {
                    ccd.reset(pts[t], R[t] + mid);
                    for (Line2 line : lines) {
                        ccd.addLine(line);
                    }
                    for (int i = 0; i < n; i++) {
                        if (i == t) {
                            continue;
                        }
                        ccd.addCircle(pts[i], R[i] + mid);
                    }
                    return ccd.findAnyUncoverRadian() < 0;
                }
            };

            double radius = dbs.binarySearch(0, 2000);
            ans = Math.max(ans, radius);
        }
        return ans;
    }
}
