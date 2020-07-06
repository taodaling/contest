package on2020_07.on2020_07_07_AtCoder___AtCoder_Grand_Contest_024.D___Isomorphism_Freak;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DIsomorphismFreak {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        int[][] edges = new int[n - 1][2];
        for (int i = 0; i < n - 1; i++) {
            edges[i][0] = in.readInt() - 1;
            edges[i][1] = in.readInt() - 1;
            Node a = nodes[edges[i][0]];
            Node b = nodes[edges[i][1]];
            a.adj.add(b);
            b.adj.add(a);
        }
        seq = new int[n];


        minDepth = n;
        minLeaf = Long.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            reset();
            dfs(nodes[i], null, 0);
            update(1);
        }

        for (int[] e : edges) {
            Node a = nodes[e[0]];
            Node b = nodes[e[1]];
            reset();
            dfs(a, b, 0);
            dfs(b, a, 0);
            update(2);
        }

        out.println(minDepth + 1).println(minLeaf);
    }

    int[] seq;
    int depthest;

    int minDepth;
    long minLeaf;

    public void update(int mul) {
        int depth = depthest;
        long leaf = mul;
        for (int j = 0; j <= depth; j++) {
            leaf *= seq[j];
        }

        if (depth < minDepth) {
            minDepth = depth;
            minLeaf = Long.MAX_VALUE;
        }
        if (depth == minDepth) {
            minLeaf = Math.min(minLeaf, leaf);
        }
    }


    public void reset() {
        Arrays.fill(seq, 1);
        depthest = 0;
    }

    public void dfs(Node root, Node p, int d) {
        depthest = Math.max(depthest, d);
        int degree = root.adj.size() - (p == null ? 0 : 1);
        seq[d] = Math.max(seq[d], degree);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root, d + 1);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}