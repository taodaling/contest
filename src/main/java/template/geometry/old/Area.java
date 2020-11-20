package template.geometry.old;

import template.utils.GeometryUtils;

public class Area {
    public double areaOfRect(Line2D a, Line2D b) {
        return Math.abs(GeometryUtils.cross(a.d.x, a.d.y, b.d.x, b.d.y));
    }

    public double areaOfTriangle(Line2D a, Line2D b) {
        return areaOfRect(a, b) / 2;
    }

    public double areaOfTriangle(Point2D a, Point2D b, Point2D c) {
        return Math.abs(a.cross(b, c)) / 2;
    }

}
