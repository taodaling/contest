package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class FlightRouteRequests {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            Node.merge(a, b);
        }
        for (Node node : nodes) {
            if (dfs(node)) {
                node.find().circle = true;
            }
        }

        int ans = 0;
        for(Node node : nodes){
            if(node.find() != node){
                continue;
            }
            ans += node.size - 1;
            if(node.circle){
                ans++;
            }
        }

        out.println(ans);
    }

    public static boolean dfs(Node root) {
        if (root.visited) {
            return root.instk;
        }
        root.visited = root.instk = true;
        for (Node node : root.adj) {
            if (dfs(node)) {
                root.instk = false;
                return true;
            }
        }
        root.instk = false;
        return false;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    boolean visited;
    boolean instk;
    boolean circle;
    int size = 1;

    Node p = this;
    int rank;

    Node find() {
        return (p.p == p) ? p : (p = p.find());
    }

    static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank > b.rank) {
            b.p = a;
            a.size += b.size;
        } else {
            a.p = b;
            b.size += a.size;
        }
    }
}