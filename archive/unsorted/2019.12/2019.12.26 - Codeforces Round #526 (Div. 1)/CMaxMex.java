package contest;

import graphs.lca.Lca;
import template.graph.TreePath;
import template.graph.LcaOnTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerIterator;
import template.primitve.generated.IntegerList;
import template.primitve.generated.IntegerMultiWayStack;
import template.utils.SequenceUtils;

import java.util.function.IntBinaryOperator;

public class CMaxMex {
    int[] depth;
    IntegerMultiWayStack edges;

    public void dfsForDepth(int root, int p, int d) {
        depth[root] = d;
        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            if (node == p) {
                continue;
            }
            dfsForDepth(node, root, d + 1);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] nodeToPerm = new int[n];
        int[] permToNode = new int[n];
        for (int i = 0; i < n; i++) {
            nodeToPerm[i] = in.readInt();
            permToNode[nodeToPerm[i]] = i;
        }

        edges = new IntegerMultiWayStack(n, n + n);
        depth = new int[n];
        for (int i = 1; i < n; i++) {
            int p = in.readInt() - 1;
            edges.addLast(i, p);
            edges.addLast(p, i);
        }
        LcaOnTree lca = new LcaOnTree(edges, 0);
        dfsForDepth(0, -1, 0);
        Segment.lca = lca;
        Segment.distFunc = (a, b) -> depth[a] + depth[b] - depth[lca.lca(a, b)] * 2;

        int l = 0;
        int r = n - 1;
        Segment seg = new Segment(l, r, permToNode);

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            if (in.readInt() == 1) {
                int a = in.readInt() - 1;
                int b = in.readInt() - 1;
                SequenceUtils.swap(nodeToPerm, a, b);
                permToNode[nodeToPerm[a]] = a;
                permToNode[nodeToPerm[b]] = b;
                seg.update(nodeToPerm[a], nodeToPerm[a], l, r, a);
                seg.update(nodeToPerm[b], nodeToPerm[b], l, r, b);
            } else {
                out.println(seg.query(l, r));
            }
        }
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;

    static LcaOnTree lca;
    static IntBinaryOperator distFunc;

    TreePath path = new TreePath();
    boolean isPath;

    public void pushUp() {
        path.mergeAsLongestPath(left.path, right.path, distFunc);
        if (!left.isPath || !right.isPath) {
            isPath = false;
        } else {
            isPath = path.include(left.path.getA(), lca) &&
                    path.include(left.path.getB(), lca) &&
                    path.include(right.path.getA(), lca) &&
                    path.include(right.path.getB(), lca);
        }
    }

    public void pushDown() {
    }

    public Segment(int l, int r, int[] nodes) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, nodes);
            right = new Segment(m + 1, r, nodes);
            pushUp();
        } else {
            path.initAsSingleVertex(nodes[l]);
            isPath = true;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int node) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            path.initAsSingleVertex(node);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, node);
        right.update(ll, rr, m + 1, r, node);
        pushUp();
    }

    public int query(int l, int r) {
        TreePath pair = null;
        TreePath tmp = new TreePath();

        Segment trace = this;
        while (l < r) {
            int m = (l + r) >>> 1;
            trace.pushDown();
            if (trace.left.isPath) {
                if (pair == null) {
                    pair = new TreePath();
                    pair.copyOf(trace.left.path);
                    l = m + 1;
                    trace = trace.right;
                    continue;
                }
                tmp.mergeAsLongestPath(pair, trace.left.path, distFunc);
                if (tmp.include(pair.getA(), lca) && tmp.include(pair.getB(), lca) &&
                        tmp.include(trace.left.path.getA(), lca) && tmp.include(trace.left.path.getB(), lca)) {
                    l = m + 1;
                    trace = trace.right;
                    pair.copyOf(tmp);
                    continue;
                }
            }
            trace = trace.left;
            r = m;
        }

        if (trace.isPath) {
            if (pair == null) {
                return l + 1;
            }
            tmp.mergeAsLongestPath(pair, trace.path, distFunc);
            if (tmp.include(pair.getA(), lca) && tmp.include(pair.getB(), lca) &&
                    tmp.include(trace.path.getA(), lca) && tmp.include(trace.path.getB(), lca)) {
                return l + 1;
            }
        }

        return l;
    }
}


