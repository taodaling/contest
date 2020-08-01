package contest;

import template.datastructure.LeftistTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BAlyonaAndATree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].a = in.readInt();
        }
        for (int i = 1; i < n; i++) {
            Node p = nodes[in.readInt() - 1];
            Edge e = new Edge();
            e.a = p;
            e.b = nodes[i];
            e.w = in.readInt();
            e.a.adj.add(e);
            e.b.adj.add(e);
        }

        dfs(nodes[0], null, 0);

        for (Node node : nodes) {
            out.println(node.cnt);
        }
    }



    public PQ dfs(Node root, Edge p, long depth) {
        PQ pq = new PQ();
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            pq.merge(dfs(node, e, depth + e.w));
        }
        while (!pq.isEmpty() && pq.peek() > depth) {
            pq.pop();
        }

        root.cnt = pq.size;
        pq.add(depth - root.a);
        return pq;
    }

}

class PQ {
    LeftistTree<Long> tree = LeftistTree.NIL;
    int size = 0;

    public void merge(PQ pq) {
        tree = LeftistTree.merge(tree, pq.tree, Comparator.reverseOrder());
        size += pq.size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Long peek() {
        return tree.peek();
    }

    public void add(long x) {
        tree = LeftistTree.merge(tree, new LeftistTree<>(x), Comparator.reverseOrder());
        size++;
    }

    public void pop() {
        tree = LeftistTree.pop(tree, Comparator.reverseOrder());
        size--;
    }
}

class Edge {
    Node a;
    Node b;
    int w;

    public Node other(Node x) {
        return x == a ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int a;
    int cnt;
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
