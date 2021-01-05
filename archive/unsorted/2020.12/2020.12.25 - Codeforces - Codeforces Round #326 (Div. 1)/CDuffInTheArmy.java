package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

public class CDuffInTheArmy {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int q = in.ri();
        LCTNode[] nodes = new LCTNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LCTNode();
            nodes[i].id = i;
        }
        LCTNode[][] edges = new LCTNode[2][n - 1];
        for (int i = 0; i < n - 1; i++) {
            LCTNode u = nodes[in.ri() - 1];
            LCTNode v = nodes[in.ri() - 1];
            edges[0][i] = u;
            edges[1][i] = v;
        }
        debug.log("build tree");
        for (int i = 0; i < m; i++) {
            LCTNode u = nodes[in.ri() - 1];
            u.element.add(i);
        }
        for (int i = 0; i < n; i++) {
            nodes[i].element.sort();
            nodes[i].element.retain(10);
            nodes[i].pushUp();
        }
        for(int i = 0; i < n - 1; i++){
            LCTNode u = edges[0][i];
            LCTNode v = edges[1][i];
            LCTNode.join(u, v);
        }
        debug.log("sort");
        for (int i = 0; i < q; i++) {
            LCTNode u = nodes[in.ri() - 1];
            LCTNode v = nodes[in.ri() - 1];
            int a = in.ri();
            LCTNode.findRoute(u, v);
            LCTNode.splay(u);
            IntegerArrayList list = u.sorted;
            out.append(Math.min(a, list.size())).append(' ');
            for (int j = 0; j < a && j < list.size(); j++) {
                out.append(list.get(j) + 1).append(' ');
            }
            out.println();
        }
        debug.log("query");
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
    IntegerArrayList element = new IntegerArrayList();
    IntegerArrayList sorted = new IntegerArrayList(10);
    static IntegerArrayList buf1 = new IntegerArrayList(10);

    public static void merge(IntegerArrayList a, IntegerArrayList b, IntegerArrayList ans) {
        ans.clear();
        int ai = 0;
        int bi = 0;
        while (ans.size() < 10 && (ai < a.size() || bi < b.size())) {
            if (bi >= b.size() || (ai < a.size() && a.get(ai) <= b.get(bi))) {
                ans.add(a.get(ai++));
            } else {
                ans.add(b.get(bi++));
            }
        }
    }

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
        merge(left.sorted, right.sorted, buf1);
        merge(buf1, element, sorted);
    }
}
