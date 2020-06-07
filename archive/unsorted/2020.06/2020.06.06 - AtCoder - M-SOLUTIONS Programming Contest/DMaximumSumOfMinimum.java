package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

import java.util.ArrayList;
import java.util.List;

public class DMaximumSumOfMinimum {
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

        list = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            list.add(in.readInt());
        }
        list.sort();
        int ans = dfs(nodes[0], null);
        out.println(ans);
        for(Node node : nodes){
            out.append(node.val).append(' ');
        }
    }

    public int dfs(Node root, Node p) {
        int ans = 0;
        root.val = list.pop();
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            ans += dfs(node, root) + node.val;
        }
        return ans;
    }

    IntegerList list;
}

class Node {
    List<Node> adj = new ArrayList<>();
    int val;
}
