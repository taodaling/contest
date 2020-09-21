package contest;

import template.algo.DoubleBinarySearch;
import template.geometry.geo2.CircleCoverDetect;
import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.DoubleComparator;
import template.primitve.generated.datastructure.DoubleDeque;
import template.primitve.generated.datastructure.DoubleDequeImpl;
import template.primitve.generated.datastructure.DoublePriorityQueue;
import template.primitve.generated.utils.DoubleBinaryConsumer;
import template.rand.Randomized;

import java.util.ArrayList;
import java.util.List;

public class EGenaAndSecondDistance {

    double eps = 1e-12;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int w = in.readInt();
        int h = in.readInt();
        int n = in.readInt();

        Point2[] pts = new Point2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2(in.readInt(), in.readInt());
        }

        Point2[] corners = new Point2[]{
                new Point2(0, 0),
                new Point2(w, 0),
                new Point2(w, h),
                new Point2(0, h)
        };
        Line2[] lines = new Line2[4];
        for (int i = 0; i < 4; i++) {
            lines[i] = new Line2(corners[i], corners[(i + 1) % 4]);
        }

        Randomized.shuffle(pts);
        double ans = 1e-9;


        for (int i = 0; i < n; i++) {
            if (!check(pts, pts[i], ans, lines)) {
                continue;
            }
            Point2 center = pts[i];
            DoubleBinarySearch dbs = new DoubleBinarySearch(eps, eps) {
                @Override
                public boolean check(double mid) {
                    return !EGenaAndSecondDistance.this.check(pts, center, mid, lines);
                }
            };
            double local = dbs.binarySearch(ans, h + w);
            ans = Math.max(ans, local);
        }

        out.println(ans);
    }

    CircleCoverDetect ccd = new CircleCoverDetect(eps);
    DoublePriorityQueue left = new DoublePriorityQueue(10000, DoubleComparator.NATURE_ORDER);
    DoublePriorityQueue right = new DoublePriorityQueue(10000, DoubleComparator.NATURE_ORDER);

    public boolean check(Point2[] pts, Point2 center, double r, Line2[] borders) {
        left.clear();
        right.clear();
        ccd.reset(center, r, (a, b) -> {
            left.add(a);
            right.add(b);
        });
        for (Line2 line : borders) {
            ccd.addLine(line);
            ccd.addLine(line);
        }
        for (Point2 pt : pts) {
            ccd.addCircle(pt, r);
        }

        double cnt = 0;

        if (left.isEmpty() || left.peek() > 0) {
            return true;
        }
        while (!right.isEmpty()) {
            double min = right.peek();
            if (!left.isEmpty()) {
                min = Math.min(left.peek(), min);
            }
            while (!left.isEmpty() && left.peek() == min) {
                left.pop();
                cnt++;
            }
            while (!right.isEmpty() && right.peek() == min) {
                right.pop();
                cnt--;
            }
            if (min == 2 * Math.PI) {
                return false;
            }
            if (cnt < 2) {
                return true;
            }
        }
        return true;

    }
}
