package template.geometry.geo2;

import template.primitve.generated.datastructure.DoubleIntervalMap;
import template.utils.GeometryUtils;

import java.util.function.IntBinaryOperator;

/**
 * find coverage on circle, each operation take O(\log_2 n)
 */
public class CircleCoverDetect {
    private DoubleIntervalMap dm = new DoubleIntervalMap();
    private Point2 center;
    private double cr;
    private double eps;
    private static double min = 0;
    private static double max = 2 * Math.PI;

    public CircleCoverDetect(double eps) {
        this.eps = eps;
    }

    public void reset(Point2 center, double r) {
        this.center = center;
        this.cr = r;
        dm.clear();
    }


    private double pow2(double x) {
        return x * x;
    }

    private double dist2(double x, double y) {
        return pow2(x) + pow2(y);
    }

    private double dist(double x, double y) {
        return Math.sqrt(dist2(x, y));
    }

    private void addForbidden(double theta, double angle) {
        double l = theta - angle;
        double r = theta + angle;

        if (l < min) {
            dm.add(max + l, max);
            l = 0;
        }
        if (r > max) {
            dm.add(0, r - max);
            r = max;
        }
        dm.add(l, r);
    }

    public void addCircle(Point2 pt, double r) {
        double dist = Point2.dist(center, pt);
        if (dist + eps >= r + cr) {
            return;
        }
        //no intersection case
        if (dist + r + eps <= cr) {
            return;
        }
        if (dist + cr + eps <= r) {
            dm.add(0, Math.PI * 2);
            return;
        }

        double theta = GeometryUtils.theta(pt.y - center.y, pt.x - center.x);
        double angle = GeometryUtils.triangleAngle(r, cr, dist);
        addForbidden(theta, angle);
    }

    private double valueOfRadian(double x) {
        while (x > max) {
            x -= Math.PI * 2;
        }
        while (x < min) {
            x += Math.PI * 2;
        }
        return x;
    }

    /**
     * remove clock wise area
     *
     * @param line
     */
    public void addLine(Line2 line) {
        int side = line.side(center);
        double dist = line.dist(center);
        if (dist + eps >= cr) {
            if (side == -1) {
                dm.add(min, max);
            }
            return;
        }
        double theta = valueOfRadian(GeometryUtils.theta(line.vec.y, line.vec.x) - Math.PI / 2);
        double angle = side == 0 ? Math.PI / 2 : Math.acos(dist / cr);
        if (side == -1) {
            angle = Math.PI - angle;
        }
        addForbidden(theta, angle);
    }

    public double findAnyCoveredRadian() {
        if (dm.isEmpty()) {
            return -1;
        }
        return dm.first().l;
    }

    public double findAnyUncoverRadian() {
        if (dm.isEmpty()) {
            return 0;
        }
        DoubleIntervalMap.Interval first = dm.first();
        if (first.l == min && first.r == max) {
            return -1;
        }
        return first.l == 0 ? first.r : first.l;
    }

    public Point2 findPointOnCircleByRadian(double radian) {
        if (radian < 0) {
            return null;
        }
        Point2 cur = Point2.translate(center, new Point2(cr, 0));
        Point2 rotated = Point2.rotate(center, cur, radian);
        return rotated;
    }

    @Override
    public String toString() {
        return dm.toString();
    }
}
