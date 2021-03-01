package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class AFoxAndNames {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        String[] names = new String[n];
        for (int i = 0; i < n; i++) {
            names[i] = in.rs();
        }
        int charset = 'z' - 'a' + 1;
        Node[] nodes = new Node[charset];
        for (int i = 0; i < charset; i++) {
            nodes[i] = new Node();
            nodes[i].v = (char) ('a' + i);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                String a = names[j];
                String b = names[i];
                if (a.startsWith(b) && !a.equals(b)) {
                    out.println("Impossible");
                    return;
                }
                if (b.startsWith(a)) {
                    continue;
                }
                int k = 0;
                while (a.charAt(k) == b.charAt(k)) {
                    k++;
                }
                nodes[b.charAt(k) - 'a'].adj.add(nodes[a.charAt(k) - 'a']);
            }
        }

        for (Node node : nodes) {
            if (dfs(node)) {
                out.println("Impossible");
                return;
            }
        }
        Arrays.sort(nodes, Comparator.comparingInt(x -> x.dfn));
        for (Node node : nodes) {
            out.append(node.v);
        }
    }

    int order = 0;

    public boolean dfs(Node root) {
        if (root.visited) {
            return root.instk;
        }
        root.visited = true;
        root.instk = true;
        for (Node node : root.adj) {
            if (dfs(node)) {
                return true;
            }
        }
        root.dfn = order++;
        root.instk = false;
        return false;
    }
}

class Node {
    char v;
    boolean instk;
    boolean visited;
    int dfn = -1;
    List<Node> adj = new ArrayList<>();
}
