package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class FMobilePhoneNetwork {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int m = in.readInt();
        LCTNode[] nodes = new LCTNode[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new LCTNode();
        }
        LCTNode[] edges = new LCTNode[k];
        for (int i = 0; i < k; i++) {
            edges[i] = new LCTNode();
            LCTNode a = nodes[in.readInt()];
            LCTNode b = nodes[in.readInt()];
            LCTNode.join(a, edges[i]);
            LCTNode.join(b, edges[i]);
        }

        for (int i = 0; i < m; i++) {
            LCTNode a = nodes[in.readInt()];
            LCTNode b = nodes[in.readInt()];
            int w = in.readInt();

            LCTNode.makeRoot(a);
            LCTNode.access(b);
            if (LCTNode.findRoot(b) != a) {
                LCTNode.join(a, b);
            } else {
                LCTNode.splay(a);
                a.setMin(w);
            }
        }

        long ans = 0;
        for (int i = 0; i < k; i++) {
            LCTNode.access(edges[i]);
            LCTNode.splay(edges[i]);
            if (edges[i].limit > 1e9) {
                out.println(-1);
                return;
            }
            ans += edges[i].limit;
        }

        out.println(ans);
    }
}


/**
 * Created by dalt on 2018/5/20.
 */
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

    static final int INF = (int) 2e9;
    int limit = INF;
    int dirty = INF;

    public void setMin(int x) {
        dirty = Math.min(x, dirty);
        limit = Math.min(limit, x);
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
        if (dirty < INF) {
            left.setMin(dirty);
            right.setMin(dirty);
            dirty = INF;
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
    }
}
