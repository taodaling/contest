package contest;

import template.datastructure.DSU;
import template.graph.DirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class TeleportersPath {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        DSU dsu = new DSU(n);
        dsu.init();
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.indeg++;
            dsu.merge(a.id, b.id);
        }
        boolean valid = true;
        for (int i = 0; i < n; i++) {
            if (nodes[i].indeg + nodes[i].adj.size() > 0 && dsu.find(i) != dsu.find(0)) {
                valid = false;
            }
        }
        if (nodes[0].adj.size() - nodes[0].indeg != 1) {
            valid = false;
        }
        if (nodes[n - 1].adj.size() - nodes[n - 1].indeg != -1) {
            valid = false;
        }
        for (int i = 1; i < n - 1; i++) {
            if(nodes[i].indeg != nodes[i].adj.size()){
                valid = false;
            }
        }
        if(!valid){
            out.println("IMPOSSIBLE");
            return;
        }
        List<Node> seq = new ArrayList<>(m + 1);
        dfs(nodes[0], seq);
        Collections.reverse(seq);
        for(Node node : seq){
            out.append(node.id + 1).append(' ');
        }
    }

    public static void dfs(Node root, List<Node> seq) {
        while(!root.adj.isEmpty()){
            Node tail = root.adj.remove(root.adj.size() - 1);
            dfs(tail, seq);
        }
        seq.add(root);
    }
}

class Node {
    int indeg;
    List<Node> adj = new ArrayList<>();
    int id;
}
