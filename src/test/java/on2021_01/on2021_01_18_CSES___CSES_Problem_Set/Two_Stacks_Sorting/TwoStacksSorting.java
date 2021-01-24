package on2021_01.on2021_01_18_CSES___CSES_Problem_Set.Two_Stacks_Sorting;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.CompareUtils;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.TreeSet;
import java.util.stream.IntStream;

public class TwoStacksSorting {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] die = new int[n];
        int[] indices = IntStream.range(0, n).toArray();
        CompareUtils.quickSort(indices, (i, j) -> Integer.compare(a[i], a[j]), 0, n);
        int preMax = 0;
        for (int i : indices) {
            preMax = Math.max(preMax, i);
            die[i] = preMax;
        }
        debug.debugArray("die", die);
        XorDeltaDSU dsu = new XorDeltaDSU(n);
        dsu.init();
        dsu.callback = () -> {
            String impossible = "IMPOSSIBLE";
            out.println(impossible);
            out.flush();
            System.exit(0);
            return;
        };
        Segment.dsu = dsu;
        Segment seg = new Segment(0, n - 1);

        int[] revOrder = indices;
        SequenceUtils.reverse(revOrder);

        //debug.debug("seg", seg);
        for (int i : revOrder) {
            seg.update(i, die[i] - 1, 0, n - 1, i, 1, 0);
            seg.update(i, i, 0, n - 1, i, 0, 1);
            //debug.debug("i", i);
            //debug.debug("seg", seg);
        }
        seg.pushDownAll(0, n - 1);
        for (int i = 0; i < n; i++) {
            int ans = dsu.deltaRoot(i) + 1;
            out.append(ans).append(' ');
        }

        return;
    }
}

class XorDeltaDSU {
    int[] p;
    int[] rank;
    int[] delta;
    Runnable callback;


    public XorDeltaDSU(int n) {
        p = new int[n];
        rank = new int[n];
        delta = new int[n];
        init();
    }

    public void init() {
        init(p.length);
    }

    public void init(int n) {
        for (int i = 0; i < n; i++) {
            p[i] = i;
            rank[i] = 0;
            delta[i] = 0;
        }
    }

    public int find(int a) {
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

    public int deltaRoot(int a) {
        find(a);
        return delta[a];
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
            if (d == 1) {
                callback.run();
            }
            return;
        }
        if (rank[a] < rank[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        if (rank[a] == rank[b]) {
            rank[a]++;
        }
        p[b] = a;
        delta[b] = d;
    }
}


class Segment implements Cloneable {
    static XorDeltaDSU dsu;
    private Segment left;
    private Segment right;
    private int x = -1;
    private int delta;
    private int size;

    private void modify(int x, int d) {
        if (size == 0) {
            return;
        }
        if (this.x != -1) {
            dsu.merge(this.x, x, delta ^ d);
        } else {
            this.x = x;
            delta = d;
        }
    }

    public void pushUp() {
        size = left.size + right.size;
    }

    public void pushDown() {
        if (x != -1) {
            left.modify(x, delta);
            right.modify(x, delta);
            x = -1;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
        }
    }

    private boolean enter(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean leave(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x, int d, int s) {
        if (leave(ll, rr, l, r)) {
            return;
        }
        if (enter(ll, rr, l, r)) {
            size += s;
            modify(x, d);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x, d, s);
        right.update(ll, rr, m + 1, r, x, d, s);
        pushUp();
    }

    public void pushDownAll(int l, int r) {
        if (l == r) {
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.pushDownAll(l, m);
        right.pushDownAll(m + 1, r);
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
            builder.append(x).append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    @Override
    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        deepClone().toString(builder);
//        if (builder.length() > 0) {
//            builder.setLength(builder.length() - 1);
//        }
//        return builder.toString();
        return "";
    }
}
