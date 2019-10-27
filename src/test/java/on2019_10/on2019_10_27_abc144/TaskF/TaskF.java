package on2019_10.on2019_10_27_abc144.TaskF;



import template.FastInput;
import template.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i <= m; i++) {
            Node s = nodes[in.readInt()];
            Node t = nodes[in.readInt()];
            s.next.add(t);
        }
        nodes[n].escape = true;
        findExp(nodes[1]);
        for(int i = 1; i <= n; i++){
            nodes[i].mx = null;
            for(Node node : nodes[i].next){
                if(nodes[i].mx == null || nodes[i].mx.exp < node.exp){
                    nodes[i].mx = node;
                }
            }
        }

        double minE = nodes[1].exp;
        for(int i = 1; i <= n; i++){
            if(nodes[i].mx == null){
                continue;
            }
            nodes[i].next.remove(nodes[i].mx);
            for(int j = 1; j <= n; j++){
                nodes[j].visited = false;
            }
            findExp(nodes[1]);
            minE = Math.min(minE, nodes[1].exp);
            nodes[i].next.add(nodes[i].mx);
        }

        out.printf("%.15f", minE);
    }

    public void findExp(Node root) {
        if(root.visited){
            return;
        }
        root.visited = true;
        if (root.escape) {
            root.exp = 0;
            return;
        }
        if(root.next.isEmpty()){
            root.exp = 1e50;
            return;
        }
        root.exp = 0;
        for (Node node : root.next) {
            findExp(node);
            root.exp += (node.exp + 1) / root.next.size();
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    Node mx;
    int id;
    boolean escape;
    double exp;
    boolean visited;
}
