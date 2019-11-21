package template.geometry;

import template.utils.GeometryUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;

public class HalfPlaneIntersection {
    private LineConvexHull convex;
    private boolean hasSolution = true;

    public LineConvexHull getConvex() {
        return convex;
    }

    public boolean isHasSolution() {
        return hasSolution;
    }

    public HalfPlaneIntersection(Polygon<Line2D> linePolygon, boolean close) {
        this(linePolygon, close, false);
    }

    public HalfPlaneIntersection(Polygon<Line2D> linePolygon, boolean close, boolean isAnticlockwise) {
        Line2D[] lines = linePolygon.data.toArray(new Line2D[linePolygon.data.size()]);
        if (!isAnticlockwise) {
            Arrays.sort(lines, Line2D.SORT_BY_ANGLE);
        }
        int n = lines.length;


        Deque<Line2D> deque = new ArrayDeque(n);
        for (int i = 0; i < n; i++) {
            Line2D line = lines[i];
            while (i + 1 < n && GeometryUtils.valueOf(line.theta - lines[i + 1].theta) == 0) {
                i++;
                if (line.whichSideIs(lines[i].b) == 1) {
                    line = lines[i];
                }
            }
            insert(deque, line, close);
        }
        insert(deque, deque.removeFirst(), close);

        // reinsert head
        if (!hasSolution) {
            return;
        }
        convex = new LineConvexHull(new ArrayList(deque));
    }

    private void insert(Deque<Line2D> deque, Line2D line, boolean close) {
        if (!hasSolution) {
            return;
        }
        while (deque.size() >= 2) {
            Line2D tail = deque.removeLast();
            Point2D pt = tail.intersect(deque.peekLast());
            if (pt == null) {
                continue;
            }
            int side = line.whichSideIs(pt);
            if (side > 0 || (close && side == 0)) {
                deque.add(tail);
                break;
            }
            if (line.onWhichSide(deque.peekLast()) != tail.onWhichSide(deque.peekLast())) {
                hasSolution = false;
            }

        }
        if (deque.size() == 1 && line.onWhichSide(deque.peekLast()) == 0) {
            int side = deque.peekLast().whichSideIs(line.b);
            if (!(side > 0 || (close && side == 0))) {
                hasSolution = false;
            }
        }

        deque.addLast(line);
    }
}
