package template.datastructure;

import template.utils.Buffer;

import java.util.Comparator;

/**
 * maintain line like f(x, z) = (x + b) / (z + c) while (x + b) > 0 and (z + c) > 0 always satisfied
 * and query for max point
 */
public class ConvexHullTrickExt {
    public static class Line {
        //x
        double b;
        //z
        double c;
        //[l, r)
        double l;
        double r;

        public Line(double b, double c, double l, double r) {
            this.b = b;
            this.c = c;
            this.l = l;
            this.r = r;
        }


        double apply(double x, double z) {
            return (b + x) / (c + z);
        }
    }


    TreapT<Line> root = TreapT.NIL;
    static Comparator<Line> comp = Comparator.comparingDouble(x -> -x.c);
    static Comparator<Line> comp2 = (a, b) -> {
        double res = a.apply(b.b, b.c);
        return res >= a.r ? -1 : res < a.l ? 1 : 0;
    };
    static Buffer<TreapT<Line>> buffer = new Buffer<>(TreapT::new);

    private Line searchLine(Line l) {
        TreapT<Line> node = root;
        while (node != TreapT.NIL) {
            int res = comp2.compare(node.key, l);
            if (res == 0) {
                return node.key;
            } else if (res < 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return null;
    }

    private Line popCeil(Line l, Comparator<Line> comp) {
        TreapT<Line>[] p0 = TreapT.splitByKey(root, l, comp, false);
        if (p0[1].size == 0) {
            return null;
        }
        TreapT<Line>[] p1 = TreapT.splitByRank(p0[1], 1);
        p0[1] = p1[1];
        root = TreapT.merge(p0[0], p0[1]);
        buffer.release(p1[0]);
        return p1[0].key;
    }

    private Line popFloor(Line l, Comparator<Line> comp) {
        TreapT<Line>[] p0 = TreapT.splitByKey(root, l, comp, true);
        if (p0[0].size == 0) {
            return null;
        }
        TreapT<Line>[] p1 = TreapT.splitByRank(p0[0], p0[0].size - 1);
        p0[0] = p1[0];
        root = TreapT.merge(p0[0], p0[1]);
        buffer.release(p1[1]);
        return p1[1].key;
    }

    private void add0(Line l) {
        TreapT<Line>[] p0 = TreapT.splitByKey(root, l, comp, true);
        TreapT<Line> node = buffer.alloc();
        node.key = l;
        p0[0] = TreapT.merge(p0[0], node);
        root = TreapT.merge(p0[0], p0[1]);
    }


    static Line body = new Line(0, 0, 0, 0);

    public Line best(double x, double z) {
        body.b = x;
        body.c = z;
        return searchLine(body);
    }

    public double query(double x, double z) {
        Line best = best(x, z);
        if (best == null) {
            return 0;
        }
        return best.apply(x, z);
    }


    public boolean empty() {
        return root.size == 0;
    }

    private static double inf = 1e100;

    private double intersectY(Line a, Line b) {
        if (a.c == b.c) {
            return a.b > b.b ? inf : 0;
        }
        return (a.b - b.b) / (a.c - b.c);
    }

    public void add(double b, double c) {
        Line line = new Line(b, c, 0, inf);
        while (true) {
            Line floor = popFloor(line, comp);
            if (floor == null) {
                line.l = 0;
                break;
            }
            double y = intersectY(floor, line);
            line.l = y;
            if (y <= floor.l) {
                continue;
            } else {
                floor.r = Math.min(floor.r, line.l);
                add0(floor);
                break;
            }
        }
        while (true) {
            Line ceil = popCeil(line, comp);
            if (ceil == null) {
                line.r = inf;
                break;
            }
            double y = intersectY(line, ceil);
            line.r = y;
            if (y >= ceil.r) {
                continue;
            } else {
                ceil.l = Math.max(ceil.l, line.r);
                add0(ceil);
                break;
            }
        }
        if (line.l < line.r) {
            add0(line);
        }
    }
}