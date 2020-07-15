package on2020_07.on2020_07_14_TopCoder_SRM__744.CoverTreePaths;



import template.datastructure.LeftistTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class CoverTreePaths {
    public long minimumCost(int n, int[] p, int[] d, int[] c, int[] params) {
        int t = d.length;
        p = Arrays.copyOf(p, n - 1);
        d = Arrays.copyOf(d, n);
        c = Arrays.copyOf(c, n);
        for (int i = t - 1; i <= n - 2; i++) {
            p[i] = (int) (((long) params[0] * p[i - 1] + params[1]) % (i + 1));
        }
        for (int i = t; i <= n - 1; i++) {
            d[i] = (int) (1 + (((long) params[2] * d[i - 1] + params[3]) % params[4]));
        }

        for (int i = t; i <= n - 1; i++) {
            c[i] = (int) (1 + (((long) params[5] * c[i - 1] + params[6]) % params[7]));
        }

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].d = d[i];
            nodes[i].c = c[i];
        }
        for (int i = 1; i < n; i++) {
            Node fa = nodes[p[i - 1]];
            fa.adj.add(nodes[i]);
        }

        dfs(nodes[0], 0);
        LeftistTree<Require> heap = dp(nodes[0]);

        long ans = 0;
        while(!heap.isEmpty()){
            ans += heap.peek().k * heap.peek().price;
            heap = LeftistTree.pop(heap, Require.sortByPrice);
        }
        return ans;
    }

    public void dfs(Node root, long ps) {
        if (root.d > ps) {
            root.req = new Require(root.c, root.d - ps);
            ps = root.d;
        } else {
            root.req = new Require(root.c, 0);
        }
        for (Node node : root.adj) {
            dfs(node, ps);
        }
    }

    public LeftistTree<Require> dp(Node root) {
        PriorityQueue<LeftistTree<Require>> pq = new PriorityQueue<>(Math.max(1, root.adj.size()), (a, b) -> Long.compare(a.peek().k, b.peek().k));
        long subtract = 0;
        for (Node node : root.adj) {
            LeftistTree<Require> req = dp(node);
            if (!req.isEmpty()) {
                pq.add(dp(node));
            }
        }
        long sum = 0;
        for (LeftistTree<Require> tree : pq) {
            sum += tree.peek().price;
        }
        while (sum > root.c) {
            LeftistTree<Require> head = pq.remove();
            sum -= head.peek().price;
            if (head.peek().k > subtract) {
                root.req.k += head.peek().k - subtract;
                subtract += head.peek().k - subtract;
            }
            head = LeftistTree.pop(head, Require.sortByPrice);
            if (!head.isEmpty()) {
                head.peek().k += subtract;
                sum += head.peek().price;
                pq.add(head);
            }
        }

        LeftistTree<Require> heap = LeftistTree.NIL;
        for (LeftistTree<Require> tree : pq) {
            tree.peek().k -= subtract;
            if (tree.peek().k == 0) {
                tree = LeftistTree.pop(tree, Require.sortByPrice);
            }
            if (!tree.isEmpty()) {
                heap = LeftistTree.merge(heap, tree, Require.sortByPrice);
            }
        }
        if (root.req.k > 0) {
            LeftistTree<Require> tree = new LeftistTree<>(root.req);
            heap = LeftistTree.merge(heap, tree, Require.sortByPrice);
        }
        return heap;
    }
}

class Require {
    long price;
    long k;

    public Require(long price, long k) {
        this.price = price;
        this.k = k;
    }

    static Comparator<Require> sortByPrice = (a, b) -> -Long.compare(a.price, b.price);
}

class Node {
    long c;
    long d;
    List<Node> adj = new ArrayList<>();
    Require req;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}

