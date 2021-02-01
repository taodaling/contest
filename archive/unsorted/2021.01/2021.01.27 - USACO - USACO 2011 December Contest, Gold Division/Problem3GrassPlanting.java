package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class Problem3GrassPlanting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        LCTNode[] nodes = new LCTNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LCTNode();
        }
        for (int i = 0; i < n - 1; i++) {
            LCTNode a = nodes[in.ri() - 1];
            LCTNode b = nodes[in.ri() - 1];
            LCTNode e = new LCTNode();
            e.weight = 1;
            e.pushUp();
            LCTNode.join(a, e);
            LCTNode.join(b, e);
        }
        for (int i = 0; i < m; i++) {
            char c = in.rc();
            LCTNode a = nodes[in.ri() - 1];
            LCTNode b = nodes[in.ri() - 1];
            LCTNode.findRoute(a, b);
            LCTNode.splay(a);
            if (c == 'P') {
                a.modify(1);
            } else {
                long ans = a.grassSum;
                out.println(ans);
            }
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
    int weight;
    int weightSum;
    long grassPlant;
    long grassDirty;
    long grassSum;

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

    public void modify(long x) {
        if (this == NIL) {
            return;
        }
        this.grassDirty += x;
        this.grassPlant += weight * x;
        this.grassSum += weightSum * x;
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
        if (grassDirty != 0) {
            left.modify(grassDirty);
            right.modify(grassDirty);
            grassDirty = 0;
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
        grassSum = left.grassSum + right.grassSum + grassPlant;
        weightSum = left.weightSum + right.weightSum + weight;
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
