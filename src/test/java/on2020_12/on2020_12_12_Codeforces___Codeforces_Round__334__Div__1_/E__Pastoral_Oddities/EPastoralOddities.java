package on2020_12.on2020_12_12_Codeforces___Codeforces_Round__334__Div__1_.E__Pastoral_Oddities;



import template.graph.OfflineConnectivityChecker;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.*;

public class EPastoralOddities {
    public boolean connected(LCTNode a, LCTNode b) {
        LCTNode.makeRoot(b);
        LCTNode.access(a);
        return LCTNode.findRoot(a) == b;
    }

    int odd;
    Debug debug = new Debug(true);

    public void delete(LCTNode e, LCTNode a, LCTNode b) {
        LCTNode.makeRoot(e);
        odd -= e.treeSize & 1;
        LCTNode.cut(e, a);
        LCTNode.cut(e, b);
        LCTNode.makeRoot(a);
        LCTNode.makeRoot(b);
        odd += a.treeSize & 1;
        odd += b.treeSize & 1;
        debug.debug("delete", e.length);
    }

    public void add(LCTNode e, LCTNode a, LCTNode b) {
        LCTNode.makeRoot(a);
        LCTNode.makeRoot(b);
        odd -= a.treeSize & 1;
        odd -= b.treeSize & 1;
        LCTNode.join(a, e);
        LCTNode.join(e, b);
        LCTNode.makeRoot(e);
        odd += e.treeSize & 1;
        debug.debug("add", e.length);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        PriorityQueue<Edge> pq = new PriorityQueue<>(m, Comparator.comparingInt(x -> -x.l));
        Map<LCTNode, Edge> map = new IdentityHashMap<>(m);
        odd = n;
        int inf = (int) 2e9;
        int best = inf;
        LCTNode[] nodes = new LCTNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LCTNode();
            nodes[i].id = i;
            nodes[i].weight = 1;
            nodes[i].pushUp();
        }
        for (int i = 0; i < m; i++) {
            Edge e = new Edge();
            e.a = nodes[in.ri() - 1];
            e.b = nodes[in.ri() - 1];
            e.l = in.ri();

            if (connected(e.a, e.b)) {
                LCTNode cand = e.b.longest;
                if (cand.length > e.l) {
                    Edge deleted = map.get(cand);
                    delete(deleted.node, deleted.a, deleted.b);
                }
            }
            if (!connected(e.a, e.b)) {
                e.node = new LCTNode();
                e.node.id = -(e.a.id * 100 + e.b.id);
                e.node.length = e.l;
                e.node.pushUp();
                map.put(e.node, e);
                pq.add(e);
                add(e.node, e.a, e.b);
            }

            while (odd == 0) {
                Edge top = pq.remove();
                best = Math.min(best, top.l);
                if (!connected(top.node, top.a)) {
                    continue;
                }
                delete(top.node, top.a, top.b);
            }
            out.println(best == inf ? -1 : best);
        }
    }
}

class Edge {
    LCTNode a;
    LCTNode b;
    int l;
    LCTNode node;
}

class LCTNode {
    public static final LCTNode NIL = new LCTNode();

    static {
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.father = NIL;
        NIL.treeFather = NIL;
        NIL.longest = NIL;
    }

    LCTNode left = NIL;
    LCTNode right = NIL;
    LCTNode father = NIL;
    LCTNode treeFather = NIL;
    boolean reverse;
    int id;
    int treeSize;
    int vtreeSize;
    byte weight;
    int length;
    LCTNode longest;

    public static void access(LCTNode x) {
        LCTNode last = NIL;
        while (x != NIL) {
            splay(x);
            x.right.father = NIL;
            x.right.treeFather = x;
            x.vtreeSize += x.right.treeSize;
            x.setRight(last);
            x.vtreeSize -= last.treeSize;
            x.pushUp();

            last = x;
            x = x.treeFather;
        }
    }

    public static void makeRoot(LCTNode x) {
        access(x);
        splay(x);
        x.reverse();
    }

    public static void cut(LCTNode y, LCTNode x) {
        makeRoot(y);
        access(x);
        splay(y);
        y.right.treeFather = NIL;
        y.right.father = NIL;
        y.setRight(NIL);
        y.pushUp();
    }

    public static void join(LCTNode y, LCTNode x) {
        makeRoot(x);
        makeRoot(y);
        x.treeFather = y;
        y.vtreeSize += x.treeSize;
    }

    public static void findRoute(LCTNode x, LCTNode y) {
        makeRoot(y);
        access(x);
    }

    public static void splay(LCTNode x) {
        if (x == NIL) {
            return;
        }
        LCTNode y, z;
        while ((y = x.father) != NIL) {
            if ((z = y.father) == NIL) {
                y.pushDown();
                x.pushDown();
                if (x == y.left) {
                    zig(x);
                } else {
                    zag(x);
                }
            } else {
                z.pushDown();
                y.pushDown();
                x.pushDown();
                if (x == y.left) {
                    if (y == z.left) {
                        zig(y);
                        zig(x);
                    } else {
                        zig(x);
                        zag(x);
                    }
                } else {
                    if (y == z.left) {
                        zag(x);
                        zig(x);
                    } else {
                        zag(y);
                        zag(x);
                    }
                }
            }
        }

        x.pushDown();
        x.pushUp();
    }

    public static void zig(LCTNode x) {
        LCTNode y = x.father;
        LCTNode z = y.father;
        LCTNode b = x.right;

        y.setLeft(b);
        x.setRight(y);
        z.changeChild(y, x);

        y.pushUp();
    }

    public static void zag(LCTNode x) {
        LCTNode y = x.father;
        LCTNode z = y.father;
        LCTNode b = x.left;

        y.setRight(b);
        x.setLeft(y);
        z.changeChild(y, x);

        y.pushUp();
    }

    public static LCTNode findRoot(LCTNode x) {
        splay(x);
        x.pushDown();
        while (x.left != NIL) {
            x = x.left;
            x.pushDown();
        }
        splay(x);
        return x;
    }

    @Override
    public String toString() {
        return "" + id;
    }

    public void pushDown() {
        if (this == NIL) {
            return;
        }
        if (reverse) {
            reverse = false;

            LCTNode tmpNode = left;
            left = right;
            right = tmpNode;

            left.reverse();
            right.reverse();
        }

        left.treeFather = treeFather;
        right.treeFather = treeFather;
    }

    public void reverse() {
        reverse = !reverse;
    }

    public void setLeft(LCTNode x) {
        left = x;
        x.father = this;
    }

    public void setRight(LCTNode x) {
        right = x;
        x.father = this;
    }

    public void changeChild(LCTNode y, LCTNode x) {
        if (left == y) {
            setLeft(x);
        } else {
            setRight(x);
        }
    }

    public void pushUp() {
        if (this == NIL) {
            return;
        }
        treeSize = left.treeSize + right.treeSize + vtreeSize + weight;
        longest = this;
        if (left.longest.length > longest.length) {
            longest = left.longest;
        }
        if (right.longest.length > longest.length) {
            longest = right.longest;
        }
    }
}
