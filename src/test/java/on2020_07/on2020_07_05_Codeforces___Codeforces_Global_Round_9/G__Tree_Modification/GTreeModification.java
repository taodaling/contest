package on2020_07.on2020_07_05_Codeforces___Codeforces_Global_Round_9.G__Tree_Modification;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class GTreeModification {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        int[] cnts = new int[2];
        dfs(nodes[0], null, 0);
        for(Node node : nodes){
            cnts[node.depth % 2]++;
        }

        int ans = Math.min(cnts[0] - 1, cnts[1] - 1);
        out.println(ans);
    }

    public void dfs(Node root, Node p, int d){
        root.depth = d;
        for(Node node : root.adj){
            if(node == p){
                continue;
            }
            dfs(node, root, d + 1);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int depth;
}
