package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class EFibTree {
    int[] fib = new int[30];
    int[] inv = new int[200000 + 10];

    {
        fib[0] = fib[1] = 1;
        inv[1] = 1;
        for (int i = 2; i < 30; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
            if (fib[i] < inv.length) {
                inv[fib[i]] = i;
            }
        }
    }

    void dfsForSize(Node root, Node p) {
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForSize(node, root);
            root.size += node.size;
        }
    }

    Pair<Node, Node> findPair(Node root, Node p, int target, int total) {
        if (root.size == target || total - root.size == target) {
            return new Pair<>(root, p);
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            Pair<Node, Node> ans = findPair(node, root, target, total);
            if (ans != null) {
                return ans;
            }
        }
        return null;
    }

    boolean check(Node root) {
        dfsForSize(root, null);
        if (root.size <= 2) {
            return true;
        }
        int x = inv[root.size];
        assert x != -1;
        Pair<Node, Node> pair = findPair(root, null, fib[x - 1], root.size);
        if(pair == null){
            return false;
        }
        pair.a.adj.remove(pair.b);
        pair.b.adj.remove(pair.a);
        return check(pair.a) && check(pair.b);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int index = -1;
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < 30; i++) {
            if (fib[i] == n) {
                index = i;
            }
        }
        if (index == -1) {
            out.println("NO");
            return;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        out.println(check(nodes[0]) ? "YES" : "NO");
    }
}

class Node {
    int id;
    List<Node> adj = new ArrayList<>();
    int size;
}
