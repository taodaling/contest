package template.geometry;

import template.utils.GeometryUtils;

import java.util.ArrayList;
import java.util.List;

public class LineConvexHull extends LinePolygon {
    LineConvexHull(List<Line2D> points) {
        super(points);
    }

    public boolean inside(Point2D pt, boolean includeOnLinePoint) {
        for (Line2D line : data) {
            int side = line.whichSideIs(pt);
            if (side == -1) {
                return false;
            }
            if (side == 0 && !includeOnLinePoint) {
                return false;
            }
        }
        return true;
    }

    public PointConvexHull asPoints() {
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
        return new PointConvexHull(deque);
    }
}
