package contest;

import java.util.Comparator;

import template.FastInput;
import template.FastOutput;
import template.LeftSideTree;
import template.SparseTable;

public class TaskD {

    Node[] nodes;
    SparseTable<Node>[] rmq;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        nodes = new Node[n];
        Node[][] splitNodes = new Node[2][n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].val = in.readInt();
            nodes[i].index = i;
            splitNodes[i % 2][i] = nodes[i];
        }

        rmq = new SparseTable[2];
        for (int i = 0; i < 2; i++) {
            rmq[i] = new SparseTable<>(splitNodes[i], n, (a, b) -> {
                if (a == null) {
                    return b;
                }
                if (b == null) {
                    return a;
                }
                return a.val < b.val ? a : b;
            });
        }

        LeftSideTree<Node> pq = dfs(0, n - 1);
        while (pq != LeftSideTree.NIL) {
            Node e = pq.peek();
            out.append(e.val).append(' ').append(e.carry.val).append(' ');
            pq = LeftSideTree.pop(pq, Node.sortByVal);
            pq = LeftSideTree.merge(pq, e.pq, Node.sortByVal);
        }
    }

    public LeftSideTree<Node> dfs(int l, int r) {
        if (l > r) {
            return LeftSideTree.NIL;
        }
        Node min = rmq[l % 2].query(l, r);
        min.carry = rmq[1 - l % 2].query(min.index + 1, r);

        min.pq = LeftSideTree.merge(min.pq, dfs(l, min.index - 1), Node.sortByVal);
        min.pq = LeftSideTree.merge(min.pq, dfs(min.index + 1, min.carry.index - 1), Node.sortByVal);
        min.pq = LeftSideTree.merge(min.pq, dfs(min.carry.index + 1, r), Node.sortByVal);

        return new LeftSideTree<>(min);
    }
}


class Node {
    Node carry;
    LeftSideTree<Node> pq = LeftSideTree.NIL;
    int val;
    int index;

    static Comparator<Node> sortByVal = (a, b) -> a.val - b.val;
}
