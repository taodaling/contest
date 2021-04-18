package on2021_04.on2021_04_16_Codeforces___Codeforces_Round__715__Div__1_.C__Complete_the_MST;



import template.datastructure.DSU;
import template.datastructure.LinkedListBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashMap;

import java.util.*;

public class CCompleteTheMST {
    LongHashMap E = new LongHashMap((int) 5e5, false);

    public static int inf = (int) 2e9;

    long id(int u, int v) {
        if (u > v) {
            int tmp = u;
            u = v;
            v = tmp;
        }
        return DigitUtils.asLong(u, v);
    }


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();

        Edge[] edges = new Edge[m];
        List<Edge> created = new ArrayList<>(n + m);
        int xor = 0;
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].u = in.ri() - 1;
            edges[i].v = in.ri() - 1;
            edges[i].w = in.ri();
            E.put(id(edges[i].u, edges[i].v), edges[i].w);
            xor ^= edges[i].w;
        }

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].dist = inf;
            nodes[i].lct = new LCTNode();
        }
        LinkedListBeta<Node> cand = new LinkedListBeta<>();
        for (int i = 1; i < n; i++) {
            nodes[i].lnode = cand.addLast(nodes[i]);
        }
        nodes[0].dist = 0;
        TreeSet<Node> set = new TreeSet<>(Comparator.<Node>comparingInt(x -> x.dist).thenComparingInt(x -> x.id));
        for (int i = 0; i < n; i++) {
            set.add(nodes[i]);
        }

        long sum = 0;
        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            if (head.lnode != null) {
                cand.remove(head.lnode);
                head.lnode = null;
            }
            sum += head.dist;
            for (LinkedListBeta.Node<Node> iter = cand.begin(); iter != cand.end(); iter = iter.next) {
                Node node = iter.val;
                long id = id(head.id, node.id);
                int newW;
                if (E.containKey(id)) {
                    //upd
                    newW = (int) E.get(id);
                } else {
                    Edge e = new Edge();
                    e.u = head.id;
                    e.v = node.id;
                    newW = 0;
                    created.add(e);
                    iter = iter.prev;
                    cand.remove(node.lnode);
                    node.lnode = null;
                }

                if (node.dist > newW) {
                    set.remove(node);
                    node.dist = newW;
                    set.add(node);
                }
            }
        }

        if (created.size() + m != (long) n * (n - 1) / 2) {
            out.println(sum);
            return;
        }

        //have to remove an edge
        DSU dsu = new DSU(n);
        dsu.init();
        for (Edge e : created) {
            Node a = nodes[e.u];
            Node b = nodes[e.v];
            LCTNode.join(a.lct, e.lct);
            LCTNode.join(b.lct, e.lct);
            dsu.merge(e.u, e.v);
        }
        Arrays.sort(edges, Comparator.comparingInt(x -> x.w));
        for (Edge e : edges) {
            Node a = nodes[e.u];
            Node b = nodes[e.v];
            if (dsu.find(e.u) != dsu.find(e.v)) {
                dsu.merge(e.u, e.v);
                LCTNode.join(a.lct, b.lct);
            } else {
                LCTNode.makeRoot(a.lct);
                LCTNode.access(b.lct);
                LCTNode.splay(b.lct);
                b.lct.modify(e.w);
            }
        }

        long best = inf;
        for (Edge e : created) {
            //delete e
            long c = sum;
            LCTNode.splay(e.lct);
            c += Math.min(xor, e.lct.rep);
            best = Math.min(best, c);
        }

        out.println(best);
    }
}

class Edge {
    int u;
    int v;
    int w;

    LCTNode lct = new LCTNode();

    @Override
    public String toString() {
        return "(" + u + "," + v + "," + w + ")";
    }
}

class Node {
    LCTNode lct;
    LinkedListBeta.Node<Node> lnode;
    int dist;
    int id;


    @Override
    public String toString() {
        return "" + (id + 1);
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

    public static int inf = (int) 2e9;
    public int id;
    public int rep = inf;
    public int dirty = inf;


    void modify(int x) {
        rep = Math.min(rep, x);
        dirty = Math.min(dirty, x);
    }

    public void init() {
        left = right = father = treeFather = NIL;
        reverse = false;
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

        if (dirty < inf) {
            left.modify(dirty);
            right.modify(dirty);
            dirty = inf;
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