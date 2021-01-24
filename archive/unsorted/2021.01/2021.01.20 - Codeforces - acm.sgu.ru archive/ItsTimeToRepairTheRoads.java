package contest;

import template.algo.CommutativeUndoOperation;
import template.algo.OfflineUndoSegment;
import template.algo.UndoOperation;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class ItsTimeToRepairTheRoads {
    LCTNode[] nodes;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        nodes = new LCTNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LCTNode();
            nodes[i].weight = 0;
            nodes[i].pushUp();
        }
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = nodes[in.ri() - 1];
            edges[i].b = nodes[in.ri() - 1];
            edges[i].c = in.ri();
        }

        int q = in.ri();
        OfflineUndoSegment st = new OfflineUndoSegment(0, q - 1);
        for (int i = 0; i < q; i++) {
            Edge e = edges[in.ri() - 1];
            int c = in.ri();
            st.update(e.born, i - 1, 0, q - 1, new AddEdgeOp(e));
            e.c = c;
            e.born = i;
        }
        for (Edge e : edges) {
            st.update(e.born, q - 1, 0, q - 1, new AddEdgeOp(e));
        }
        st.solve(0, q - 1, i -> {
            out.println(sum);
        });
    }

    int sum = 0;

    class AddEdgeOp extends CommutativeUndoOperation {
        Edge edge;
        private LCTNode node;
        boolean added;
        Edge deletedEdge;
        LCTNode deletedNode;

        public AddEdgeOp(Edge edge) {
            this.edge = edge;
            node = new LCTNode();
            node.weight = edge.c;
            node.edge = edge;
            node.pushUp();
        }

        @Override
        public void apply() {
            added = false;
            deletedEdge = null;
            LCTNode.makeRoot(edge.a);
            LCTNode.access(edge.b);
            if (LCTNode.findRoot(edge.b) == edge.a) {
                LCTNode.splay(edge.b);
                if (edge.b.maxWeightNode.weight <= node.weight) {
                    return;
                }
                deletedEdge = edge.b.maxWeightNode.edge;
                deletedNode = edge.b.maxWeightNode;
                rawDeleteEdge(deletedEdge, deletedNode);
            }
            added = true;
            rawAddEdge(edge, node);
        }

        void rawDeleteEdge(Edge e, LCTNode node) {
            assert e != null;
            assert node != null;
            LCTNode.cut(e.a, node);
            LCTNode.cut(e.b, node);
            sum -= node.weight;
        }

        void rawAddEdge(Edge e, LCTNode node) {
            LCTNode.join(e.a, node);
            LCTNode.join(e.b, node);
            sum += node.weight;
        }

        @Override
        public void undo() {
            if (!added) {
                return;
            }
            rawDeleteEdge(edge, node);
            if (deletedEdge != null) {
                rawAddEdge(deletedEdge, deletedNode);
            }
        }
    }
}

class Edge {
    LCTNode a;
    LCTNode b;
    int c;
    int born;
}

class LCTNode {
    public static final LCTNode NIL = new LCTNode();

    static {
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.father = NIL;
        NIL.treeFather = NIL;
        NIL.maxWeightNode = NIL;
    }

    LCTNode left = NIL;
    LCTNode right = NIL;
    LCTNode father = NIL;
    LCTNode treeFather = NIL;
    Edge edge;
    boolean reverse;
    int id;
    /**
     * 所在连通块中的treeWeight之和
     */
    int treeSize;
    int vtreeSize;
    byte treeWeight;
    int weight;
    LCTNode maxWeightNode;

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
        maxWeightNode = this;
        if (left.maxWeightNode.weight > maxWeightNode.weight) {
            maxWeightNode = left.maxWeightNode;
        }
        if (right.maxWeightNode.weight > maxWeightNode.weight) {
            maxWeightNode = right.maxWeightNode;
        }
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
