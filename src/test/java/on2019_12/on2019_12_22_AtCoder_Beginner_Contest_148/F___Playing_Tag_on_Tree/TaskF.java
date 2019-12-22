package on2019_12.on2019_12_22_AtCoder_Beginner_Contest_148.F___Playing_Tag_on_Tree;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }
        Node u = nodes[in.readInt()];
        Node v = nodes[in.readInt()];
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        dfsForDepth(v, null);
        int ans = findSolution(u, 0);
        out.println(ans);
    }

    public int findSolution(Node root, int time) {
        if (root.p.depth > time + 1) {
            return findSolution(root.p, time + 1);
        }
        int ans = root.depethest - root.depth;
        if (root.p.depth > time) {
            ans++;
        }
        return ans + time;
    }

    public void dfsForDepth(Node root, Node p) {
        root.depth = p == null ? 0 : p.depth + 1;
        root.p = p;
        root.depethest = root.depth;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfsForDepth(node, root);
            root.depethest = Math.max(root.depethest, node.depethest);
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int depth;
    Node p;
    int depethest;
}
