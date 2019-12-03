package template.geometry;

import template.utils.GeometryUtils;

import java.util.Map;
import java.util.TreeMap;

public class DynamicConvexHull {
    private TreeMap<Double, Point2D> pts = new TreeMap<>((a, b) -> GeometryUtils.near(a, b) ? 0 : a.compareTo(b));
    private Point2D center;

    public DynamicConvexHull(Point2D center) {
        this.center = center;
    }

    private Map.Entry<Double, Point2D> clockwise(Double theta) {
        Map.Entry<Double, Point2D> floor = pts.floorEntry(theta);
        if (floor == null) {
            floor = pts.lastEntry();
        }
        return floor;
    }

    private Map.Entry<Double, Point2D> countclockwise(Double theta) {
        Map.Entry<Double, Point2D> ceil = pts.ceilingEntry(theta);
        if (ceil == null) {
            ceil = pts.firstEntry();
        }
        return ceil;
    }

    private boolean contain(Point2D point, Double theta, boolean close) {
        Point2D cw = clockwise(theta).getValue();
        Point2D ccw = countclockwise(theta).getValue();

        if (cw == ccw) {
            Segment2D seg = new Segment2D(center, ccw);
            if (close) {
                return seg.contain(point);
            }
            return seg.containWithoutEndpoint(point);
        }

        if (close) {
            return center.cross(cw, point) >= 0 && cw.cross(ccw, point) >= 0 && ccw.cross(center, point) >= 0;
        }
        return center.cross(cw, point) > 0 && cw.cross(ccw, point) > 0 && ccw.cross(center, point) > 0;
    }

    public boolean contain(Point2D point, boolean close) {
        if (pts.isEmpty()) {
            return false;
        }
        return contain(point, GeometryUtils.theta(point.y - center.y, point.x - center.x), close);
    }

    public void add(Point2D point) {
        Double theta = GeometryUtils.theta(point.y - center.y, point.x - center.x);
        if (pts.size() < 3) {
            Point2D exists = pts.get(theta);
            if (exists != null && center.distance2Between(exists) >= center.distance2Between(point)) {
                return;
            }
            pts.put(theta, point);
            return;
        }

        if (contain(point, theta, true)) {
            return;
        }

        // clockwise
        while (pts.size() >= 3) {
            Map.Entry<Double, Point2D> cw = clockwise(theta);
            pts.remove(cw.getKey());
            Point2D next = clockwise(theta).getValue();
            if (point.cross(cw.getValue(), next) < 0) {
                pts.put(cw.getKey(), cw.getValue());
                break;
            }
        }
        // counterclockwise
        while (pts.size() >= 3) {
            Map.Entry<Double, Point2D> ccw = countclockwise(theta);
            pts.remove(ccw.getKey());
            Point2D next = countclockwise(theta).getValue();
            if (point.cross(ccw.getValue(), next) > 0) {
                pts.put(ccw.getKey(), ccw.getValue());
                break;
            }
        }

        pts.put(theta, point);
    }
}
