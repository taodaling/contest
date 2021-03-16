package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Power;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MaximumTopologicalSort {
    void dfs(Node root, Node p) {
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            root.size += node.size;
        }
    }

    int n;
    List<Node> centroids = new ArrayList<>(2);

    void dfsForCentroid(Node root, Node p) {
        int maxSize = n - root.size;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            maxSize = Math.max(maxSize, node.size);
            dfsForCentroid(node, root);
        }
        if (maxSize * 2 <= n) {
            centroids.add(root);
        }
    }

    void dfsForDp(Node root, Node p) {
        root.way = 1;
        int size = 0;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForDp(node, root);
            size += node.size;
            root.way = root.way * node.way % mod * comb.combination(size, node.size) % mod;
        }
    }

    int mod = (int) 1e9 + 7;
    Combination comb = new Combination((int) 1e6, mod);
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int k = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfs(nodes[0], null);
        centroids.clear();
        dfsForCentroid(nodes[0], null);
        centroids.sort(Comparator.<Node>comparingInt(x -> x.id).reversed());
        Node root = centroids.get(0);
        dfs(root, null);
        dfsForDp(root, null);
        Node choice = null;
        if (k == 1) {
            choice = root;
        } else {
            Node largest = null;
            for (Node node : root.adj) {
                if (largest == null || largest.size < node.size || largest.size == node.size && largest.id < node.id) {
                    largest = node;
                }
            }
            assert largest != null;
            long way = root.way * largest.size % mod * pow.inverse(n - largest.size) % mod;
            largest.way = way;
            choice = largest;
        }

        out.append(choice.id + 1).append(' ').append(choice.way).println();
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int id;
    int size;
    long way;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
