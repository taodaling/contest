package contest;

import template.graph.LcaOnTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Log2;
import template.math.DigitUtils;
import template.primitve.generated.IntegerIterator;
import template.primitve.generated.LongHashSet;
import template.primitve.generated.IntegerMultiWayStack;

import java.util.Arrays;

public class FDFS {
    IntegerMultiWayStack edges;
    int[] intervalL;
    int[] intervalR;
    int[] depths;
    int[][] jumps;
    int n;
    Segment segtree;


    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int q = in.readInt();
        edges = new IntegerMultiWayStack(n, n * 2);
        intervalL = new int[n];
        intervalR = new int[n];
        depths = new int[n];
        jumps = new int[n][20];

        for (int i = 1; i < n; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            edges.addLast(a, b);
            edges.addLast(b, a);
        }

        dfs(0, -1, 0);
        segtree = new Segment(1, n);
        LongHashSet exist = new LongHashSet(q, false);
        LcaOnTree lcaOnTree = new LcaOnTree(edges, 0);

        for (int i = 0; i < q; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            if (depths[a] > depths[b]) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            int lca = lcaOnTree.lca(a, b);
            long eId = edgeId(a, b);
            if (exist.contain(eId)) {
                exist.remove(eId);
                if (lca == a) {
                    int sub = climb(b, depths[lca] + 1);
                    update(sub, -1);
                    update(b, 1);
                } else {
                    update(0, -1);
                    update(a, 1);
                    update(b, 1);
                }
            } else {
                exist.add(eId);
                if (lca == a) {
                    int sub = climb(b, depths[lca] + 1);
                    update(sub, 1);
                    update(b, -1);
                } else {
                    update(0, 1);
                    update(a, -1);
                    update(b, -1);
                }
            }

            if (segtree.min < 0) {
                throw new RuntimeException();
            }
            if (segtree.min > 0) {
                out.println(0);
            } else {
                out.println(segtree.cnt);
            }
        }
    }

    public void update(int root, int x) {
        segtree.update(intervalL[root], intervalR[root], 1, n, x);
    }

    public long edgeId(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return DigitUtils.asLong(a, b);
    }

    int alloc = 1;

    int climb(int root, int depth) {
        if (depths[root] == depth) {
            return root;
        }
        return climb(jumps[root][Log2.floorLog(depths[root] - depth)], depth);
    }

    void dfs(int root, int p, int depth) {
        Arrays.fill(jumps[root], -1);
        jumps[root][0] = p;
        for (int i = 0; jumps[root][i] != -1; i++) {
            jumps[root][i + 1] = jumps[jumps[root][i]][i];
        }
        depths[root] = depth;

        intervalL[root] = alloc++;
        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            if (node == p) {
                continue;
            }
            dfs(node, root, depth + 1);
        }
        intervalR[root] = alloc - 1;
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int mod;
    int min;
    int cnt;

    public void modify(int m) {
        mod += m;
        min += m;
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
        cnt = 0;
        if (min == left.min) {
            cnt += left.cnt;
        }
        if (min == right.min) {
            cnt += right.cnt;
        }
    }

    public void pushDown() {
        if (mod != 0) {
            left.modify(mod);
            right.modify(mod);
            mod = 0;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            min = 0;
            cnt = 1;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }
}
