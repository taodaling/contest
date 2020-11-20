package template.geometry.geo2;

import template.geometry.old.GeoConstant;
import template.primitve.generated.utils.DoubleBinaryConsumer;

/**
 * find coverage on circle, each operation take O(\log_2 n)
 */
public class CircleCoverDetect {

    private Point2 center;
    private double cr;
    private double eps;
    private static double max = 2 * Math.PI;
    private DoubleBinaryConsumer consumer = NIL_CONSUMER;
    private static DoubleBinaryConsumer NIL_CONSUMER = (l, r) -> {
    };

    public CircleCoverDetect(double eps) {
        this.eps = eps;
    }

    /**
     * all interval [l, r] coverage information will invoke consumer.accept(l, r)
     * to notify invoker
     */
    public void reset(Point2 center, double r) {
        this.center = center;
        this.cr = r;
    }

    public void setConsumer(DoubleBinaryConsumer consumer) {
        this.consumer = consumer == null ? NIL_CONSUMER : consumer;
    }

    private void add(double l, double r) {
        consumer.accept(l, r);
    }

    private void addForbidden(double theta, double angle) {
        double l = theta - angle;
        double r = theta + angle;

        if (l < 0) {
            add(max + l, max);
            l = 0;
        }
        if (r > max) {
            add(0, r - max);
            r = max;
        }
        add(l, r);
    }

    /**
     * ignore the same circle as (center, cr)
     */
    public void addCircle(Point2 pt, double r) {
        double dist = Point2.dist(center, pt);
        if (dist + eps >= r + cr) {
            return;
        }
        //no intersection case
        if (dist - eps <= cr - r) {
            return;
        }
        if (dist - eps <= r - cr) {
            add(0, Math.PI * 2);
            return;
        }

        double theta = GeoConstant.theta(pt.x - center.x, pt.y - center.y);
        double angle = GeoConstant.triangleAngle(r, cr, dist);
        addForbidden(theta, angle);
    }

    private double valueOfRadian(double x) {
        while (x >= max) {
            x -= max;
        }
        while (x < 0) {
            x += max;
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
                add(0, max);
            }
            return;
        }
        double theta = valueOfRadian(GeoConstant.theta(line.vec.x, line.vec.y) - Math.PI / 2);
        double angle = side == 0 ? Math.PI / 2 : Math.acos(dist / cr);
        if (side == -1) {
            angle = Math.PI - angle;
            theta = valueOfRadian(-theta);
        }
        addForbidden(theta, angle);
    }

    public Point2 findPointOnCircleByRadian(double radian) {
        if (radian < 0) {
            return null;
        }
        Point2 cur = Point2.translate(center, new Point2(cr, 0));
        Point2 rotated = Point2.rotate(center, cur, radian);
        return rotated;
    }
}
