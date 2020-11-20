package template.geometry.old;

import template.utils.GeometryUtils;

public class Segment2D extends Line2D {
    public Segment2D(Point2D a, Point2D b) {
        super(a, b);
    }

    public Line2D getPerpendicularBisector() {
        Point2D center = new Point2D((a.x + b.x) / 2, (a.y + b.y) / 2);
        Point2D apeak = d.getApeak();
        return new Line2D(center, center.add(apeak));
    }

    /**
     * 判断p是否落在线段section上
     */
    public boolean contain(Point2D p) {
        return GeometryUtils.cross(p.x - a.x, p.y - a.y, d.x, d.y) == 0
                && GeometryUtils.valueOf(p.x - Math.min(a.x, b.x)) >= 0
                && GeometryUtils.valueOf(p.x - Math.max(a.x, b.x)) <= 0
                && GeometryUtils.valueOf(p.y - Math.min(a.y, b.y)) >= 0
                && GeometryUtils.valueOf(p.y - Math.max(a.y, b.y)) <= 0;
    }

    public boolean containWithoutEndpoint(Point2D p) {
        return contain(p) && !p.equals(a) && !p.equals(b);
    }

    /**
     * 获取与线段的交点，null表示无交点或有多个交点
     */
    public Point2D intersect(Segment2D another) {
        Point2D point = super.intersect(another);
        return point != null && contain(point) && another.contain(point) ? point : null;
    }

    public double length2() {
        return a.distance2Between(b);
    }

    public double length() {
        return a.distanceBetween(b);
    }
}
