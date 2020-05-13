package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

import java.util.ArrayList;
import java.util.List;

public class BLehaAndAnotherGameAboutGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node root = null;
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].d = in.readInt();
            nodes[i].id = i;

            if (nodes[i].d == -1) {
                root = nodes[i];
            } else {
                nodes[i].tag = nodes[i].d;
            }
        }
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            edges[i].a = a;
            edges[i].b = b;
            edges[i].tag = 0;
            a.next.add(edges[i]);
            b.next.add(edges[i]);
        }

        if (root == null) {
            root = nodes[0];
        }

        dfs(root, null);
        if(root.d != -1 && root.tag != 0){
            out.println(-1);
            return;
        }

        IntegerList ans = new IntegerList(m);
        for(int i = 0; i < m; i++){
            if(edges[i].tag == 1){
                ans.add(i);
            }
        }
        out.println(ans.size());
        for(int i = 0; i < ans.size(); i++){
            out.println(ans.get(i) + 1);
        }
    }

    public void dfs(Node root, Edge p) {
        if (root.visited) {
            return;
        }
        root.visited = true;
        for (Edge e : root.next) {
            Node node = e.other(root);
            if (e == p) {
                continue;
            }
            dfs(node, e);
            root.tag ^= e.tag;
        }
        if (p != null) {
            p.tag ^= root.tag;
            root.tag = 0;
        }
    }
}

class Edge {
    Node a;
    Node b;
    int tag;

    Node other(Node x) {
        return x == a ? b : a;
    }
}

class Node {
    int d;
    int tag;
    boolean visited;
    List<Edge> next = new ArrayList<>();

    int id;
    @Override
    public String toString() {
        return "" + id;
    }
}