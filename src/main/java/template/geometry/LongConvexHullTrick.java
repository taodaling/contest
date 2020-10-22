package template.geometry;

import template.math.DigitUtils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Maintain lots of lines and support querying the max y overall lines inserted at some point x.
 */
public class LongConvexHullTrick implements Iterable<LongConvexHullTrick.Line> {
    static final long INF = Long.MAX_VALUE / 2;

    public static class Line {
        long a;
        long b;
        long l;
        long r;

        public Line(long a, long b) {
            this.a = a;
            this.b = b;
        }

        public long y(long x) {
            return a * x + b;
        }

        static Comparator<Line> sortByA = (x, y) -> Long.compare(x.a, y.a);
        static Comparator<Line> sortByL = (x, y) -> Long.compare(x.l, y.l);

        public boolean isEmpty() {
            return r <= l;
        }
    }

    TreeSet<Line> setSortedByA = new TreeSet<>(Line.sortByA);
    TreeSet<Line> setSortedByL = new TreeSet<>(Line.sortByL);

    //y.a > x.a
    private long intersect(Line x, Line y) {
        assert y.a > x.a;
        return DigitUtils.ceilDiv(x.b - y.b, y.a - x.a);
    }


    private void removeLine(Line line) {
        setSortedByA.remove(line);
        setSortedByL.remove(line);
    }

    private void addLine(Line line) {
        setSortedByA.add(line);
        setSortedByL.add(line);
    }

    private Line qBody = new Line(0, 0);

    public Line queryLine(long x) {
        qBody.l = x;
        return setSortedByL.floor(qBody);
    }

    public long query(long x) {
        return queryLine(x).y(x);
    }

    public Line insert(long a, long b) {
        Line line = new Line(a, b);

        while (true) {
            Line floor = setSortedByA.floor(line);
            if (floor == null) {
                line.l = -INF;
                break;
            }
            if (floor.a == line.a) {
                if (floor.b >= line.b) {
                    return line;
                } else {
                    removeLine(floor);
                    continue;
                }
            }

            long r = intersect(floor, line);
            if (r <= floor.l) {
                removeLine(floor);
                continue;
            }

            floor.r = Math.min(floor.r, r);
            line.l = r;
            break;
        }

        while (true) {
            Line ceil = setSortedByA.ceiling(line);
            if (ceil == null) {
                line.r = INF;
                break;
            }
            long r = intersect(line, ceil);
            if (r >= ceil.r) {
                removeLine(ceil);
                continue;
            }
            ceil.l = Math.max(ceil.l, r);
            line.r = r;
            break;
        }

        if (!line.isEmpty()) {
            addLine(line);
        }
        return line;
    }

    public void clear() {
        setSortedByL.clear();
        setSortedByA.clear();
    }

    public static LongConvexHullTrick merge(LongConvexHullTrick a, LongConvexHullTrick b) {
        if (a.setSortedByA.size() > b.setSortedByA.size()) {
            LongConvexHullTrick tmp = a;
            a = b;
            b = tmp;
        }
        for (Line line : a) {
            b.insert(line.a, line.b);
        }
        return b;
    }

    public boolean isEmpty() {
        return setSortedByA.isEmpty();
    }

    @Override
    public Iterator<Line> iterator() {
        return setSortedByA.iterator();
    }
}