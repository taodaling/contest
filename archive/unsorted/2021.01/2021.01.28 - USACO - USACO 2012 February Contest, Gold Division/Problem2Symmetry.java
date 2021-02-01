package contest;

import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.*;

public class Problem2Symmetry {
    TreeMap<Point2, Integer> indexMap;
    Set<Long> set;

    public void test(Line2 line, Point2[] pts) {
        boolean ok = true;
        for (Point2 pt : pts) {
            Point2 mirror = line.reflect(pt);
            if (!indexMap.containsKey(mirror)) {
                ok = false;
                break;
            }
        }
        if (ok) {
            int a = indexMap.get(line.reflect(pts[0]));
            int b = indexMap.get(line.reflect(pts[1]));
            set.add(DigitUtils.asLong(a, b));
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Point2[] pts = new Point2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2(in.ri(), in.ri());
        }
        indexMap = new TreeMap<>(Point2.SORT_BY_XY);
        for (int i = 0; i < n; i++) {
            indexMap.put(pts[i], i);
        }
        set = new HashSet<>(n * 2 + 1);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < n; j++) {
                if (j == i) {
                    continue;
                }
                Line2 line = new Line2(pts[i], pts[j]).perpendicularThrough(Point2.middle(pts[i], pts[j]));
                test(line, pts);
            }
        }
        test(new Line2(pts[0], pts[1]), pts);
        out.println(set.size());
    }
}
