package contest;

import template.FastInput;
import template.FastOutput;
import template.LeftSideTree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].price = in.readInt();
        }

        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        dfsForDp(nodes[1], null);
        out.println(nodes[1].dp);
        LeftSideTree<Node> pq = nodes[1].lst;
        while (!pq.isEmpty()) {
            pq.peek().find().selected = true;
            pq = LeftSideTree.pop(pq, Node.sortByPrice);
        }

        List<Integer> ans = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            if (nodes[i].find().selected) {
                ans.add(i);
            }
        }

        out.println(ans.size());
        for(Integer node : ans){
            out.append(node).append(' ');
        }
    }

    public void dfsForDp(Node root, Node father) {
        root.next.remove(father);
        if (root.next.isEmpty()) {
            root.dp = root.price;
            return;
        }
        LeftSideTree<Node> pq = LeftSideTree.NIL;
        for (Node node : root.next) {
            dfsForDp(node, root);
            root.dp += node.dp;
            pq = LeftSideTree.merge(pq, node.lst, Node.sortByPrice);
        }
        Node delegate = pq.peek();
        if (delegate.price == root.price) {
            Node.merge(delegate, root);
        }
        if (delegate.price > root.price) {
            pq = LeftSideTree.pop(pq, Node.sortByPrice);
            pq = LeftSideTree.merge(pq, root.lst, Node.sortByPrice);
            if (pq.peek().price == delegate.price) {
                Node.merge(delegate, pq.peek());
            }
            root.dp -= delegate.price;
            root.dp += root.price;
        }
        root.lst = pq;
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    long dp;
    long price;

    Node p = this;
    int rank;
    boolean selected;

    Node find() {
        return p == p.p ? p : (p = p.find());
    }

    static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank > b.rank) {
            b.p = a;
        } else {
            a.p = b;
        }
    }

    static Comparator<Node> sortByPrice = (a, b) -> -Long.compare(a.price, b.price);
    LeftSideTree<Node> lst = new LeftSideTree<>(this);
}
