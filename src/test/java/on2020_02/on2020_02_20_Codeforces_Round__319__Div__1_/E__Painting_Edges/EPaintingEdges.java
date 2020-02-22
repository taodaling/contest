package on2020_02.on2020_02_20_Codeforces_Round__319__Div__1_.E__Painting_Edges;



import template.datastructure.XorDeltaDSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SequenceUtils;

public class EPaintingEdges {
    void no(FastOutput out) {
        out.println("NO");
    }

    void yes(FastOutput out) {
        out.println("YES");
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        int q = in.readInt();

        Edge[] edges = new Edge[m + 1];
        for (int i = 1; i <= m; i++) {
            edges[i] = new Edge();
            edges[i].a = in.readInt();
            edges[i].b = in.readInt();
        }

        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            qs[i] = new Query();
            qs[i].e = in.readInt();
            qs[i].c = in.readInt();
        }
        int[] registries = new int[m + 1];
        SequenceUtils.deepFill(registries, q);
        for (int i = q - 1; i >= 0; i--) {
            qs[i].endTime = registries[qs[i].e];
            registries[qs[i].e] = i;
        }

        IntegerList[] list = new IntegerList[k + 1];
        for (int i = 1; i <= k; i++) {
            list[i] = new IntegerList();
        }
        for (Query query : qs) {
            list[query.c].add(edges[query.e].a);
            list[query.c].add(edges[query.e].b);
        }
        for (int i = 1; i <= k; i++) {
            list[i].unique();
        }
        ConnectionChecker[] checks = new ConnectionChecker[k + 1];
        for (int i = 1; i <= k; i++) {
            checks[i] = new ConnectionChecker(list[i].size());
        }
        for (int i = 0; i < q; i++) {
            Edge e = edges[qs[i].e];
            int c = qs[i].c;
            checks[c].elapse(i);
            int len = checks[c].check(list[c].binarySearch(e.a), list[c].binarySearch(e.b));
            if (len >= 0 && len % 2 == 1) {
                no(out);
            } else {
                yes(out);
                e.c = c;
            }
            if (e.c != 0) {
                checks[e.c].addEdge(list[e.c].binarySearch(e.a), list[e.c].binarySearch(e.b), qs[i].endTime);
            }
        }
    }
}

class Edge {
    int a;
    int b;
    int c;
}

class Query {
    int e;
    int c;
    int endTime;
}

class ConnectionChecker {
    private LCTNode[] nodes;
    private int time = -1;

    public ConnectionChecker(int n) {
        nodes = new LCTNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LCTNode();
            nodes[i].id = i;
            nodes[i].dieTime = Integer.MAX_VALUE;
            nodes[i].w = 1;
            nodes[i].pushUp();
        }
        for (int i = 1; i < n; i++) {
            LCTNode node = new LCTNode();
            node.dieTime = time;
            node.a = nodes[i - 1];
            node.b = nodes[i];
            node.pushUp();
            LCTNode.join(node.a, node);
            LCTNode.join(node.b, node);
        }
    }

    /**
     * 增加一条有效期截止到dieTime的边
     */
    public void addEdge(int aId, int bId, int dieTime) {
        LCTNode a = nodes[aId];
        LCTNode b = nodes[bId];
        LCTNode.findRoute(a, b);
        LCTNode.splay(a);
        if (a.eldest.dieTime >= dieTime) {
            return;
        }
        LCTNode eldest = a.eldest;
        LCTNode.splay(eldest);
        LCTNode.cut(eldest.a, eldest);
        LCTNode.cut(eldest.b, eldest);

        LCTNode node = new LCTNode();
        node.dieTime = dieTime;
        node.a = a;
        node.b = b;
        node.pushUp();
        LCTNode.join(node.a, node);
        LCTNode.join(node.b, node);
    }

    /**
     * 检查两个顶点之间是否存在一条路径
     */
    public int check(int aId, int bId) {
        LCTNode a = nodes[aId];
        LCTNode b = nodes[bId];
        LCTNode.findRoute(a, b);
        LCTNode.splay(b);
        return b.eldest.dieTime > time ? b.sum : -1;
    }


    public void elapse(int t) {
        time = t;
    }

    private static class LCTNode {
        public static final LCTNode NIL = new LCTNode();

        static {
            NIL.left = NIL;
            NIL.right = NIL;
            NIL.father = NIL;
            NIL.treeFather = NIL;
            NIL.dieTime = Integer.MAX_VALUE;
            NIL.eldest = NIL;
        }

        LCTNode left = NIL;
        LCTNode right = NIL;
        LCTNode father = NIL;
        LCTNode treeFather = NIL;
        boolean reverse;
        int id;
        int sum = 0;
        int w = 0;

        LCTNode a;
        LCTNode b;
        LCTNode eldest;
        int dieTime;

        public static LCTNode elder(LCTNode a, LCTNode b) {
            return a.dieTime < b.dieTime ? a : b;
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
            eldest = elder(this, left.eldest);
            eldest = elder(eldest, right.eldest);
            sum = left.sum + right.sum + w;
        }
    }
}