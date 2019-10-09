package template;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

public class Geometry {
    public static class Point2 {
        final double x, y;

        public Point2(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Point2 add(Point2 other) {
            return new Point2(x + other.x, y + other.y);
        }

        public Point2 sub(Point2 other) {
            return new Point2(x - other.x, y - other.y);
        }

        @Override
        public String toString() {
            return String.format("(%f,%f)", x, y);
        }
    }

    public static class Line2 {
        final Point2 begin, end;

        public Line2(Point2 begin, Point2 end) {
            this.begin = begin;
            this.end = end;
        }

        public Point2 getVector() {
            return end.sub(begin);
        }

        public Point2 getPointAt(double alpha) {
            return new Point2((1 - alpha) * begin.x + alpha * end.x, (1 - alpha) * begin.y + alpha * end.y);
        }

        @Override
        public String toString() {
            return String.format("%s->%s", begin.toString(), end.toString());
        }
    }

    public static int compare(double a, double b, double prec) {
        return Math.abs(a - b) < prec ? 0 : a < b ? -1 : 1;
    }

    /**
     * 从point2s点集中获取逆时针序构成凸包的外部点集
     */
    public static List<Point2> grahamScan(List<Point2> point2s, final double prec) {
        final Point2[] points = point2s.toArray(new Point2[0]);
        int n = points.length;

        Memory.swap(points, 0, Memory.min(points, 0, n, new Comparator<Point2>() {
            @Override
            public int compare(Point2 o1, Point2 o2) {
                return o1.y != o2.y ? Double.compare(o1.y, o2.y) : Double.compare(o1.x, o2.x);
            }
        }));

        Comparator<Point2> cmp = new Comparator<Point2>() {
            @Override
            public int compare(Point2 o1, Point2 o2) {
                return Geometry.compare(0, cross(o1.sub(points[0]), o2.sub(points[0])), prec);
            }
        };
        Arrays.sort(points, 1, n, cmp);

        int shrinkSize = 2;
        for (int i = 2; i < n; i++) {
            if (cmp.compare(points[i], points[shrinkSize - 1]) == 0) {
                if (distance2(points[i].sub(points[0])) > distance2(points[shrinkSize - 1].sub(points[0]))) {
                    points[shrinkSize - 1] = points[i];
                }
            } else {
                points[shrinkSize++] = points[i];
            }
        }

        n = shrinkSize;
        Deque<Point2> stack = new ArrayDeque(n);
        stack.addLast(points[0]);
        for (int i = 1; i < n; i++) {
            while (stack.size() >= 2) {
                Point2 last = stack.removeLast();
                Point2 second = stack.peekLast();
                if (cross(points[i].sub(second), last.sub(second)) < -prec) {
                    stack.addLast(last);
                    break;
                }
            }
            stack.addLast(points[i]);
        }

        return new ArrayList(stack);
    }

    /**
     * Get (x1, y1)·(x2, y2)
     */
    public static long cross(int x1, int y1, int x2, int y2) {
        return (long) x1 * y2 - (long) y1 * x2;
    }

    /**
     * Get (x1, y1)·(x2, y2)
     */
    public static double cross(double x1, double y1, double x2, double y2) {
        return x1 * y2 - y1 * x2;
    }

    /**
     * Get (x1, y1)·(x2, y2)
     */
    public static double cross(Point2 a, Point2 b) {
        return a.x * b.y - a.y * b.x;
    }

    /**
     * 判断p是否落在线段section上
     */
    public static boolean onSection(Point2 p, Line2 section, double precision) {
        return Math.abs(cross(p.sub(section.begin), section.getVector())) < precision
                && p.x >= Math.min(section.begin.x, section.end.x) - precision && p.x <= Math.max(section.begin.x, section.end.x) + precision;
    }

    /**
     * 求p向量长度的平方
     */
    public static double distance2(Point2 p) {
        return p.x * p.x + p.y * p.y;
    }

    /**
     * 求p向量长度
     */
    public static double distance(Point2 p) {
        return Math.sqrt(distance2(p));
    }

    /**
     * 如果直线s1与s2有交点，则返回交点，若平行，则返回null。
     */
    public static Point2 lineIntersection(Line2 s1, Line2 s2, double prec) {
        double m11 = s1.end.x - s1.begin.x;
        double m01 = s2.end.x - s2.begin.x;
        double m10 = s1.begin.y - s1.end.y;
        double m00 = s2.begin.y - s2.end.y;

        double div = m00 * m11 - m01 * m10;
        if (Math.abs(div) < prec) {
            return null;
        }

        double v0 = (s2.begin.x - s1.begin.x) / div;
        double v1 = (s2.begin.y - s1.begin.y) / div;

        double alpha = m00 * v0 + m01 * v1;

        return s1.getPointAt(alpha);
    }

    /**
     * 如果线段s1与s2有交点，则返回交点，否则返回null。
     */
    public static Point2 sectionIntersection(Line2 s1, Line2 s2, double prec) {
        double m11 = s1.end.x - s1.begin.x;
        double m01 = s2.end.x - s2.begin.x;
        double m10 = s1.begin.y - s1.end.y;
        double m00 = s2.begin.y - s2.end.y;

        double div = m00 * m11 - m01 * m10;
        if (Math.abs(div) < prec) {
            return null;
        }

        double v0 = (s2.begin.x - s1.begin.x) / div;
        double v1 = (s2.begin.y - s1.begin.y) / div;

        double alpha = m00 * v0 + m01 * v1;
        double beta = m10 * v0 + m11 * v1;

        if (-prec <= alpha && alpha <= 1 + prec && -prec <= beta && beta <= 1 + prec) {
            return s1.getPointAt(alpha);
        }
        return null;
    }
}