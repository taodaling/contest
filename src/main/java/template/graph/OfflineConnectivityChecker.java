package template.graph;

public class OfflineConnectivityChecker {
    private LCTNode[] nodes;
    private int time = -1;

    public OfflineConnectivityChecker(int n) {
        nodes = new LCTNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LCTNode();
            nodes[i].id = i;
            nodes[i].dieTime = Integer.MAX_VALUE;
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
    public boolean check(int aId, int bId) {
        LCTNode a = nodes[aId];
        LCTNode b = nodes[bId];
        LCTNode.findRoute(a, b);
        LCTNode.splay(b);
        return b.eldest.dieTime > time;
    }


    public void elapse(int t) {
        if (t < time) {
            throw new IllegalArgumentException();
        }
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
        }
    }
}