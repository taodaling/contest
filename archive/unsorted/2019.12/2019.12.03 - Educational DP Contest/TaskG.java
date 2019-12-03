package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class TaskG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n + 1];
        for(int i = 1; i <= n; i++){
            nodes[i] = new Node();
        }
        for(int i = 0; i < m; i++){
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
        }

        int len = 0;
        for(int i = 1; i <= n; i++){
            dfs(nodes[i]);
            len = Math.max(len, nodes[i].length);
        }

        out.println(len);
    }

    public void dfs(Node root){
        if(root.length != -1){
            return;
        }
        root.length = 0;
        for(Node node : root.next){
            dfs(node);
            root.length = Math.max(node.length + 1, root.length);
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int length = -1;
}
