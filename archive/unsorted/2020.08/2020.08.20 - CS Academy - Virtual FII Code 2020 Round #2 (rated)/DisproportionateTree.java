package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class DisproportionateTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        k -= n - 1;
        if (k < 0) {
            out.println("NO");
            return;
        }
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

        boolean find = false;
        for (int i = 0; i < n && !find; i++) {
            int req = k + i;
            if (Integer.bitCount(req) <= i && req >= i) {
                find = true;
                int l = 0;
                for (int j = 0; j < i; j++) {
                    if (req > 0) {
                        nodes[j].delta = Log2.floorLog(Integer.lowestOneBit(req));
                        req -= Integer.lowestOneBit(req);
                    } else {
                        while (nodes[l].delta == 0) {
                            l++;
                        }
                        nodes[j].delta = nodes[l].delta = nodes[l].delta - 1;
                    }
                }
            }
        }

        if (!find) {
            out.println("NO");
            return;
        }
        out.println("YES");
        dfs(nodes[n - 1], null);
        for (int i = 0; i < n; i++) {
            out.append(nodes[i].val + 1).append(' ');
        }
    }

    public void dfs(Node root, Node p) {
        if (p == null) {
            root.val = 0;
        } else {
            root.val = p.val + root.delta;
        }
        for (Node node : root.adj) {
            if(node == p){
                continue;
            }
            dfs(node, root);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int delta;
    int val;
}
