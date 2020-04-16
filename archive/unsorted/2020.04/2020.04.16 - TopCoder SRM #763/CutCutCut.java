package contest;

import template.geometry.geo2.IntegerPoint2;
import template.geometry.geo2.Point2;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class CutCutCut {
    public int[] findPieces(int[] X1, int[] Y1, int[] X2, int[] Y2, int Q) {
        TreeSet<Point2> points = new TreeSet<>(Point2.SORT_BY_XY);
        List<Segment> segments = new ArrayList<>();
        int edge = 4;
        points.add(new Point2(0, 0));
        points.add(new Point2(0, 1e4));
        points.add(new Point2(1e4, 0));
        points.add(new Point2(1e4, 1e4));
        segments.add(new Segment(0, 0, 0, 1e4));
        segments.add(new Segment(0, 0, 1e4, 0));
        segments.add(new Segment(1e4, 0, 1e4, 1e4));
        segments.add(new Segment(0, 1e4, 1e4, 1e4));

        int[] ans = new int[Q];
        for (int i = 0; i < Q; i++) {
            Segment seg = new Segment(X1[i], Y1[i], X2[i], Y2[i]);
            TreeSet<Point2> exists = new TreeSet<>(Point2.SORT_BY_XY);
            for (Segment segment : segments) {
                Point2 intersect = Segment.intersect(seg, segment);
                if (intersect == null) {
                    continue;
                }
                if (exists.contains(intersect)) {
                    continue;
                }
                exists.add(intersect);
                if (points.contains(intersect)) {
                } else {
                    points.add(intersect);
                    edge++;
                }
            }
            segments.add(seg);
            edge += exists.size() - 1;
            ans[i] = edge + 2 - points.size() - 1;
        }
        return ans;
    }
}

class Segment {
    Point2 a, b;

    public Segment(double x1, double y1, double x2, double y2) {
        a = new Point2(x1, y1);
        b = new Point2(x2, y2);
    }

    public static Point2 intersect(Segment a, Segment b) {
        return Point2.intersect(a.a, a.b, b.a, b.b);
    }
}
