package on2021_04.on2021_04_15_Codeforces___RCC_2014_Warmup__Div__1_.D__Big_Problems_for_Organizers;



import template.datastructure.Splay;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Permutation;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DBigProblemsForOrganizers {
    List<UndirectedEdge>[] g;
    LcaOnTree lca;
    Diameter[] diameters;
    DistCalc dc;
    int[] depth;
    Diameter[][] exclude;

    public void dfs(int root, int p) {
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root);
        }
    }

    public void dfsForDiameter(int root, int p) {
        Diameter d = new Diameter(root, root);
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForDiameter(e.to, root);
            d = Diameter.merge(d, diameters[e.to], dc);
        }
        diameters[root] = d;
    }

    public void dfsUpDown(int root, int p, Diameter top) {
        int n = g[root].size();
        Diameter[] prev = new Diameter[n];
        for (int i = 0; i < n; i++) {
            UndirectedEdge e = g[root].get(i);
            if (e.to == p) {
                prev[i] = top;
            } else {
                prev[i] = diameters[e.to];
            }
        }
        Diameter[] post = prev.clone();
        exclude[root] = new Diameter[n];
        for (int i = 1; i < n; i++) {
            prev[i] = Diameter.merge(prev[i], prev[i - 1], dc);
        }
        for (int i = n - 2; i >= 0; i--) {
            post[i] = Diameter.merge(post[i], post[i + 1], dc);
        }
        for (int i = 0; i < n; i++) {
            exclude[root][i] = new Diameter(root, root);
            if (i > 0) {
                exclude[root][i] = Diameter.merge(exclude[root][i], prev[i - 1], dc);
            }
            if (i + 1 < n) {
                exclude[root][i] = Diameter.merge(exclude[root][i], post[i + 1], dc);
            }
            UndirectedEdge e = g[root].get(i);
            if (e.to == p) {
                continue;
            }
            dfsUpDown(e.to, root, exclude[root][i]);
        }
    }

    public int indexOf(List<UndirectedEdge> list, int to) {
        int l = 0;
        int r = list.size() - 1;
        while (l < r) {
            int m = (l + r + 1) / 2;
            int cmpRes = list.get(m).to - to;
            if (cmpRes <= 0) {
                l = m;
            } else {
                r = m - 1;
            }
        }
        assert list.get(l).to == to;
        return l;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        depth = new int[n];
        LCTNode[] nodes = new LCTNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LCTNode();
            nodes[i].id = i;
            nodes[i].treeWeight = 1;
        }
        g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            Graph.addUndirectedEdge(g, a, b);
            LCTNode.join(nodes[a], nodes[b]);
        }
        for (int i = 0; i < n; i++) {
            g[i].sort(Comparator.comparingInt(x -> x.to));
        }
        diameters = new Diameter[n];
        exclude = new Diameter[n][];
        lca = new LcaOnTree(g, 0);
        dfs(0, -1);
        dc = new DistCalc(lca, depth);
        dfsForDiameter(0, -1);
        dfsUpDown(0, -1, new Diameter(0, 0));

        int m = in.ri();
        for (int i = 0; i < m; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            LCTNode.makeRoot(nodes[u]);
            LCTNode.access(nodes[v]);
            LCTNode.splay(nodes[v]);
            int size = nodes[v].treeSize;
            //leftHalf
            Diameter du = null;
            Diameter dv = null;

            if (size % 2 == 0) {
                LCTNode a = LCTNode.rankK(nodes[u], size / 2);
                LCTNode b = LCTNode.rankK(nodes[u], size / 2 + 1);
                int aid = a.id;
                int bid = b.id;
                du = exclude[aid][indexOf(g[aid], bid)];
                dv = exclude[bid][indexOf(g[bid], aid)];
            } else {
                LCTNode a = LCTNode.rankK(nodes[u], size / 2);
                LCTNode b = LCTNode.rankK(nodes[u], size / 2 + 1);
                LCTNode c = LCTNode.rankK(nodes[u], size / 2 + 2);
                int bid = b.id;
                du = exclude[bid][indexOf(g[bid], c.id)];
                dv = exclude[bid][indexOf(g[bid], a.id)];
            }

            int d1 = Math.max(dc.dist(u, du.data[0]), dc.dist(u, du.data[1]));
            int d2 = Math.max(dc.dist(v, dv.data[0]), dc.dist(v, dv.data[1]));
            int ans = Math.max(d1, d2);
            out.println(ans);
        }
    }
}

class DistCalc {
    LcaOnTree lca;
    int[] depth;

    public DistCalc(LcaOnTree lca, int[] depth) {
        this.lca = lca;
        this.depth = depth;
    }

    public int dist(int a, int b) {
        int c = lca.lca(a, b);
        return depth[a] + depth[b] - depth[c] * 2;
    }
}

class Diameter {
    int[] data;

    public Diameter(int... data) {
        this.data = data;
    }


    static Diameter merge(Diameter a, Diameter b, DistCalc calc) {
        assert a != null;
        assert b != null;
        int[] best = new int[2];
        int d1 = calc.dist(a.data[0], a.data[1]);
        int d2 = calc.dist(b.data[0], b.data[1]);
        int cur;
        if (d1 < d2) {
            best[0] = b.data[0];
            best[1] = b.data[1];
            cur = d2;
        } else {
            best[0] = a.data[0];
            best[1] = a.data[1];
            cur = d1;
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int cand = calc.dist(a.data[i], b.data[j]);
                if (cand > cur) {
                    cur = cand;
                    best[0] = a.data[i];
                    best[1] = b.data[j];
                }
            }
        }

        return new Diameter(best);
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

    public LCTNode left = NIL;
    public LCTNode right = NIL;
    public LCTNode father = NIL;
    public LCTNode treeFather = NIL;
    public boolean reverse;
    public int id;
    /**
     * 所在连通块中的treeWeight之和
     */
    public int treeSize;
    public byte treeWeight;

    public static LCTNode rankK(LCTNode root, int k) {
        splay(root);
        LCTNode last = root;
        while (root != LCTNode.NIL) {
            last = root;
            root.pushDown();
            if (root.left.treeSize >= k) {
                root = root.left;
            } else if (root.left.treeSize + 1 < k) {
                k -= root.left.treeSize + 1;
                root = root.right;
            } else {
                break;
            }
        }
        splay(last);
        return last;
    }

    public void init() {
        left = right = father = treeFather = NIL;
        reverse = false;
        treeSize = 0;
        pushUp();
    }

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
        makeRoot(y);
        x.treeFather = y;
        y.pushUp();
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
        treeSize = left.treeSize + right.treeSize + treeWeight;
    }

    public static LCTNode lca(LCTNode a, LCTNode b) {
        access(a);
        access(b);
        splay(a);
        if (a.treeFather != NIL) {
            return a.treeFather;
        }
        return NIL;
    }

    public static boolean connected(LCTNode a, LCTNode b) {
        return lca(a, b) != NIL;
    }
}