package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class FNewYearAndSocialNetwork {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        LCTNode[] nodes = new LCTNode[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new LCTNode();
            nodes[i].id = (int) 1e7;
        }

        LCTNode[] edges = new LCTNode[n];
        int[][] t1 = new int[n][2];
        for (int i = 1; i < n; i++) {
            edges[i] = new LCTNode();
            edges[i].id = i;

            t1[i][0] = in.readInt();
            t1[i][1] = in.readInt();

            LCTNode.join(nodes[t1[i][0]], edges[i]);
            LCTNode.join(nodes[t1[i][1]], edges[i]);
        }

        out.println(n - 1);
        for (int i = 1; i < n; i++) {
            int a = in.readInt();
            int b = in.readInt();
            LCTNode.findRoute(nodes[a], nodes[b]);
            LCTNode.splay(nodes[a]);
            LCTNode replace = nodes[a].minIdNode;
            replace.pushUp();

            out.append(t1[replace.id][0]).append(' ').append(t1[replace.id][1])
                    .append(' ').append(a).append(' ').append(b).println();

            LCTNode.cut(replace, nodes[t1[replace.id][0]]);
            LCTNode.cut(replace, nodes[t1[replace.id][1]]);
            LCTNode.join(nodes[a], nodes[b]);
        }
    }
}

class LCTNode {
    public static final LCTNode NIL = new LCTNode();

    static {
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.father = NIL;
        NIL.treeFather = NIL;
        NIL.id = (int) 1e8;
        NIL.minIdNode = NIL;
    }

    LCTNode left = NIL;
    LCTNode right = NIL;
    LCTNode father = NIL;
    LCTNode treeFather = NIL;
    boolean reverse;
    int id;
    LCTNode minIdNode;

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
        if (this == NIL) {
            return;
        }
        minIdNode = this;
        if (this.left.minIdNode.id < minIdNode.id) {
            minIdNode = this.left.minIdNode;
        }
        if (this.right.minIdNode.id < minIdNode.id) {
            minIdNode = this.right.minIdNode;
        }
    }
}
