package on2020_09.on2020_09_14_Codeforces___Codeforces_Round__360__Div__1_.D__Dividing_Kingdom_II;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerVersionArray;
import template.utils.CompareUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;

public class DDividingKingdomII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].u = in.readInt() - 1;
            edges[i].v = in.readInt() - 1;
            edges[i].w = in.readInt();
        }


        XorDeltaDSU dsu = new XorDeltaDSU(n);
        List<Edge> collect = new ArrayList<>();
        Edge[] special = new Edge[m];
        Segment seg = new Segment(0, m - 1, i -> edges[i]);
        CompareUtils.ensureObjectSpace(m);
        for (int i = 0; i < q; i++) {
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            collect.clear();
            seg.query(l, r, 0, m - 1, collect);
            int k = collect.size();
            collect.toArray(special);
            CompareUtils.radixSortIntObject(special, 0, k - 1, x -> x.w);
            int ans = -1;
            dsu.reset();
            for (int j = k - 1; j >= 0; j--) {
                Edge e = special[j];
                if (dsu.find(e.u) == dsu.find(e.v)) {
                    if (dsu.delta(e.u, e.v) == 0) {
                        ans = e.w;
                        break;
                    }
                    continue;
                }
                dsu.merge(e.u, e.v, 1);
            }
            out.println(ans);
        }
    }

}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private static XorDeltaDSU dsu = new XorDeltaDSU(1000);
    private List<Edge> sorted;
    private static List<Edge> buf = new ArrayList<>();

    public static void mergeTo(List<Edge> a, List<Edge> b, List<Edge> c) {
        int lOffset = 0;
        int rOffset = 0;
        int lSize = a.size();
        int rSize = b.size();
        dsu.reset();
        while (lOffset < lSize || rOffset < rSize) {
            if (rOffset >= rSize || lOffset < lSize &&
                    a.get(lOffset).w >=
                            b.get(rOffset).w) {
                c.add(a.get(lOffset++));
            } else {
                c.add(b.get(rOffset++));
            }
            Edge last = c.get(c.size() - 1);
            if (dsu.find(last.u) == dsu.find(last.v)) {
                if (dsu.delta(last.u, last.v) == 1) {
                    c.remove(c.size() - 1);
                    continue;
                }
                break;
            }
            dsu.merge(last.u, last.v, 1);
        }
    }

    public void pushUp() {
        mergeTo(left.sorted, right.sorted, sorted = new ArrayList<>());
    }

    public void pushDown() {
    }

    public Segment(int l, int r, IntFunction<Edge> func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            sorted = Collections.singletonList(func.apply(l));
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }


    public void query(int ll, int rr, int l, int r, List<Edge> collect) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            collect.addAll(sorted);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m, collect);
        right.query(ll, rr, m + 1, r, collect);
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
            builder.append("val").append(",");
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


class XorDeltaDSU {
    int[] p;
    int[] rank;
    int[] delta;
    int[] version;
    int now;

    public void access(int i) {
        if (version[i] != now) {
            version[i] = now;
            rank[i] = 0;
            delta[i] = 0;
            p[i] = i;
        }
    }

    public XorDeltaDSU(int n) {
        p = new int[n];
        rank = new int[n];
        delta = new int[n];
        version = new int[n];
        reset();
    }

    public void reset() {
        now++;
    }

    public int find(int a) {
        access(a);
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        find(p[a]);
        delta[a] ^= delta[p[a]];
        return p[a] = p[p[a]];
    }

    /**
     * return a - b, you should ensure a and b belong to same set
     */
    public int delta(int a, int b) {
        find(a);
        find(b);
        return delta[a] ^ delta[b];
    }

    /**
     * a - b = delta
     */
    public void merge(int a, int b, int d) {
        find(a);
        find(b);
        d = d ^ delta[a] ^ delta[b];
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (rank[a] == rank[b]) {
            rank[a]++;
        }
        if (rank[a] > rank[b]) {
            p[b] = a;
            delta[b] = d;
        } else {
            p[a] = b;
            delta[a] = d;
        }
    }
}

class Edge {
    int u;
    int v;
    int w;

    @Override
    public String toString() {
        return String.format("(%d, %d)=%d", u, v, w);
    }
}