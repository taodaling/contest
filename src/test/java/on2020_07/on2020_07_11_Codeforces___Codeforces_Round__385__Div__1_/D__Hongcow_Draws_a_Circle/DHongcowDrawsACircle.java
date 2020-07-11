package on2020_07.on2020_07_11_Codeforces___Codeforces_Round__385__Div__1_.D__Hongcow_Draws_a_Circle;



import template.algo.DoubleBinarySearch;
import template.datastructure.DoubleIntervalMap;
import template.geometry.GeoConstant;
import template.geometry.Point2D;
import template.geometry.geo2.ConvexHull2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DHongcowDrawsACircle {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Point2[] red = new Point2[n];
        Point2[] blue = new Point2[m];
        for (int i = 0; i < n; i++) {
            red[i] = new Point2(in.readInt(), in.readInt());
        }
        for (int i = 0; i < m; i++) {
            blue[i] = new Point2(in.readInt(), in.readInt());
        }

        Randomized.shuffle(red);
        Randomized.shuffle(blue);
        List<Point2> allPts = new ArrayList<>(n + m);
        allPts.addAll(Arrays.asList(red));
        allPts.addAll(Arrays.asList(blue));
        Point2[] ch = ConvexHull2.grahamScan(allPts).toArray(new Point2[0]);
        for (Point2 r : red) {
            for (int i = 0; i < ch.length; i++) {
                Point2 cur = ch[i];
                Point2 next = ch[(i + 1) % ch.length];
                if (Point2.onSegment(cur, next, r)) {
                    out.println(-1);
                    return;
                }
            }
        }


        double errorTolerant = 1e-5;
        double low = 0;
        for (Point2 pt : red) {
            DoubleBinarySearch dbs = new DoubleBinarySearch(errorTolerant, errorTolerant) {
                @Override
                public boolean check(double mid) {
                    return !DHongcowDrawsACircle.this.check(red, blue, pt, true, mid);
                }
            };
            if (dbs.check(low)) {
                continue;
            }
            low = dbs.binarySearch(low, 1e30);
        }
        debug.debug("red", low);

        for (Point2 pt : blue) {
            DoubleBinarySearch dbs = new DoubleBinarySearch(errorTolerant, errorTolerant) {
                @Override
                public boolean check(double mid) {
                    return !DHongcowDrawsACircle.this.check(red, blue, pt, false, mid);
                }
            };
            if (dbs.check(low)) {
                continue;
            }
            low = dbs.binarySearch(low, 1e30);
        }

        out.println(low);
    }

    public void radian(DoubleIntervalMap dm, Point2 a, double r, boolean include) {
        if (Point2.dist2(a, Point2.ORIGIN) >= r * r * 4) {
            return;
        }
        if (Point2.SORT_BY_XY.compare(a, Point2.ORIGIN) == 0) {
            return;
        }
        double dx = a.x;
        double dy = a.y;
        double dist = a.abs();
        double mx = a.x / 2;
        double my = a.y / 2;
        double moveX = -dy / dist;
        double moveY = dx / dist;
        double h = Math.sqrt(r * r - dist / 2 * dist / 2);
        double tx = mx + moveX * h;
        double ty = my + moveY * h;
        double bx = mx - moveX * h;
        double by = my - moveY * h;

        double trad = GeoConstant.theta(tx, ty);
        double brad = GeoConstant.theta(bx, by);
        if (trad >= brad) {
            dm.set(brad, trad, include);
        } else {
            dm.set(brad, 2 * Math.PI, include);
            dm.set(0, trad, include);
        }
    }

    public boolean check(Point2[] red, Point2[] blue, Point2 center, boolean isRedCenter, double r) {
        DoubleIntervalMap dm = new DoubleIntervalMap();
        if (isRedCenter) {
            dm.setTrue(0, 2 * Math.PI);
        } else {
            for (Point2 pt : red) {
                radian(dm, Point2.minus(pt, center), r, true);
            }
        }

        for (Point2 pt : blue) {
            radian(dm, Point2.minus(pt, center), r, false);
        }

        return dm.firstTrue(0) != null;
    }
}
