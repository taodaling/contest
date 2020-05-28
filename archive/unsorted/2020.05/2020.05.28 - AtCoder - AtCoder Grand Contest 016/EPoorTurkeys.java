package contest;


import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class EPoorTurkeys {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.time = i;
            a.adj.add(e);
            b.adj.add(e);
            edges[i] = e;
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                //init
                for (Node node : nodes) {
                    node.visited = false;
                    node.tag = -1;
                }
                valid = true;
                nodes[i].tag = nodes[j].tag = m;
                dfs(nodes[i]);
                dfs(nodes[j]);
                if (valid) {
                    ans++;
                }
            }
        }
        out.println(ans);
    }

    boolean valid;


    public void dfs(Node root) {
        if (root.visited) {
            return;
        }
        root.visited = true;
        for (Edge e : root.adj) {
            if (!valid) {
                break;
            }
            if (e.time >= root.tag) {
                break;
            }
            Node node = e.other(root);
            if (node.tag != -1 && node.tag != e.time) {
                valid = false;
            }
            node.tag = e.time;
            dfs(node);
        }
    }
}

class Edge {
    Node a;
    Node b;
    int time;

    public Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int tag;
    boolean visited;
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
