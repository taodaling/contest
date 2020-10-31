package on2020_10.on2020_10_29_CSES___CSES_Problem_Set.Round_Trip_II;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class RoundTripII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
        }
        for (int i = 0; i < n; i++) {
            if (dfs(nodes[i])) {
                out.println(dq.size());
                for (Node node : dq) {
                    out.append(node.id + 1).append(' ');
                }
                return;
            }
        }
        out.println("IMPOSSIBLE");
    }

    Deque<Node> dq = new ArrayDeque<>();

    public boolean dfs(Node root) {
        if (root.visited) {
            if (root.instk) {
                dq.addLast(root);
                while (dq.peekFirst() != root) {
                    dq.removeFirst();
                }
                return true;
            }
            return false;
        }
        root.visited = root.instk = true;
        dq.addLast(root);

        for (Node node : root.adj) {
            if (dfs(node)) {
                return true;
            }
        }

        dq.removeLast();
        root.instk = false;
        return false;
    }
}


class Node {
    List<Node> adj = new ArrayList<>();
    boolean instk;
    boolean visited;
    int id;
}