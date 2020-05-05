package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EArkadyAndANobodyMen {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].node = new LCTNode();
            nodes[i].node.pushUp();
        }

        int[] p = new int[n];
        Node root = null;
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt() - 1;
            if (p[i] == -1) {
                root = nodes[i];
            } else {
                nodes[p[i]].next.add(nodes[i]);
                LCTNode.join(nodes[p[i]].node, nodes[i].node);
            }
        }

        dfs(root, 1);
        Node[] sorted = nodes.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a.depth, b.depth));

        LCTNode.makeRoot(root.node);
        for (int i = 0; i < n; i++) {
            int r = i;
            while (r + 1 < n && sorted[r + 1].depth == sorted[i].depth) {
                r++;
            }
            for (int j = i; j <= r; j++) {
                LCTNode.access(sorted[j].node);
                LCTNode.splay(root.node);
                root.node.modify(1);
            }
            for (int j = i; j <= r; j++) {
                LCTNode.access(sorted[j].node);
                LCTNode.splay(root.node);
                sorted[j].ans = root.node.sum - sorted[j].depth;
            }
            i = r;
        }

        for (Node node : nodes) {
            out.append(node.ans).append(' ');
        }
    }

    public void dfs(Node root, int d) {
        root.depth = d;
        for (Node node : root.next) {
            dfs(node, d + 1);
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int depth;
    long ans;

    int id;
    LCTNode node;

    @Override
    public String toString() {
        return "" + id;
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
    int w;
    long sum;
    int dirty;
    int size;

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

    public void modify(int x) {
        if (this == NIL) {
            return;
        }
        dirty += x;
        w += x;
        sum += (long) x * size;
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

        if (dirty != 0) {
            left.modify(dirty);
            right.modify(dirty);
            dirty = 0;
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
        sum = left.sum + right.sum + w;
        size = left.size + right.size + 1;
    }
}
