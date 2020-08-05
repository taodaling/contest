package on2020_08.on2020_08_05_AtCoder___AtCoder_Regular_Contest_083.E___Bichrome_Tree;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EBichromeTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            Node p = nodes[in.readInt() - 1];
            p.adj.add(nodes[i]);
        }
        for (int i = 0; i < n; i++) {
            nodes[i].x = in.readInt();
        }

        dfs(nodes[0]);
        out.println(valid ? "POSSIBLE" : "IMPOSSIBLE");
    }

    boolean valid = true;
    int[] last = new int[5001];
    int[] next = new int[5001];
    int inf = (int) 1e9;

    public void dfs(Node root) {
        for (Node node : root.adj) {
            dfs(node);
        }
        Arrays.fill(last, inf);
        last[0] = 0;
        for (Node node : root.adj) {
            Arrays.fill(next, inf);
            for (int i = 0; i <= root.x; i++) {
                if (node.x + i <= root.x) {
                    next[node.x + i] = Math.min(next[node.x + i], last[i] + node.y);
                }
                if (node.y + i <= root.x) {
                    next[node.y + i] = Math.min(next[node.y + i], last[i] + node.x);
                }
            }
            int[] tmp = last;
            last = next;
            next = tmp;
        }
        for(int i = 1; i <= root.x; i++){
            last[i] = Math.min(last[i], last[i - 1]);
        }
        if (last[root.x] >= inf) {
            valid = false;
        }
        root.y = last[root.x];
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int x;
    int y;
}