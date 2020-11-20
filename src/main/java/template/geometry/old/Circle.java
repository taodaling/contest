package template.geometry.old;

import template.rand.Randomized;

import java.util.List;

public class Circle implements Shape {
    public final Point2D center;
    public final double radius;

    public Circle(Point2D center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public boolean contain(Point2D pt, boolean cover) {
        double dx = pt.x - center.x;
        double dy = pt.y - center.y;
        double dist2 = dx * dx + dy * dy - radius * radius;
        if (cover) {
            return dist2 <= 0;
        }
        return dist2 < 0;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    /**
     * O(r-l+1) algorithm solve min circle cover problem
     */
    public static Circle minCircleCover(Point2D[] pts, int l, int r) {
        Randomized.shuffle(pts, l, r + 1);
        Circle circle = new Circle(new Point2D(0, 0), 0);
        for (int i = l; i <= r; i++) {
            if (circle.contain(pts[i], true)) {
                continue;
            }
            circle = new Circle(pts[i], 0);
            for (int j = l; j < i; j++) {
                if (circle.contain(pts[j], true)) {
                    continue;
                }
                circle = getCircleByDiameter(pts[i], pts[j]);
                for (int k = l; k < j; k++) {
                    if (circle.contain(pts[k], true)) {
                        continue;
                    }
                    circle = new Triangle(pts[i], pts[j], pts[k]).outerCircle();
                }
            }
        }
        return circle;
    }



    public static Circle getCircleByDiameter(Point2D a, Point2D b) {
        return new Circle(new Point2D((a.x + b.x) / 2, (a.y + b.y) / 2), a.distanceBetween(b) / 2);
    }

    public static Circle minCircleCover(List<Point2D> pts) {
        return minCircleCover(pts.toArray(new Point2D[0]), 0, pts.size() - 1);
    }

    @Override
    public String toString() {
        return "Circle " + center + " with radius=" + radius;
    }
}
