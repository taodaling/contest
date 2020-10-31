package on2020_10.on2020_10_29_CSES___CSES_Problem_Set.Planets_and_Kingdoms;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PlanetsAndKingdoms {
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

        for (Node node : nodes) {
            tarjan(node);
        }

        int kingdom = 0;
        for(int i = 0; i < n; i++){
            if(nodes[i].set == nodes[i]){
                nodes[i].kingdom = ++kingdom;
            }
        }
        out.println(kingdom);
        for(int i = 0; i < n; i++){
            out.append(nodes[i].set.kingdom).append(' ');
        }
    }

    int order = 0;
    Deque<Node> dq = new ArrayDeque<>();

    public void tarjan(Node root) {
        if (root.dfn != 0) {
            return;
        }
        root.dfn = root.low = ++order;
        root.instk = true;
        dq.addLast(root);
        for (Node node : root.adj) {
            tarjan(node);
            if (node.instk) {
                root.low = Math.min(root.low, node.low);
            }
        }
        if (root.low == root.dfn) {
            while (true) {
                Node tail = dq.removeLast();
                tail.instk = false;
                tail.set = root;
                if (tail == root) {
                    break;
                }
            }
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int dfn;
    int low;
    boolean instk;
    int id;
    Node set;
    int kingdom;
}
