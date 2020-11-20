package template.geometry.old;

import template.utils.GeometryUtils;

import java.util.ArrayList;
import java.util.List;

public class LinePolygon extends Polygon<Line2D> {
    public LinePolygon(List<Line2D> points) {
        super(points);
    }

    public PointPolygon asPoints() {
        int n = data.size();
        List<Point2D> deque = new ArrayList<>(n);
        deque.add(data.get(0).intersect(data.get(n - 1)));
        for (int i = 1; i < n; i++) {
            Point2D pt = data.get(i).intersect(data.get(i - 1));
            Point2D tail = deque.get(deque.size() - 1);
            if (GeometryUtils.valueOf(pt.x - tail.x) == 0 && GeometryUtils.valueOf(pt.y - tail.y) == 0) {
                continue;
            }
            deque.add(pt);
        }
        return new PointPolygon(deque);
    }
}
