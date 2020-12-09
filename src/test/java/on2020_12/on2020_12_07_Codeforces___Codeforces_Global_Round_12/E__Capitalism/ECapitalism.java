package on2020_12.on2020_12_07_Codeforces___Codeforces_Global_Round_12.E__Capitalism;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class ECapitalism {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            int d = in.ri();
            a.adj.add(b);
            if (d == 0) {
                b.adj.add(a);
            }
            a.bd.add(b);
            b.bd.add(a);
            edges[i] = new Edge();
            edges[i].a = a;
            edges[i].b = b;
        }

        if(!isBipartite(nodes[0], 0)){
            out.println("NO");
            return;
        }

        int[][] dist = new int[n][n];
        int inf = (int)1e8;
        SequenceUtils.deepFill(dist, inf);
        for(int i = 0; i < n; i++){
            for(Node node : nodes[i].adj){
                dist[i][node.id] = 1;
            }
        }
        for(int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }
        int a = 0;
        int b = 1;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                
            }
        }
    }

    public boolean isBipartite(Node root, int c) {
        if (root.color != -1) {
            return root.color == c;
        }
        root.color = c;
        for (Node node : root.bd) {
            if(!isBipartite(node, c ^ 1)){
                return false;
            }
        }
        return true;
    }
}

class Edge {
    Node a;
    Node b;
}

class Node {
    int id;
    List<Node> adj = new ArrayList<>();
    List<Node> bd = new ArrayList<>();
    int color = -1;
}
