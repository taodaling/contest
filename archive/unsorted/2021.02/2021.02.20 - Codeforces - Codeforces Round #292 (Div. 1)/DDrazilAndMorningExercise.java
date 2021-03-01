package contest;

import template.datastructure.DSU;
import template.datastructure.LCTNode;
import template.datastructure.Range2DequeAdapter;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongWeightDirectedEdge;
import template.primitve.generated.graph.LongWeightGraph;
import template.primitve.generated.graph.LongWeightUndirectedEdge;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DDrazilAndMorningExercise {

    public void dfs(Node root, Edge p) {
        root.dist = 0;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            dfs(node, e);
            root.dist = Math.max(root.dist, node.dist + e.w);
        }
    }

    public void dfs1(Node root, Edge p, long fromTop) {
        root.dist = Math.max(root.dist, fromTop);
        int m = root.adj.size();
        long[] preMax = new long[m];
        for (int i = 0; i < m; i++) {
            Edge e = root.adj.get(i);
            if (e == p) {
                continue;
            }
            preMax[i] = e.w + e.other(root).dist;
        }
        long[] postMax = preMax.clone();
        for (int i = 1; i < m; i++) {
            preMax[i] = Math.max(preMax[i - 1], preMax[i]);
        }
        for (int i = m - 2; i >= 0; i--) {
            postMax[i] = Math.max(postMax[i + 1], postMax[i]);
        }
        for (int i = 0; i < m; i++) {
            Edge e = root.adj.get(i);
            if (e == p) {
                continue;
            }
            long cand = fromTop;
            if (i > 0) {
                cand = Math.max(cand, preMax[i - 1]);
            }
            if (i + 1 < m) {
                cand = Math.max(cand, postMax[i + 1]);
            }
            dfs1(e.other(root), e, cand + e.w);
        }
    }

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.w = in.ri();
            e.a.adj.add(e);
            e.b.adj.add(e);
        }
        dfs(nodes[0], null);
        dfs1(nodes[0], null, 0);

        for (int i = 0; i < n; i++) {
            debug.debug("i", i);
            debug.debug("dist", nodes[i].dist);
        }
        Node[] sorted = nodes.clone();
        Arrays.sort(sorted, Comparator.comparingLong(x -> x.dist));
        for (int i = 0; i < n; i++) {
            sorted[i].invId = i;
        }

        int q = in.ri();
        DSUExt dsu = new DSUExt(n);
        for (int i = 0; i < q; i++) {
            dsu.init();
            long l = in.rl();
            Range2DequeAdapter<Node> dq = new Range2DequeAdapter<>(j -> sorted[j], 0, n - 1);
            int best = 1;
            for (Node node : sorted) {
                while (!dq.isEmpty() && node.dist - dq.peekFirst().dist > l) {
                    Node head = dq.removeFirst();
                    for (Edge x : head.adj) {
                        Node to = x.other(head);
                        if (to.invId < node.invId && to.invId > head.invId) {
                            //added
                            dsu.size[dsu.find(to.id)]--;
                        }
                    }
                }
                for (Edge x : node.adj) {
                    Node to = x.other(node);
                    if (to.invId < node.invId && node.dist - to.dist <= l) {
                        dsu.merge(to.id, node.id);
                        best = Math.max(best, dsu.size[dsu.find(node.id)]);
                    }
                }
            }
            out.println(best);
        }
    }
}

class DSUExt extends DSU {
    public DSUExt(int n) {
        super(n);
        size = new int[n];
    }

    int[] size;

    @Override
    public void init(int n) {
        super.init(n);
        Arrays.fill(size, 1);
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        size[a] += size[b];
    }
}

class Edge {
    Node a;
    Node b;
    int w;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    int id;
    long dist;
    List<Edge> adj = new ArrayList<>();
    int invId;
}
