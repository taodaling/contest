package on2020_01.on2020_01_09_Codeforces_Round__518__Div__1___Thanks__Mail_Ru__.D__Computer_Game;



import template.algo.LongBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Matrix;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class DComputerGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long t = in.readLong();
        Quest[] quests = new Quest[n];
        for (int i = 0; i < n; i++) {
            quests[i] = new Quest();
            quests[i].a = in.readInt();
            quests[i].b = in.readInt();
            quests[i].p = in.readDouble();
        }

        double E = 0;
        for (int i = 0; i < n; i++) {
            E = Math.max(E, quests[i].p * quests[i].b);
        }

        ConvexHullTrick cht = new ConvexHullTrick();
        for (Quest q : quests) {
            cht.insert(q.p, q.a * q.p, q);
        }

        long now = 0;
        double begin = 0;
        for (ConvexHullTrick.Line line : cht) {
            double rx = line.rx;
            BSResult result = bs(now, t, line.quest, E, begin, rx);
            begin = result.state;
            now = result.now;
            if (now == t) {
                break;
            }
        }

        out.printf("%.13f", begin);
    }

    public BSResult bs(long l, long r, Quest q, double E, double begin, double rx) {
        Matrix[] mats = new Matrix[35];
        Matrix x = new Matrix(new double[][]{
                {1 - q.p, E * q.p, q.a * q.p},
                {0, 1, 1},
                {0, 0, 1}
        });
        mats[0] = x;
        for (int i = 1; i < 35; i++) {
            mats[i] = Matrix.mul(mats[i - 1], mats[i - 1]);
        }

        Matrix next = new Matrix(3, 1);
        Matrix vector = new Matrix(new double[][]{
                {begin},
                {l},
                {1}
        });
        long now = l;
        for (int i = 35 - 1; i >= 0; i--) {
            if (now + (1L << i) > r) {
                continue;
            }
            Matrix.mul(mats[i], vector, next);
            if (E * (now + (1L << i)) - next.get(0, 0) < rx) {
                now += 1L << i;
                Matrix tmp = next;
                next = vector;
                vector = tmp;
            }
        }

        if (now < r && E * now - vector.get(0, 0) < rx) {
            now++;
            vector = Matrix.mul(mats[0], vector);
        }

        return new BSResult(vector.get(0, 0), now);
    }

    public double to(Quest q, double E, double begin, long now, long to) {
        if (to == now) {
            return begin;
        }
        Matrix matrix = new Matrix(new double[][]{
                {1 - q.p, E * q.p, q.a * q.p},
                {0, 1, 1},
                {0, 0, 1}
        });

        Matrix vector = new Matrix(new double[][]{
                {begin},
                {now},
                {1}
        });

        //double bf = bf(q.a, q.p, E, (int) t);
        Matrix transform = Matrix.pow(matrix, to - now);
        vector = Matrix.mul(transform, vector);
        return vector.get(0, 0);
    }
}

class BSResult {
    double state;
    long now;

    public BSResult(double state, long now) {
        this.state = state;
        this.now = now;
    }

    @Override
    public String toString() {
        return String.format("(%d, %f)", now, state);
    }
}

class Quest {
    int a;
    int b;
    double p;
}

class ConvexHullTrick implements Iterable<ConvexHullTrick.Line> {
    static final double INF = 1e50;

    public static class Line {
        // y = ax + b
        double a;
        double b;
        double lx;
        double rx;
        Quest quest;

        static Comparator<Line> orderByA = new Comparator<Line>() {
            @Override
            public int compare(Line o1, Line o2) {
                return Double.compare(o1.a, o2.a);
            }
        };
        static Comparator<Line> orderByLx = new Comparator<Line>() {
            @Override
            public int compare(Line o1, Line o2) {
                return Double.compare(o1.lx, o2.lx);
            }
        };

        public Line(double a, double b) {
            this.a = a;
            this.b = b;
        }

        public double y(double x) {
            return a * x + b;
        }

        //a1x+b1=a2x+b2=>(a1-a2)x=b2-b1=>x=(b2-b1)/(a1-a2)
        public static double intersectAt(Line a, Line b) {
            return (b.b - a.b) / (a.a - b.a);
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

    public boolean isEmpty() {
        return setOrderByA.isEmpty();
    }

    public double query(double x) {
        if (setOrderByLx.isEmpty()) {
            return -INF;
        }
        queryLine.lx = x;
        Line line = setOrderByLx.floor(queryLine);
        return line.y(x);
    }

    public Line insert(double a, double b, Quest quest) {
        Line newLine = new Line(a, b);
        newLine.quest = quest;
        boolean add = true;
        while (add) {
            Line prev = setOrderByA.floor(newLine);
            if (prev == null) {
                newLine.lx = -INF;
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
                    prev.rx = lx;
                    newLine.lx = lx;
                    break;
                }
            }
        }

        while (add) {
            Line next = setOrderByA.ceiling(newLine);
            if (next == null) {
                newLine.rx = INF;
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
                next.lx = rx;
                newLine.rx = rx;
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


    public void clear() {
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