package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ReachabilityQueries {
    int n;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int m = in.ri();
        int q = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
        }
        for (Node node : nodes) {
            tarjan(node);
        }
        for (int i = 0; i < q; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            out.println(solve(nodes[a]).get(b) ? "YES" : "NO");
        }
    }

    int order;
    Deque<Node> dq = new ArrayDeque<>((int) 1e5);

    public void tarjan(Node root) {
        if (root.dfn > 0) {
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
                root.scc.add(tail);
                if (tail == root) {
                    break;
                }
            }
        }
    }

    public BitSet solve(Node root) {
        root = root.set;
        if (root.reach == null) {
            root.reach = new BitSet(n);
            for (Node member : root.scc) {
                root.reach.set(member.id);
                for (Node node : member.adj) {
                    if (node.set == root) {
                        continue;
                    }
                    root.reach.or(solve(node));
                }
            }
        }
        return root.reach;
    }
}

class Node {
    int id;
    List<Node> adj = new ArrayList<>();
    BitSet reach;
    List<Node> scc = new ArrayList<>();
    Node set;
    int dfn;
    int low;
    boolean instk;
}