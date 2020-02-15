package template.geometry;

import template.math.DigitUtils;

import java.util.*;

/**
 * Maintain lots of lines and support querying the max y overall lines inserted at some point x.
 */
public class LongConvexHullTrick implements Iterable<LongConvexHullTrick.Line> {
    static final long INF = (long) 2e18;

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
            return r < l;
        }
    }

    TreeSet<Line> setSortedByA = new TreeSet<>(Line.sortByA);
    TreeSet<Line> setSortedByL = new TreeSet<>(Line.sortByL);

    //y.a > x.a
    private long rightBoundOfXPrefX(Line x, Line y) {
        //x.a * r + x.b >= y.a * r + y.b
        //x.a * (r + 1) + x.b < y.a * (r + 1) + y.b
        //r * (y.a - x.a) <= x.b - y.b
        //r <= (x.b - y.b) / (y.a - x.a)
        return (x.b - y.b) / (y.a - x.a);
    }

    private long rightBoundOfXPrefY(Line x, Line y) {
        return DigitUtils.floorDiv(x.b - y.b - 1, y.a - x.a);
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

            long r = rightBoundOfXPrefX(floor, line);
            if (r > floor.r + 1) {
                return line;
            }
            if (r < floor.l) {
                removeLine(floor);
                continue;
            }
            floor.r = r;
            line.l = r + 1;
            break;
        }

        while (true) {
            Line ceil = setSortedByA.ceiling(line);
            if (ceil == null) {
                line.r = INF;
                break;
            }
            long r = rightBoundOfXPrefY(line, ceil);
            if (r < ceil.l - 1) {
                return line;
            }
            removeLine(ceil);
            if (r >= ceil.r) {
                continue;
            }
            ceil.l = r + 1;
            line.r = r;
            addLine(ceil);
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

    @Override
    public Iterator<Line> iterator() {
        return setSortedByA.iterator();
    }
}