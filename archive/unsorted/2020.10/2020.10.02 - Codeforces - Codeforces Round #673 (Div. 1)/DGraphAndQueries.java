package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.utils.Debug;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class DGraphAndQueries {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();
        int[] p = new int[n];
        in.populate(p);


        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = in.readInt() - 1;
            edges[i].b = in.readInt() - 1;
        }


        int[][] qs = new int[4][q];
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < 2; j++) {
                qs[j][i] = in.readInt();
            }
            if (qs[0][i] == 2) {
                edges[qs[1][i] - 1].delete = true;
            }
        }


        DSUExt dsu = new DSUExt(n);
        dsu.reset();
        for (Edge e : edges) {
            if (e.delete) {
                continue;
            }
            dsu.merge(e.a, e.b);
        }

        for (int i = q - 1; i >= 0; i--) {
            if (qs[0][i] == 2) {
                Edge e = edges[qs[1][i] - 1];
                dsu.merge(e.a, e.b);
            } else {
                int v = qs[1][i] - 1;
                int pa = dsu.find(v);
                qs[2][i] = dsu.begin[pa];
                qs[3][i] = dsu.end[pa];
            }
        }

        int[] id = new int[n];
        int[] inv = new int[n];
        int idAlloc = 0;
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) != i) {
                continue;
            }
            for (int trace = dsu.begin[i]; trace != -1; trace = dsu.next[trace]) {
                id[trace] = idAlloc++;
                inv[id[trace]] = trace;
            }
        }

        Segment seg = new Segment(0, n - 1, i -> p[inv[i]]);
        debug.debug("seg", seg);
        for (int i = 0; i < q; i++) {
            if (qs[0][i] != 1) {
                continue;
            }
            int l = id[qs[2][i]];
            int r = id[qs[3][i]];
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            int ans = seg.queryL(l, r, 0, n - 1);
            seg.pop(l, r, 0, n - 1, ans);
            out.println(ans);

            debug.debug("debug", seg);
        }
    }

    Debug debug = new Debug(true);
}


class Edge {
    int a;
    int b;
    boolean delete;
}

class DSUExt extends DSU {
    int[] begin;
    int[] end;
    int[] next;

    public DSUExt(int n) {
        super(n);
        begin = new int[n];
        end = new int[n];
        next = new int[n];
    }

    @Override
    public void reset() {
        super.reset();
        for (int i = 0; i < begin.length; i++) {
            begin[i] = end[i] = i;
            next[i] = -1;
        }
    }

    @Override
    protected void preMerge(int a, int b) {
        next[end[a]] = begin[b];
        end[a] = end[b];
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int max;

    private void modify(int x) {
        max = x;
    }

    public void pushUp() {
        max = Math.max(left.max, right.max);
    }

    public void pushDown() {
    }

    public Segment(int l, int r, IntToIntegerFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            max = func.apply(l);
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public boolean pop(int ll, int rr, int l, int r, int maxVal) {
        if (noIntersection(ll, rr, l, r)) {
            return false;
        }
        if (covered(ll, rr, l, r)) {
            if (max != maxVal) {
                return false;
            }
            if (l == r) {
                modify(0);
                return true;
            }
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        boolean ans = left.pop(ll, rr, l, m, maxVal);
        if (!ans) {
            ans = right.pop(ll, rr, m + 1, r, maxVal);
        }
        pushUp();
        return ans;
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return max;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.max(left.queryL(ll, rr, l, m),
                right.queryL(ll, rr, m + 1, r));
    }

    private Segment deepClone() {
        Segment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected Segment clone() {
        try {
            return (Segment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(max).append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}