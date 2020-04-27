package on2020_04.on2020_04_27_Kattis.Airport_Construction;



import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AirportConstruction {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point2[] pts = new Point2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2(in.readInt(), in.readInt());
        }
        List<Point2> ptList = Arrays.asList(pts);
        Line2[] lines = new Line2[n];
        for (int i = 0; i < n; i++) {
            lines[i] = new Line2(pts[i], pts[(i + 1) % n]);
        }
        double ans = 0;
        for (int i = 0; i < n; i++) {
            Point2 cur = pts[i];
            Point2 next = pts[(i + 1) % n];
            ans = Math.max(ans, Point2.dist2(cur, next));
        }
        List<Point2> ps = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                ps.clear();
                Line2 line = new Line2(pts[i], pts[j]);
                for (int k = 0; k < n; k++) {
                    Point2 a = pts[k];
                    Point2 b = pts[(k + 1) % n];
                    Point2 intersect = Line2.intersect(line, lines[i]);

                    if (line.side(a) == 0 || line.side(b) == 0) {
                        if (line.side(a) == 0) {
                            ps.add(a);
                        }
                        continue;
                    }

                    if (intersect == null || !Point2.onSegment(a, b, intersect)) {
                        continue;
                    }
                    ps.add(intersect);
                }
                ps.sort(line.sortPointAlongLine());
                double longest = 0;
                Point2 last = ps.get(0);
                for (int k = 1; k < ps.size(); k++) {
                    Point2 prev = ps.get(k - 1);
                    Point2 cur = ps.get(k);
                    if (Point2.SORT_BY_XY.compare(prev, cur) == 0) {
                        continue;
                    }
                    Point2 center = Point2.div(Point2.plus(prev, cur), 2);
                    if (Point2.inPolygon(ptList, center) > 0) {
                        longest = Math.max(longest, Point2.dist2(last, cur));
                    } else {
                        last = cur;
                    }
                }

                ans = Math.max(ans, longest);
            }
        }

        out.println(Math.sqrt(ans));
    }
}
