package on2021_03.on2021_03_25_Codeforces___Codeforces_Round__236__Div__1_.E__Two_Rooted_Trees;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.LongBinaryOperator;

public class ETwoRootedTrees {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Edge[] blueE = new Edge[n - 1];
        Edge[] redE = new Edge[n - 1];
        HeavyLightDecompose blueT = new HeavyLightDecompose(n, 0);
        HeavyLightDecompose redT = new HeavyLightDecompose(n, 0);

        for (int i = 0; i < n - 1; i++) {
            Edge e = new Edge();
            e.a = in.ri() - 1;
            e.b = i + 1;
            e.id = i;
            blueE[i] = e;
            blueT.addEdge(e.a, e.b);
        }

        for (int i = 0; i < n - 1; i++) {
            Edge e = new Edge();
            e.a = in.ri() - 1;
            e.b = i + 1;
            e.id = i;
            redE[i] = e;
            redT.addEdge(e.a, e.b);
        }

        blueT.finish();
        redT.finish();

        for (Edge e : redE) {
            blueT.processPath(e.a, e.b, e);
        }
        for (Edge e : blueE) {
            redT.processPath(e.a, e.b, e);
        }

        List<Edge> dq = new ArrayList<>(n * 20);
        List<Edge> buf = new ArrayList<>(n * 20);
        dq.add(blueE[in.ri() - 1]);
        dq.get(0).visited = true;
        boolean blue = true;
        IntegerArrayList sorted = new IntegerArrayList(n);
        while (!dq.isEmpty()) {
            buf.clear();
            sorted.clear();
            if (blue) {
                out.println("Blue");
                for (Edge e : dq) {
                    sorted.add(e.id);
                    int child = blueT.child(e);
                    blueT.consume(child, buf);
                }
            } else {
                out.println("Red");
                for (Edge e : dq) {
                    sorted.add(e.id);
                    int child = redT.child(e);
                    redT.consume(child, buf);
                }
            }
            sorted.sort();
            for(int i = 0; i < sorted.size(); i++){
                out.append(sorted.get(i) + 1).append(' ');
            }
            out.println();
            blue = !blue;
            List<Edge> tmp = dq;
            dq = buf;
            buf = tmp;
        }
    }
}

class Edge {
    int a;
    int b;
    int id;
    boolean visited;
}

class HeavyLightDecompose {

    public static class Segment implements Cloneable {
        private Segment left;
        private Segment right;
        private List<Edge> edges = new ArrayList<>();

        public void pushUp() {
        }

        public void modify(Edge e) {
            edges.add(e);
        }

        public void pushDown() {
        }

        public Segment(int l, int r) {
            if (l < r) {
                int m = DigitUtils.floorAverage(l, r);
                left = new Segment(l, m);
                right = new Segment(m + 1, r);
                pushUp();
            } else {
            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, Edge x) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                modify(x);
                return;
            }
            pushDown();
            int m = DigitUtils.floorAverage(l, r);
            left.update(ll, rr, l, m, x);
            right.update(ll, rr, m + 1, r, x);
            pushUp();
        }

        public void query(int ll, int rr, int l, int r, List<Edge> consumer) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (!edges.isEmpty()) {
                for(Edge e : edges){
                    if(e.visited){
                        continue;
                    }
                    e.visited = true;
                    consumer.add(e);
                }
                edges.clear();
            }
            if (l == r) {
                return;
            }
            pushDown();
            int m = DigitUtils.floorAverage(l, r);
            left.query(ll, rr, l, m, consumer);
            right.query(ll, rr, m + 1, r, consumer);
        }

        private void toString(StringBuilder builder) {
            if (left == null && right == null) {
                builder.append("val").append(",");
                return;
            }
            pushDown();
            left.toString(builder);
            right.toString(builder);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            deepClone().toString(builder);
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
            return builder.toString();
        }

        private Segment deepClone() {
            Segment seg = clone();
            if (seg.left != null) {
                seg.left = seg.left.deepClone();
            }
            if (seg.right != null) {
                seg.right = seg.right.deepClone();
            }
            return seg;
        }

        @Override
        protected Segment clone() {
            try {
                return (Segment) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class HLDNode {
        List<HLDNode> next = new ArrayList<>(2);
        int id;
        int dfsOrderFrom;
        int dfsOrderTo;
        int size;
        HLDNode link;
        HLDNode heavy;
        HLDNode father;

        @Override
        public String toString() {
            return "" + id;
        }
    }

    public int child(Edge e) {
        HLDNode a = nodes[e.a];
        HLDNode b = nodes[e.b];
        return a.father == b ? a.id : b.id;
    }

    public HeavyLightDecompose(int n, int rootId) {
        this.n = n;
        nodes = new HLDNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new HLDNode();
            nodes[i].id = i;
        }
        root = nodes[rootId];
    }

    public void addEdge(int a, int b) {
        nodes[a].next.add(nodes[b]);
        nodes[b].next.add(nodes[a]);
    }


    public void finish() {
        dfs(root, null);
        dfs2(root, root);
        segIndexToNode = new HLDNode[n + 1];
        for (int i = 0; i < n; i++) {
            segIndexToNode[nodes[i].dfsOrderFrom] = nodes[i];
        }
        segment = new Segment(1, n);
    }

    public void consume(int nodeId, List<Edge> consumer) {
        HLDNode node = nodes[nodeId];
        segment.query(node.dfsOrderFrom, node.dfsOrderFrom, 1, n, consumer);
    }

    public void processPath(int uId, int vId, Edge e) {
        HLDNode u = nodes[uId];
        HLDNode v = nodes[vId];
        while (u != v) {
            if (u.link == v.link) {
                if (u.size > v.size) {
                    HLDNode tmp = u;
                    u = v;
                    v = tmp;
                }
                segment.update(v.dfsOrderFrom + 1, u.dfsOrderFrom, 1, n, e);
                u = v;
            } else {
                if (u.link.size > v.link.size) {
                    HLDNode tmp = u;
                    u = v;
                    v = tmp;
                }
                segment.update(u.link.dfsOrderFrom, u.dfsOrderFrom, 1, n, e);
                u = u.link.father;
            }
        }
    }

    private static void dfs(HLDNode root, HLDNode father) {
        root.size = 1;
        root.father = father;
        for (HLDNode node : root.next) {
            if (node == father) {
                continue;
            }
            dfs(node, root);
            root.size += node.size;
            if (root.heavy == null || root.heavy.size < node.size) {
                root.heavy = node;
            }
        }
    }

    private void dfs2(HLDNode root, HLDNode link) {
        root.dfsOrderFrom = order++;
        root.link = link;
        if (root.heavy != null) {
            dfs2(root.heavy, link);
        }
        for (HLDNode node : root.next) {
            if (node == root.father || node == root.heavy) {
                continue;
            }
            dfs2(node, node);
        }
        root.dfsOrderTo = order - 1;
    }

    int n;
    int order = 1;
    HLDNode root;
    HLDNode[] nodes;
    HLDNode[] segIndexToNode;
    Segment segment;
}