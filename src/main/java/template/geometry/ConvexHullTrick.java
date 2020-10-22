package template.geometry;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Maintain lots of lines and support querying the max y overall lines inserted at some point x.
 */
public class ConvexHullTrick implements Iterable<ConvexHullTrick.Line> {
    static final double INF = 1e50;

    public static class Line {
        // y = ax + b
        double a;
        double b;
        double l;
        double r;

        static Comparator<Line> orderByA = new Comparator<Line>() {
            @Override
            public int compare(Line o1, Line o2) {
                return Double.compare(o1.a, o2.a);
            }
        };
        static Comparator<Line> orderByLx = new Comparator<Line>() {
            @Override
            public int compare(Line o1, Line o2) {
                return Double.compare(o1.l, o2.l);
            }
        };

        public Line(double a, double b) {
            this.a = a;
            this.b = b;
        }

        public double y(double x) {
            return a * x + b;
        }

        @Override
        public int hashCode() {
            return (int) (Double.doubleToLongBits(a) * 31 + Double.doubleToLongBits(b));
        }

        @Override
        public boolean equals(Object obj) {
            Line line = (Line) obj;
            return a == line.a && b == line.b;
        }

        @Override
        public String toString() {
            return a + "x+" + b;
        }

        public boolean isEmpty() {
            return r <= l;
        }
    }

    private TreeSet<Line> setSortedByA = new TreeSet<>(Line.orderByA);
    private TreeSet<Line> setSortedByL = new TreeSet<>(Line.orderByLx);

    private Line queryLine = new Line(0, 0);

    public boolean isEmpty() {
        return setSortedByA.isEmpty();
    }

    public double query(double x) {
        if (setSortedByL.isEmpty()) {
            return -INF;
        }
        queryLine.l = x;
        Line line = setSortedByL.floor(queryLine);
        return line.y(x);
    }

    //y.a > x.a
    private double intersect(Line x, Line y) {
        return (x.b - y.b) / (y.a - x.a);
    }

    private void removeLine(Line line) {
        setSortedByA.remove(line);
        setSortedByL.remove(line);
    }

    private void addLine(Line line) {
        setSortedByA.add(line);
        setSortedByL.add(line);
    }

    public Line insert(double a, double b) {
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

            double r = intersect(floor, line);
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
            double r = intersect(line, ceil);
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

    @Override
    public Iterator<Line> iterator() {
        return setSortedByA.iterator();
    }

    public static ConvexHullTrick merge(ConvexHullTrick a, ConvexHullTrick b) {
        if (a.setSortedByA.size() > b.setSortedByA.size()) {
            ConvexHullTrick tmp = a;
            a = b;
            b = tmp;
        }
        for (Line line : a) {
            b.insert(line.a, line.b);
        }
        return b;
    }

    public void clear() {
        setSortedByA.clear();
        setSortedByL.clear();
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Line line : this) {
            builder.append(line).append('\n');
        }
        return builder.toString();
    }
}