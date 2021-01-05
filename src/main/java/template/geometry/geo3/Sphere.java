package template.geometry.geo3;

import java.util.List;

public class Sphere {
    public final Point3 o;
    public final double r;

    public Sphere(Point3 center, double r) {
        this.o = center;
        this.r = r;
    }

    public Point3 sph(double lat, double lon) {
        lat *= Math.PI / 180;
        lon *= Math.PI / 180;
        return new Point3(r * Math.cos(lat) * Math.cos(lon) + o.x, r * Math.cos(lat) * Math.sin(lon) + o.y, r * Math.sin(lat) + o.z);
    }

    public int sphereLine(Line3 l, List<Point3> out) {
        double h2 = r * r - l.squareDist(o);
        if (h2 < 0) return 0; // the line doesn’t touch the sphere
        Point3 p = l.project(o); // point P
        Point3 h = Point3.mul(l.d, Math.sqrt(h2) / l.d.abs()); // vector parallel to l, of length h
        out.add(Point3.minus(p, h));
        out.add(Point3.plus(p, h));
        return 1 + (h2 > 0 ? 1 : 0);
    }
}
