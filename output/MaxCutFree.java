import java.util.List;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ArrayList;

public class MaxCutFree {
    Deque<Node> dq;
    int dfn = 0;

    public int solve(int n, int[] a, int[] b) {
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        int m = a.length;
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = nodes[a[i]];
            edges[i].b = nodes[b[i]];
            edges[i].a.adj.add(edges[i]);
            edges[i].b.adj.add(edges[i]);
        }

        dq = new ArrayDeque<>(n);
        for (Node node : nodes) {
            tarjan(node, null);
            dfsForSide(node, 0);
        }

        HungaryAlgo algo = new HungaryAlgo(n, n);
        for (Edge e : edges) {
            if (e.a.side == e.b.side) {
                continue;
            }
            if (e.a.side == 1) {
                Node tmp = e.a;
                e.a = e.b;
                e.b = tmp;
            }
            algo.addEdge(e.a.id, e.b.id, true);
        }

        int match = 0;
        for (int i = 0; i < n; i++) {
            match += algo.matchLeft(i) ? 1 : 0;
        }

        int ans = n - match;
        return ans;
    }

    public void dfsForSide(Node root, int parity) {
        if (root.visited) {
            return;
        }
        root.visited = true;
        root.side = parity;
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node.set != root.set) {
                dfsForSide(node, parity ^ 1);
            } else {
                dfsForSide(node, parity);
            }
        }
    }

    public void tarjan(Node root, Edge p) {
        if (root.dfn != 0) {
            return;
        }
        root.instk = true;
        root.low = root.dfn = ++dfn;
        dq.addLast(root);
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            tarjan(node, e);
            if (node.instk) {
                root.low = Math.min(root.low, node.low);
            }
        }

        if (root.low == root.dfn) {
            while (true) {
                Node tail = dq.removeLast();
                tail.instk = false;
                tail.set = root;
                if (tail == root) {
                    break;
                }
            }
        }
    }

}

class Node {
    List<Edge> adj = new ArrayList<>();
    Node set;
    int dfn;
    boolean instk;
    int id;
    int low;
    int side;
    boolean visited;

}

class HungaryAlgo {
    HungaryAlgo.Node[] leftSides;
    HungaryAlgo.Node[] rightSides;
    int version;

    public HungaryAlgo(int l, int r) {
        leftSides = new HungaryAlgo.Node[l];
        for (int i = 0; i < l; i++) {
            leftSides[i] = new HungaryAlgo.Node();
            leftSides[i].id = i;
            leftSides[i].leftSide = true;
        }
        rightSides = new HungaryAlgo.Node[r];
        for (int i = 0; i < r; i++) {
            rightSides[i] = new HungaryAlgo.Node();
            rightSides[i].id = i;
        }
    }

    public void addEdge(int lId, int rId, boolean urge) {
        leftSides[lId].nodes.add(rightSides[rId]);
        rightSides[rId].nodes.add(leftSides[lId]);
        if (urge && leftSides[lId].partner == null && rightSides[rId].partner == null) {
            leftSides[lId].partner = rightSides[rId];
            rightSides[rId].partner = leftSides[lId];
        }
    }

    private void prepare() {
        version++;
    }

    public boolean matchLeft(int id) {
        if (leftSides[id].partner != null) {
            return true;
        }
        prepare();
        return findPartner(leftSides[id]);
    }

    private boolean findPartner(HungaryAlgo.Node src) {
        if (src.visited == version) {
            return false;
        }
        src.visited = version;
        for (HungaryAlgo.Node node : src.nodes) {
            if (!tryRelease(node)) {
                continue;
            }
            node.partner = src;
            src.partner = node;
            return true;
        }
        return false;
    }

    private boolean tryRelease(HungaryAlgo.Node src) {
        if (src.visited == version) {
            return false;
        }
        src.visited = version;
        if (src.partner == null) {
            return true;
        }
        if (findPartner(src.partner)) {
            src.partner = null;
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < leftSides.length; i++) {
            if (leftSides[i].partner == null) {
                continue;
            }
            builder.append(leftSides[i].id).append(" - ").append(leftSides[i].partner.id).append("\n");
        }
        return builder.toString();
    }

    public static class Node {
        List<HungaryAlgo.Node> nodes = new ArrayList<>();
        int visited;
        HungaryAlgo.Node partner;
        int id;
        boolean leftSide;

        public String toString() {
            return "" + id;
        }

    }

}

class Edge {
    Node a;
    Node b;

    Node other(Node x) {
        return a == x ? b : a;
    }

}
