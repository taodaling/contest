package contest;

import template.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskA {
    int r;
    int c;

    public boolean inboard(IntPoint a) {
        return a.x == 0 || a.y == 0 ||
                a.x == c || a.y == r;
    }

    boolean valid = true;
    BIT lb;
    BIT rb;
    BIT tb;
    BIT bb;

    public void check(IntPoint pt) {
        if (pt.x == 0) {
            valid = valid && lb.query(pt.y + 1) == 0;
        }
        if (pt.x == c) {
            valid = valid && rb.query(pt.y + 1) == 0;
        }
        if (pt.y == 0) {
            valid = valid && bb.query(pt.x + 1) == 0;
        }
        if (pt.y == r) {
            valid = valid && tb.query(pt.x + 1) == 0;
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        c = in.readInt();
        r = in.readInt();
        int n = in.readInt();

        int[][] pts = new int[n][4];
        IntegerList allInts = new IntegerList(4 + n * 4);
        allInts.add(0);
        allInts.add(r);
        allInts.add(c);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 4; j++) {
                allInts.add(pts[i][j] = in.readInt());
            }
        }
        DiscreteMap dm = new DiscreteMap(allInts.toArray(), 0, allInts.size());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 4; j++) {
                pts[i][j] = dm.rankOf(pts[i][j]);
            }
        }
        c = dm.rankOf(c);
        r = dm.rankOf(r);

        lb = new BIT(r + 1);
        rb = new BIT(r + 1);
        tb = new BIT(c + 1);
        bb = new BIT(c + 1);
        List<IntLine> lineList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            IntPoint a = new IntPoint(pts[i][0], pts[i][1]);
            IntPoint b = new IntPoint(pts[i][2], pts[i][3]);
            if (inboard(a) && inboard(b)) {
                if (a.x == 0 && b.x == 0) {
                    int cnt = lb.query(Math.max(b.y, a.y) + 1) - lb.query(Math.min(b.y, a.y) + 1);
                    if (cnt != 0) {
                        valid = false;
                    }
                    lb.update(Math.max(b.y, a.y) + 1, 1);
                    lb.update(Math.min(b.y, a.y) + 1, -1);
                } else if (a.y == 0 && b.y == 0) {
                    int cnt = bb.query(Math.max(b.x, a.x) + 1) - bb.query(Math.min(b.x, a.x) + 1);
                    if (cnt != 0) {
                        valid = false;
                    }
                    bb.update(Math.max(b.x, a.x) + 1, 1);
                    bb.update(Math.min(b.x, a.x) + 1, -1);
                } else if (a.x == c && b.x == c) {
                    int cnt = rb.query(Math.max(b.y, a.y) + 1) - rb.query(Math.min(b.y, a.y) + 1);
                    if (cnt != 0) {
                        valid = false;
                    }
                    rb.update(Math.max(b.y, a.y) + 1, 1);
                    rb.update(Math.min(b.y, a.y) + 1, -1);
                } else if (a.y == r && b.y == r) {
                    int cnt = tb.query(Math.max(b.x, a.x) + 1) - tb.query(Math.min(b.x, a.x) + 1);
                    if (cnt != 0) {
                        valid = false;
                    }
                    tb.update(Math.max(b.x, a.x) + 1, 1);
                    tb.update(Math.min(b.x, a.x) + 1, -1);
                } else {
                    IntLine line = new IntLine(a, b);
                    lineList.add(line);
                }
            }
        }

        IntLine[] lines = lineList.toArray(new IntLine[0]);
        for (IntLine line : lines) {
            check(line.a);
            check(line.b);
        }
        dac(lines, 0, lines.length - 1);
        out.println(valid ? "YES" : "NO");
    }


    RandomWrapper rw = new RandomWrapper(new Random());

    public void dac(IntLine[] lines, int l, int r) {
        if (!valid || r <= l) {
            return;
        }
        SequenceUtils.swap(lines, rw.nextInt(l, r), l);
        int sep = l + 1;
        for (int i = l + 1; i <= r; i++) {
            int sa = lines[l].whichSide(lines[i].a);
            int sb = lines[l].whichSide(lines[i].b);
            if (sa * sb == -1) {
                valid = false;
            }
            if (sa + sb < 0) {
                SequenceUtils.swap(lines, sep, i);
                sep++;
            }
        }
        dac(lines, l + 1, sep - 1);
        dac(lines, sep, r);
    }
}

class IntPoint {
    int x;
    int y;

    public IntPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}

class IntLine {
    IntPoint a;
    IntPoint b;

    @Override
    public String toString() {
        return a + "->" + b;
    }

    public IntLine(IntPoint a, IntPoint b) {
        this.a = a;
        this.b = b;
    }

    public int whichSide(IntPoint pt) {
        long vecx = b.x - a.x;
        long vecy = b.y - a.y;
        long ptx = pt.x - a.x;
        long pty = pt.y - a.y;
        return Long.signum(vecx * pty - vecy * ptx);
    }
}