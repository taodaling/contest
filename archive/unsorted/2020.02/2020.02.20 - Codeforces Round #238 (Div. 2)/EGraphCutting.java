package contest;

import template.graph.LcaOnTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class EGraphCutting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Edge e = new Edge();
            e.a = nodes[in.readInt()];
            e.b = nodes[in.readInt()];
            e.a.next.add(e);
            e.b.next.add(e);
            e.a.xor ^= 1;
            e.toA = 1;
        }
        for (int i = 1; i <= n; i++) {
            if (nodes[i].visited) {
                continue;
            }
            dfs(nodes[i], null);
        }

        List<int[]> solutions = new ArrayList<>(m / 2);
        for (int i = 1; i <= n; i++) {
            Node node = nodes[i];
            Edge remain = null;
            for (Edge e : node.next) {
                if (e.toWhich() != node) {
                    continue;
                }
                if (remain == null) {
                    remain = e;
                } else {
                    solutions.add(SequenceUtils.wrapArray(
                            remain.other(node).id, node.id, e.other(node).id));
                    remain = null;
                }
            }
            if (remain != null) {
                out.println("No solution");
                return;
            }
        }
        for (int[] s : solutions) {
            for (int x : s) {
                out.append(x).append(' ');
            }
            out.println();
        }
    }

    static void dfs(Node root, Edge p) {
        root.visited = true;
        for (Edge e : root.next) {
            Node node = e.other(root);
            if (node.visited) {
                continue;
            }
            dfs(node, e);
            root.xor ^= node.xor;
        }
        if (p != null) {
            p.toA ^= root.xor;
        }
    }
}

class Edge {
    Node a;
    Node b;
    int toA;

    Node other(Node x) {
        return a == x ? b : a;
    }

    Node toWhich() {
        return toA == 1 ? a : b;
    }
}

class Node {
    int id;
    int xor;
    boolean visited;
    List<Edge> next = new ArrayList<>();
}

