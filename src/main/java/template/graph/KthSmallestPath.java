package template.graph;

import java.util.*;

/**
 * 'Finding the k Shortest Paths' by 'David Eppstein'
 * source: http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.39.3901&rep=rep1&type=pdf
 */
public class KthSmallestPath {
    Node[] nodes;

    static final long inf = (long) 2e18;

    public KthSmallestPath(int n) {
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

    }

    /**
     * <pre>
     * Find the 1-th, 2-th, ... , k-th shortest path if exists
     * O(E\log_2E+k\log_2k)
     * </pre>
     *
     * @param k
     */
    public void solve(int k, int srcId, int dstId) {
        this.src = nodes[srcId];
        this.dst = nodes[dstId];
        //sp first
        for (Node node : nodes) {
            node.dist = inf;
            node.heap = null;
        }
        dst.dist = 0;
        TreeSet<Node> pq = new TreeSet<>(Comparator.<Node>comparingLong(x -> x.dist).thenComparingInt(x -> x.id));
        pq.add(dst);
        while (!pq.isEmpty()) {
            Node head = pq.pollFirst();
            for (Edge e : head.into) {
                Node to = e.from;
                long cand = e.len + head.dist;
                if (to.dist <= cand) {
                    continue;
                }
                pq.remove(to);
                to.dist = cand;
                to.prev = e;
                pq.add(to);
            }
        }

        if (src.dist >= inf) {
            states = Collections.emptyList();
            return;
        }
        //build shortest path tree
        for (Node node : nodes) {
            prepare(node);
        }

        states = KthSmallestSumOnTree.kthSmallestSumOnTree(src, k);
    }

    public long size() {
        return states.size();
    }

    /**
     * <pre>
     * Get the kth shortest path length, -1 means not exist
     * k start with 0
     * </pre>
     *
     * @param k
     * @return
     */
    public long getKthLength(int k) {
        if (states.size() <= k) {
            return -1;
        }
        return states.get(k).sum + src.dist;
    }

    private List<KthSmallestSumOnTree.State> states;
    private Node src;
    private Node dst;

    private PersistentLeftistTree<Edge> prepare(Node root) {
        if (root.heap == null) {
            root.heap = PersistentLeftistTree.NIL;
            for (Edge e : root.adj) {
                Node to = (Node) e.to;
                if (to.dist >= inf) {
                    continue;
                }
                if (e == root.prev) {
                    e.weight = 0;
                    root.heap = PersistentLeftistTree.merge(prepare(to), root.heap, Comparator.naturalOrder());
                } else {
                    //calc delta
                    e.weight = e.len + to.dist - root.dist;
                    root.heap = PersistentLeftistTree.merge(new PersistentLeftistTree<>(e), root.heap, Comparator.naturalOrder());
                }
            }
        }
        return root.heap;
    }

    public void addEdge(int a, int b, long w) {
        Edge e = new Edge();
        e.to = nodes[b];
        e.from = nodes[a];
        e.len = w;
        nodes[a].adj.add(e);
        nodes[b].into.add(e);
    }

    static class Edge extends KthSmallestSumOnTree.Edge implements Comparable<Edge> {
        long len;
        Node from;

        @Override
        public int compareTo(Edge o) {
            return Long.compare(weight, o.weight);
        }
    }

    static class Node implements KthSmallestSumOnTree.Vertex {
        PersistentLeftistTree<Edge> heap;
        Edge prev;
        List<Edge> adj = new ArrayList<>();
        List<Edge> into = new ArrayList<>();
        long dist;
        int id;

        @Override
        public Iterator<KthSmallestSumOnTree.Edge> children() {
            return (Iterator) PersistentLeftistTree.asIterator(heap, Comparator.naturalOrder());
        }

        @Override
        public String toString() {
            return "" + id;
        }
    }
}
