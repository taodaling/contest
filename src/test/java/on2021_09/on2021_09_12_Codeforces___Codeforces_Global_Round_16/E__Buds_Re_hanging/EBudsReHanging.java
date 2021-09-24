package on2021_09.on2021_09_12_Codeforces___Codeforces_Global_Round_16.E__Buds_Re_hanging;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class EBudsReHanging {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfs(nodes[0], null);
        for (int i = 0; i < n; i++) {
            nodes[i].nonLeaf = nodes[i].deg = nodes[i].adj.size();
            nodes[i].leaf = nodes[i].deg == 0;
        }
        Deque<Node> dq = new ArrayDeque<>(n);
        for (Node node : nodes) {
            if (node.leaf && node.parent != null) {
                node.parent.nonLeaf--;
                if (node.parent.nonLeaf == 0) {
                    node.parent.bud = true;
                    assert node.parent.check();
                    dq.addLast(node.parent);
                }
            }
        }
        while (!dq.isEmpty()) {
            Node head = dq.removeFirst();
            debug.debug("head", head);
            if (head.parent == null) {
                continue;
            }
            head.parent.nonLeaf--;
            head.parent.deg--;
            if (head.parent.deg == 0) {
                head.parent.leaf = true;
                assert head.parent.check();
                if (head.parent.parent != null) {
                    head.parent.parent.nonLeaf--;
                    if (head.parent.parent.nonLeaf == 0) {
                        head.parent.parent.bud = true;
                        assert head.parent.parent.check();
                        dq.addLast(head.parent.parent);
                    }
                }
            } else if (head.parent.nonLeaf == 0) {
                head.parent.bud = true;
                assert head.parent.check();
                dq.addLast(head.parent);
            }
        }

        int numBud = 0;
        int numLeaf = 0;
        for (Node node : nodes) {
            assert node.check();
            if (node.leaf) {
                numLeaf++;
            }
            if (node.bud) {
                numBud++;
            }
        }
        if(nodes[0].leaf){
            numLeaf--;
        }
        out.println(numLeaf - numBud + 1);
    }
    Debug debug = new Debug(false);

    public void dfs(Node root, Node p) {
        root.adj.remove(p);
        root.parent = p;
        for (Node node : root.adj) {
            dfs(node, root);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    Node parent;
    int deg;
    int nonLeaf;
    boolean leaf;
    boolean bud;
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }

    public boolean check() {
        return !(leaf && bud);
    }
}