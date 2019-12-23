package on2019_12.on2019_12_23_CodeCraft_19_and_Codeforces_Round__537__Div__2_.E__Tree;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

import java.util.Arrays;

public class ETree {
    Modular mod = new Modular(1e9 + 7);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();

        LCTNode[] nodes = new LCTNode[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new LCTNode();
        }

        for (int i = 1; i < n; i++) {
            LCTNode a = nodes[in.readInt()];
            LCTNode b = nodes[in.readInt()];
            LCTNode.join(a, b);
        }


        for (int i = 1; i <= q; i++) {
            int k = in.readInt();
            int m = in.readInt();
            int r = in.readInt();
            LCTNode.makeRoot(nodes[r]);
            Node[] qs = new Node[k];
            for (int j = 0; j < k; j++) {
                qs[j] = new Node();
                qs[j].lctNode = nodes[in.readInt()];
                LCTNode.splay(qs[j].lctNode);
                qs[j].lctNode.val = 1;
                qs[j].lctNode.pushUp();
            }

            for (int j = 0; j < k; j++) {
                LCTNode.access(qs[j].lctNode);
                LCTNode.splay(qs[j].lctNode);
                qs[j].p = qs[j].lctNode.sum - 1;
            }

            Arrays.sort(qs, (a, b) -> Integer.compare(a.p, b.p));
            int[] dp = new int[m + 1];
            dp[0] = 1;
            for (int j = 1; j <= k; j++) {
                int p = qs[j - 1].p;
                for (int t = m; t >= 0; t--) {
                    if (t > p) {
                        dp[t] = mod.plus(mod.mul(dp[t], t - p), dp[t - 1]);
                    } else {
                        dp[t] = 0;
                    }
                }
            }

            int ans = 0;
            for (int j = 0; j <= m; j++) {
                ans = mod.plus(dp[j], ans);
            }
            out.println(ans);

            for (int j = 0; j < k; j++) {
                LCTNode.splay(qs[j].lctNode);
                qs[j].lctNode.val = 0;
                qs[j].lctNode.pushUp();
            }
        }
    }
}

class Node {
    int p;
    LCTNode lctNode;
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
    int val;
    int sum;

    public void setVal(int v) {
        if (this == NIL) {
            return;
        }
        this.val = v;
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
        if (this == NIL) {
            return;
        }
        sum = left.sum + right.sum + val;
    }
}
