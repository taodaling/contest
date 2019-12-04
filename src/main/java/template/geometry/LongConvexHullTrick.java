package template.geometry;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Maintain lots of lines and support querying the max y overall lines inserted at some point x.
 */
public class LongConvexHullTrick implements Iterable<LongConvexHullTrick.Line> {
    public static class Line {
        // y = ax + b
        long a;
        long b;
        long lx;
        long rx;

        static Comparator<Line> orderByA = new Comparator<Line>() {
            @Override
            public int compare(Line o1, Line o2) {
                return Double.compare(o1.a, o2.a);
            }
        };
        static Comparator<Line> orderByLx = new Comparator<Line>() {
            @Override
            public int compare(Line o1, Line o2) {
                return Long.compare(o1.lx, o2.lx);
            }
        };

        public Line(long a, long b) {
            this.a = a;
            this.b = b;
        }

        public long y(long x) {
            return a * x + b;
        }

        //a1x+b1=a2x+b2=>(a1-a2)x=b2-b1=>x=(b2-b1)/(a1-a2)
        public static double intersectAt(Line a, Line b) {
            return (double)(b.b - a.b) / (a.a - b.a);
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
    }

    private TreeSet<Line> setOrderByA = new TreeSet(Line.orderByA);
    private TreeSet<Line> setOrderByLx = new TreeSet(Line.orderByLx);

    private Line queryLine = new Line(0, 0);

    public boolean isEmpty(){
        return setOrderByA.isEmpty();
    }

    public long query(long x) {
        queryLine.lx = x;
        Line line = setOrderByLx.floor(queryLine);
        return line.y(x);
    }

    public Line insert(long a, long b) {
        Line newLine = new Line(a, b);
        boolean add = true;
        while (add) {
            Line prev = setOrderByA.floor(newLine);
            if (prev == null) {
                newLine.lx = Long.MIN_VALUE;
                break;
            }
            if (prev.a == newLine.a) {
                if (prev.b >= newLine.b) {
                    add = false;
                    break;
                } else {
                    setOrderByA.remove(prev);
                    setOrderByLx.remove(prev);
                }
            } else {
                double lx = Line.intersectAt(prev, newLine);
                if (lx <= prev.lx) {
                    setOrderByA.remove(prev);
                    setOrderByLx.remove(prev);
                } else if (lx > prev.rx) {
                    add = false;
                    break;
                } else {
                    prev.rx = (long)Math.floor(lx);
                    newLine.lx = (long)Math.ceil(lx);
                    break;
                }
            }
        }

        while (add) {
            Line next = setOrderByA.ceiling(newLine);
            if (next == null) {
                newLine.rx = Long.MAX_VALUE;
                break;
            }
            double rx = Line.intersectAt(newLine, next);
            if (rx >= next.rx) {
                setOrderByA.remove(next);
                setOrderByLx.remove(next);
            } else if (rx < next.lx || (newLine.lx >= rx)) {
                Line lastLine = setOrderByA.floor(newLine);
                if (lastLine != null) {
                    lastLine.rx = next.lx;
                }
                add = false;
                break;
            } else {
                next.lx = (long)Math.floor(rx);
                newLine.rx = (long)Math.ceil(rx);
                break;
            }
        }

        if (add) {
            setOrderByA.add(newLine);
            setOrderByLx.add(newLine);
        }

        return newLine;
    }

    @Override
    public Iterator<Line> iterator() {
        return setOrderByA.iterator();
    }

    public static LongConvexHullTrick merge(LongConvexHullTrick a, LongConvexHullTrick b) {
        if (a.setOrderByA.size() > b.setOrderByA.size()) {
            LongConvexHullTrick tmp = a;
            a = b;
            b = tmp;
        }
        for (Line line : a) {
            b.insert(line.a, line.b);
        }
        return b;
    }

    public void clear(){
        setOrderByA.clear();
        setOrderByLx.clear();
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