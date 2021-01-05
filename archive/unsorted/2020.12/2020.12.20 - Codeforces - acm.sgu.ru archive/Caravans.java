package contest;



import template.datastructure.DSU;
import template.geometry.geo2.IntegerPoint2;
import template.geometry.geo2.IntegerTriangulation;
import template.geometry.geo2.Point2;
import template.geometry.geo2.Triangulation;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.KahamSummation;
import template.primitve.generated.datastructure.LongObjectEntryIterator;
import template.primitve.generated.datastructure.LongObjectHashMap;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Caravans {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Point2Ext[] pts = new Point2Ext[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2Ext(in.ri() - 5000 , in.ri() - 5000 );
            pts[i].id = i;
        }
        Randomized.shuffle(pts);
        IntegerTriangulation tri = new IntegerTriangulation(30000);
        for (IntegerPoint2 pt : pts) {
            tri.addPoint(pt);
        }
        HashMap<Long, Edge> map = new HashMap<>(n);
        tri.forEach(triangle -> {
            for (int i = 0; i < 3; i++) {
                if (triangle.p[i].getClass() == Point2Ext.class && triangle.p[(i + 1) % 3].getClass() == Point2Ext.class) {
                    Point2Ext a = (Point2Ext) triangle.p[i];
                    Point2Ext b = (Point2Ext) triangle.p[(i + 1) % 3];
                    long sig = edgeId(a.id, b.id);
                    if (map.containsKey(sig)) {
                        continue;
                    }
                    map.put(sig, new Edge(a.id, b.id, IntegerPoint2.dist(a, b)));
                }
            }
        });
        List<Edge> edges = new ArrayList<>(map.values());
        edges.sort(Comparator.comparing(x -> x.dist));
        DSU dsu = new DSU(n);
        dsu.init();
        LCTNode[] nodes = new LCTNode[n];
        for(int i = 0; i < n; i++){
            nodes[i] = new LCTNode();
        }
        for (Edge e : edges) {
            if (dsu.find(e.a) != dsu.find(e.b)) {
                dsu.merge(e.a, e.b);
                LCTNode mid = new LCTNode();
                mid.weight = e.dist;
                mid.pushUp();
                LCTNode.join(nodes[e.a], mid);
                LCTNode.join(nodes[e.b], mid);
            }
        }
        int q = in.ri();
        for(int i = 0; i < q; i++){
            LCTNode a = nodes[in.ri() - 1];
            LCTNode b = nodes[in.ri() - 1];
            LCTNode.findRoute(a, b);
            LCTNode.splay(a);
            double ans = a.maxWeight;
            out.println(ans);
        }
        debug.summary();
    }

    public long edgeId(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return DigitUtils.asLong(a, b);
    }
}

class Point2Ext extends IntegerPoint2 {
    int id;

    public Point2Ext(long x, long y) {
        super(x, y);
    }
}

class Edge {
    int a;
    int b;
    double dist;

    public Edge(int a, int b, double dist) {
        this.a = a;
        this.b = b;
        this.dist = dist;
    }
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
    /**
     * 所在连通块中的treeWeight之和
     */
    int treeSize;
    int vtreeSize;
    byte treeWeight;

    double weight;
    double maxWeight;

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
        treeSize = left.treeSize + right.treeSize + vtreeSize + treeWeight;
        maxWeight = weight;
        maxWeight = Math.max(maxWeight, left.maxWeight);
        maxWeight = Math.max(maxWeight, right.maxWeight);
    }
}
