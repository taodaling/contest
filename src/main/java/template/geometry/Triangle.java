package template.geometry;

public class Triangle implements Shape {
    Point2D a, b, c;
    double ab = -1;
    double ac = -1;
    double bc = -1;

    public double getAB() {
        if (ab == -1) {
            ab = a.distanceBetween(b);
        }
        return ab;
    }

    public double getAC() {
        if (ac == -1) {
            ac = a.distanceBetween(c);
        }
        return ac;
    }

    public double getBC() {
        if (bc == -1) {
            bc = b.distanceBetween(c);
        }
        return bc;
    }

    public Point2D getA() {
        return a;
    }

    public Point2D getB() {
        return b;
    }

    public Point2D getC() {
        return c;
    }

    public Triangle(Point2D a, Point2D b, Point2D c) {
        if (a.cross(b, c) < 0) {
            Point2D tmp = b;
            b = c;
            c = tmp;
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public boolean contain(Point2D pt, boolean cover) {
        if (cover) {
            return a.cross(b, pt) >= 0 &&
                    a.cross(c, pt) <= 0 &&
                    b.cross(c, pt) <= 0;
        }
        return a.cross(b, pt) > 0 &&
                a.cross(c, pt) < 0 &&
                b.cross(c, pt) < 0;
    }

    @Override
    public double area() {
        return a.cross(b, c) / 2;
    }

    public double triangleA() {
        double bc = getBC();
        double ac = getAC();
        double ab = getAB();
        return Math.acos((ac * ac + ab * ab - bc * bc) / (2 * ac * ab));
    }

    public double triangleB() {
        double bc = getBC();
        double ac = getAC();
        double ab = getAB();
        return Math.acos((-ac * ac + ab * ab + bc * bc) / (2 * bc * ab));
    }

    public double triangleC() {
        double bc = getBC();
        double ac = getAC();
        double ab = getAB();
        return Math.acos((ac * ac - ab * ab + bc * bc) / (2 * bc * ac));
    }

    public Circle innerCircle() {
        double bc = getBC();
        double ac = getAC();
        double ab = getAB();
        double sum = bc + ac + ab;
        double radius = 2 * area() / sum;
        Point2D center = new Point2D((bc * a.x + ac * b.x + ab * c.x) / sum,
                (bc * a.y + ac * b.y + ab * c.y) / sum);
        return new Circle(center, radius);
    }

    public Circle outerCircle() {
        Segment2D ab = new Segment2D(a, b);
        Segment2D ac = new Segment2D(a, c);
        Line2D abPB = ab.getPerpendicularBisector();
        Line2D acPB = ac.getPerpendicularBisector();
        Point2D center = abPB.intersect(acPB);
        return new Circle(center, center.distanceBetween(a));
    }
}
