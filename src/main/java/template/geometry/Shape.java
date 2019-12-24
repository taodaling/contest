package template.geometry;

public interface Shape {
    boolean contain(Point2D pt, boolean cover);

    double area();
}
