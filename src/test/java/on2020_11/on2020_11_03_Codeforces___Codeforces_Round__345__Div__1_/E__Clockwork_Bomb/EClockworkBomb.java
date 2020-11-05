package on2020_11.on2020_11_03_Codeforces___Codeforces_Round__345__Div__1_.E__Clockwork_Bomb;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public class EClockworkBomb {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] old = new int[2][n - 1];
        int[][] right = new int[2][n - 1];


        Set<Long> oldSet = new HashSet<>();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < 2; j++) {
                old[j][i] = in.readInt() - 1;
            }
            oldSet.add(key(old[0][i], old[1][i]));
        }
        Set<Long> rightSet = new HashSet<>();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < 2; j++) {
                right[j][i] = in.readInt() - 1;
            }
            rightSet.add(key(right[0][i], right[1][i]));
        }
        Set<Long> addSet = new HashSet<>();
        for (Long key : rightSet) {
            if (oldSet.contains(key)) {
                continue;
            }
            addSet.add(key);
        }

        LCTNode[] nodes = new LCTNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LCTNode();
            nodes[i].id = i;
        }

        for (int i = 0; i < n - 1; i++) {
            int u = old[0][i];
            int v = old[1][i];
            LCTNode e = new LCTNode();
            e.u = u;
            e.v = v;
            e.notExist = !rightSet.contains(key(u, v));
            e.pushUp();
            LCTNode.join(nodes[u], e);
            LCTNode.join(nodes[v], e);
        }

        out.println(addSet.size());
        for (Long xy : addSet) {
            int u = DigitUtils.highBit(xy);
            int v = DigitUtils.lowBit(xy);
            LCTNode.findRoute(nodes[u], nodes[v]);
            LCTNode.splay(nodes[u]);
            LCTNode deleted = nodes[u].notExistNode;
            out.append(deleted.u + 1).append(' ').append(deleted.v + 1).append(' ').append(u + 1).append(' ').append(v + 1).println();
            LCTNode.cut(nodes[deleted.u], deleted);
            LCTNode.cut(nodes[deleted.v], deleted);
            deleted.u = u;
            deleted.v = v;
            deleted.notExist = false;
            deleted.pushUp();

            LCTNode.join(nodes[u], deleted);
            LCTNode.join(nodes[v], deleted);
        }
    }

    public long key(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return DigitUtils.asLong(a, b);
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
    boolean notExist;
    LCTNode notExistNode;
    int u;
    int v;

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
        notExistNode = null;
        if (notExist) {
            notExistNode = this;
        }
        if (notExistNode == null) {
            notExistNode = left.notExistNode;
        }
        if (notExistNode == null) {
            notExistNode = right.notExistNode;
        }
    }
}
