package on2020_05.on2020_05_18_Codeforces___Codeforces_Round__423__Div__1__rated__based_on_VK_Cup_Finals_.D__Best_Edge_Weight;



import sun.security.x509.EDIPartyName;
import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class DBestEdgeWeight {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        LCTNode[] nodes = new LCTNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LCTNode();
            nodes[i].id = i;
        }
        DSU dsu = new DSU(n);
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = in.readInt() - 1;
            edges[i].b = in.readInt() - 1;
            edges[i].c = in.readInt();
        }

        Edge[] sorted = edges.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a.c, b.c));
        for (Edge e : sorted) {
            if (dsu.find(e.a) == dsu.find(e.b)) {
                continue;
            }
            dsu.merge(e.a, e.b);
            e.edge = new LCTNode();
            e.edge.w = e.c;
            e.edge.pushUp();
            LCTNode.join(e.edge, nodes[e.a]);
            LCTNode.join(e.edge, nodes[e.b]);
        }

        for (Edge e : edges) {
            if (e.edge != null) {
                continue;
            }
            LCTNode.findRoute(nodes[e.a], nodes[e.b]);
            LCTNode.splay(nodes[e.a]);
            e.ans = nodes[e.a].maxW - 1;
            nodes[e.a].modify(e.c);
        }

        for (Edge e : edges) {
            if (e.edge == null) {
                continue;
            }
            LCTNode.access(e.edge);
            LCTNode.splay(e.edge);
            e.ans = e.edge.minWeight - 1;
        }

        for (Edge e : edges) {
            if (e.ans > 1e9) {
                out.println(-1);
            } else {
                out.println(e.ans);
            }
        }
    }
}

class Edge {
    int a;
    int b;
    int c;

    int ans;
    LCTNode edge;
}

class LCTNode {
    public static final LCTNode NIL = new LCTNode();

    static {
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.father = NIL;
        NIL.treeFather = NIL;
    }

    LCTNode left = NIL;
    LCTNode right = NIL;
    LCTNode father = NIL;
    LCTNode treeFather = NIL;
    boolean reverse;
    int id;
    static int inf = (int) 2e9;
    int minWeight = inf;
    int dirty = inf;

    int w = 0;
    int maxW = 0;

    public static void access(LCTNode x) {
        LCTNode last = NIL;
        while (x != NIL) {
            splay(x);
            x.right.father = NIL;
            x.right.treeFather = x;
            x.setRight(last);
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
        x.treeFather = y;
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

        if (dirty != inf) {
            left.modify(dirty);
            right.modify(dirty);
            dirty = inf;
        }
    }

    public void modify(int d) {
        if (this == NIL) {
            return;
        }
        dirty = Math.min(dirty, d);
        minWeight = Math.min(minWeight, d);
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
        maxW = Math.max(left.maxW, right.maxW);
        maxW = Math.max(maxW, w);
    }
}
