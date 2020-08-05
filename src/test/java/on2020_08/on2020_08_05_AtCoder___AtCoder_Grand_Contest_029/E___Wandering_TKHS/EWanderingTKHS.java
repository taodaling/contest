package on2020_08.on2020_08_05_AtCoder___AtCoder_Grand_Contest_029.E___Wandering_TKHS;



import template.datastructure.LeftistTree;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EWanderingTKHS {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].lctNode = new LCTNode();
            nodes[i].lctNode.id = i;
            nodes[i].lctNode.pushUp();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);

            LCTNode.join(a.lctNode, b.lctNode);
        }

        for (Node node : nodes[0].adj) {
            dfs(node, nodes[0], nodes[0], node, nodes[0]);
        }
        solve(nodes[0], null, 0);

        for (int i = 1; i < n; i++) {
            out.println(nodes[i].cnt);
        }
    }

    public int maxIdBetween(Node a, Node b) {
        LCTNode.findRoute(a.lctNode, b.lctNode);
        LCTNode.splay(a.lctNode);
        return a.lctNode.maxId;
    }

    public void dfs(Node root, Node p, Node max, Node second, Node top) {
        root.p = p;
        if (max.id <= root.id) {
            root.add++;
        } else if (max.p != null && maxIdBetween(max.p, top) >=
                maxIdBetween(root, second)) {
            max.add++;
        } else {
            second.add++;
        }

        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            if (max.id > root.id) {
                dfs(node, root, max, second, top);
            } else {
                dfs(node, root, root, node, top);
            }
        }
    }

    public void solve(Node root, Node p, int cnt) {
        cnt += root.add;
        root.cnt = cnt;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            solve(node, root, cnt);
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
        NIL.maxId = NIL.id = -1;
    }

    LCTNode left = NIL;
    LCTNode right = NIL;
    LCTNode father = NIL;
    LCTNode treeFather = NIL;
    boolean reverse;
    int id;
    int maxId;

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
        maxId = Math.max(left.maxId, right.maxId);
        maxId = Math.max(id, maxId);
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int id;
    int cnt;
    int add;
    Node p;

    @Override
    public String toString() {
        return String.format("%d=%d", id, add);
    }

    LCTNode lctNode;
}

