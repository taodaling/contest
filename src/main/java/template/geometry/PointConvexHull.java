package template.geometry;

import template.utils.GeometryUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

public class PointConvexHull extends PointPolygon {
    PointConvexHull(List<Point2D> points) {
        super(points);
    }

    public LineConvexHull asLines() {
        int n = data.size();
        Line2D[] lines = new Line2D[n];
        lines[0] = new Line2D(data.get(n - 1), data.get(0));
        for (int i = 1; i < n; i++) {
            lines[i] = new Line2D(data.get(i - 1), data.get(i));
        }
        return new LineConvexHull(Arrays.asList(lines));
    }

    public Segment2D theFarthestPointPairBruteForce() {
        Point2D x = data.get(0);
        Point2D y = data.get(0);
        double farthestDist = 0;
        int n = data.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Point2D a = data.get(i);
                Point2D b = data.get(j);
                double d = a.distance2Between(b);
                if (d > farthestDist) {
                    farthestDist = d;
                    x = a;
                    y = b;
                }
            }
        }
        return new Segment2D(x, y);
    }

    public double area() {
        Area areaHelper = new Area();
        double area = 0;
        int n = data.size();
        for (int i = 2; i < n; i++) {
            area += areaHelper.areaOfTriangle(data.get(0), data.get(i), data.get(i - 1));
        }
        return area;
    }

    public Segment2D theFarthestPointPair() {
        // 旋转卡壳
        int n = data.size();

        if (n <= 2) {
            return new Segment2D(pointAtLoop(0), pointAtLoop(1));
        }

        Point2D[] ab = new Point2D[2];
        Point2D[] cd = new Point2D[] {pointAtLoop(1), pointAtLoop(2)};

        Point2D x = cd[0];
        Point2D y = cd[1];
        double farthestDist2 = 0;
        for (int i = 0, j = 1; i < n; i++) {
            ab[0] = pointAtLoop(i);
            ab[1] = pointAtLoop(i + 1);
            while (Point2D.cross(ab[0], ab[1], cd[0], cd[1]) >= 0
                            && Point2D.cross(ab[0], ab[1], cd[1], pointAtLoop(j + 2)) >= 0) {
                j++;
                cd[0] = cd[1];
                cd[1] = pointAtLoop(j + 1);
            }
            for (int k = 0; k < 2; k++) {
                for (int t = 0; t < 2; t++) {
                    double dist2 = ab[k].distance2Between(cd[t]);
                    if (farthestDist2 >= dist2) {
                        continue;
                    }
                    x = ab[k];
                    y = cd[t];
                    farthestDist2 = dist2;
                }
            }
        }

        return new Segment2D(x, y);
    }

    private Point2D pointAtLoop(int i) {
        return data.get(i % data.size());
    }

    public static PointConvexHull grahamScan(PointPolygon pointPolygon) {
        final Point2D[] points = pointPolygon.data.toArray(new Point2D[0]);
        int n = points.length;
        for (int i = 1; i < n; i++) {
            int cmp = points[i].y != points[0].y ? Double.compare(points[i].y, points[0].y)
                    : Double.compare(points[i].x, points[0].x);
            if (cmp >= 0) {
                continue;
            }
            Point2D tmp = points[0];
            points[0] = points[i];
            points[i] = tmp;
        }


        Comparator<Point2D> cmp = new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                return GeometryUtils.signOf(GeometryUtils.valueOf(-points[0].cross(o1, o2)));
            }
        };
        Arrays.sort(points, 1, n, cmp);

        int shrinkSize = 2;
        for (int i = 2; i < n; i++) {
            if (cmp.compare(points[i], points[shrinkSize - 1]) == 0) {
                if (points[i].distance2Between(points[0]) > points[shrinkSize - 1].distance2Between(points[0])) {
                    points[shrinkSize - 1] = points[i];
                }
            } else {
                points[shrinkSize++] = points[i];
            }
        }

        n = shrinkSize;
        Deque<Point2D> stack = new ArrayDeque(n);
        stack.addLast(points[0]);
        for (int i = 1; i < n; i++) {
            while (stack.size() >= 2) {
                Point2D last = stack.removeLast();
                Point2D second = stack.peekLast();
                if (GeometryUtils.valueOf(second.cross(points[i], last)) < 0) {
                    stack.addLast(last);
                    break;
                }
            }
            stack.addLast(points[i]);
        }

        return new PointConvexHull(new ArrayList(stack));
    }

}


