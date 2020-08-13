package template.geometry.geo2;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;

public class HalfPlaneIntersection2 {
    public static Collection<Line2> halfPlaneIntersection(Line2[] lines, boolean close) {
        return halfPlaneIntersection(lines, close, false);
    }

    public static Collection<Line2> halfPlaneIntersection(Line2[] lines, boolean close, boolean isAnticlockwise) {
        if (lines.length < 2) {
            return null;
        }
        if (!isAnticlockwise) {
            Arrays.sort(lines, (a, b) -> Point2.SORT_BY_POLAR_ANGLE.compare(a.vec, b.vec));
        }
        int n = lines.length;

        Deque<Line2> deque = new ArrayDeque<>(n);
        for (int i = 0; i < n; i++) {
            Line2 line = lines[i];
            while (i + 1 < n && Point2.SORT_BY_POLAR_ANGLE.compare(line.vec, lines[i + 1].vec) == 0) {
                i++;
                if (line.side(lines[i]) == 1) {
                    line = lines[i];
                }
            }
            if (!insert(deque, line, close)) {
                return null;
            }
        }
        if (!insert(deque, deque.removeFirst(), close)) {
            return null;
        }

        // reinsert head
        return deque;
    }

    private static boolean insert(Deque<Line2> deque, Line2 line, boolean close) {
        while (deque.size() >= 2) {
            Line2 tail = deque.removeLast();
            Point2 pt = Line2.intersect(tail, deque.peekLast());
            if (pt == null) {
                continue;
            }
            int side = line.side(pt);
            if (side > 0 || (close && side == 0)) {
                deque.addLast(tail);
                break;
            }
            if (Point2.orient(line.vec, deque.peekLast().vec) != Point2.orient(tail.vec, deque.peekLast().vec)) {
                return false;
            }
        }
        if (deque.size() == 1 && Point2.orient(line.vec, deque.peekLast().vec) == 0) {
            int side = deque.peekLast().side(line);
            if (!(side > 0 || (close && side == 0))) {
                return false;
            }
        }

        deque.addLast(line);
        return true;
    }
}
