package on2021_05.on2021_05_06_Codeforces___Codeforces_Round__190__Div__1_.C__Ciel_the_Commander;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class CCielTheCommander {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfs(nodes[0], null);
        for (Node node : nodes) {
            int rank = Log2.floorLog(Integer.lowestOneBit(node.v));
            char c = (char) ('Z' - rank);
            out.append(c).append(' ');
        }
    }

    int merge(int a, int b) {
        int and = a & b;
        int or = a | b;
        if (and != 0) {
            int highest = Integer.highestOneBit(and);
            or |= highest - 1;
        }
        return or;
    }

    void dfs(Node root, Node p) {
        int or = 0;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            or = merge(or, node.v);
        }
        root.v = or + 1;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int v;
}