package contest;

import template.algo.DoubleBinarySearch;
import template.algo.DoubleFixRoundBinarySearch;
import template.geometry.geo2.CircleCoverDetect;
import template.geometry.geo2.ConvexHull2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.DoubleIntervalMap;
import template.rand.Randomized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DHongcowDrawsACircle {
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

        List<Point2> pts = new ArrayList<>();
        pts.addAll(Arrays.asList(red));
        pts.addAll(Arrays.asList(blue));
        List<Point2> convex = new ArrayList<>(ConvexHull2.grahamScan(pts));
        for (int i = 0; i < convex.size(); i++) {
            Point2 a = convex.get(i);
            Point2 b = convex.get((i + 1) % convex.size());
            for (Point2 r : red) {
                if (Point2.onSegment(a, b, r)) {
                    out.println(-1);
                    return;
                }
            }
        }

        Randomized.shuffle(red);
        Randomized.shuffle(blue);

        double ans = prec;
        for (Point2 x : red) {
            DoubleFixRoundBinarySearch dbs = new DoubleFixRoundBinarySearch(100) {
                @Override
                public boolean check(double mid) {
                    return !checkRed(red, blue, x, mid);
                }
            };
            if (dbs.check(ans)) {
                continue;
            }
            double local = dbs.binarySearch(ans, 1e20);
            ans = Math.max(ans, local);
        }
        for (Point2 x : blue) {
            DoubleFixRoundBinarySearch dbs = new DoubleFixRoundBinarySearch(100) {
                @Override
                public boolean check(double mid) {
                    return !checkBlue(red, blue, x, mid);
                }
            };
            if (dbs.check(ans)) {
                continue;
            }
            double local = dbs.binarySearch(ans, 1e20);
            ans = Math.max(ans, local);
        }

        out.println(ans);
    }

    double prec = 1e-12;
    CircleCoverDetect ccd = new CircleCoverDetect(prec);
    DoubleIntervalMap dm = new DoubleIntervalMap();

    public boolean check(Point2[] red, Point2[] blue, Point2 center, double r, boolean skip) {
        ccd.reset(center, r, (a, b) -> dm.add(a, b));
        if(!skip)
        for (Point2 b : red) {
            ccd.addCircle(b, r);
        }
        ccd.reset(center, r, (a, b) -> dm.remove(a, b));
        for (Point2 b : blue) {
            ccd.addCircle(b, r);
        }
        return !dm.isEmpty();
    }

    public boolean checkBlue(Point2[] red, Point2[] blue, Point2 center, double r) {
        dm.clear();
        return check(red, blue, center, r, false);
    }

    public boolean checkRed(Point2[] red, Point2[] blue, Point2 center, double r) {
        dm.clear();
        dm.add(0, Math.PI * 2);
        return check(red, blue, center, r, true);
    }


}
