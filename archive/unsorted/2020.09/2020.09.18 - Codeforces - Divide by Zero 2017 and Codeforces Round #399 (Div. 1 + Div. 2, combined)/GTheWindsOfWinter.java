package contest;

import sun.awt.IconInfo;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.*;

public class GTheWindsOfWinter {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n; i++) {
            int a = in.readInt();
            int b = in.readInt();
            if (a == 0) {
                continue;
            }
            Node u = nodes[a - 1];
            Node v = nodes[b - 1];
            u.adj.add(v);
            v.adj.add(u);
        }

        if (n == 1) {
            out.println(0);
            return;
        }
        if (n == 2) {
            out.println("1 1");
            return;
        }


        dfsForSize(nodes[0], null);
        Node root = findCentroid(nodes[0], null);
        dfsForSize(root, null);
        root.adj.sort((a, b) -> Integer.compare(a.size, b.size));
        if (root.adj.get(root.adj.size() - 1).size == root.adj.get(root.adj.size() - 2).size) {
            root.ans = root.adj.get(root.adj.size() - 1).size;
        } else {
            collectSize(root.adj.get(root.adj.size() - 1), null, 1);
            getAns(root, null);
            collectSize(root.adj.get(root.adj.size() - 1), null, -1);
        }

        dfsForAns(root, null, true);
        for (Node node : nodes) {
            out.println(node.ans);
        }
    }

    int n;

    public void dfsForAns(Node root, Node p, boolean skip) {
        if (!skip) {
            getAns(root, p);
        }
        root.adj.remove(p);
        if (root.adj.isEmpty()) {
            add(1, 1);
            return;
        }
        root.adj.sort((a, b) -> Integer.compare(a.size, b.size));
        for (int i = 0; i < root.adj.size() - 1; i++) {
            Node node = root.adj.get(i);
            collectSize(node, root, 1);
        }
        Node heavy = root.adj.get(root.adj.size() - 1);
        add(n - heavy.size, 1);
        dfsForAns(heavy, root, false);
        add(n - heavy.size, -1);
        for (int i = 0; i < root.adj.size() - 1; i++) {
            Node node = root.adj.get(i);
            collectSize(node, root, -1);
            add(n - node.size, 1);
            dfsForAns(node, root, false);
            add(n - node.size, -1);
        }
        add(root.size, 1);
    }

    public int solve(int maxSize, int minSize) {
        int half = (maxSize - minSize) / 2;
        Integer floor = set.floorKey(half);
        Integer ceil = set.ceilingKey(half);

        int ans = maxSize;
        if (floor != null) {
            ans = Math.min(ans, Math.max(maxSize - floor, minSize + floor));
        }
        if (ceil != null) {
            ans = Math.min(ans, Math.max(maxSize - ceil, minSize + ceil));
        }
        return ans;
    }

    public void getAns(Node root, Node p) {
        int[] size = new int[root.adj.size()];
        for (int i = 0; i < root.adj.size(); i++) {
            Node node = root.adj.get(i);
            if (node == p) {
                size[i] = n - root.size;
            } else {
                size[i] = node.size;
            }
        }
        Randomized.shuffle(size);
        Arrays.sort(size);
        int ans = solve(size[size.length - 1], size[0]);
        for (int i = 1; i < size.length - 1; i++) {
            ans = Math.max(ans, size[i]);
        }
        root.ans = ans;
    }

    TreeMap<Integer, Integer> set = new TreeMap<>();

    public void add(int x, int c) {
        int cur = set.getOrDefault(x, 0) + c;
        if (cur == 0) {
            set.remove(x);
        } else {
            set.put(x, cur);
        }
    }

    public void collectSize(Node root, Node p, int x) {
        add(root.size, x);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            collectSize(node, root, x);
        }
    }

    public void dfsForSize(Node root, Node p) {
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForSize(node, root);
            root.size += node.size;
        }
    }

    public Node findCentroid(Node root, Node p) {
        int subtree = n - root.size;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            subtree = Math.max(subtree, node.size);
            Node ans = findCentroid(node, root);
            if (ans != null) {
                return ans;
            }
        }
        if (subtree * 2 <= n) {
            return root;
        }
        return null;
    }
}

class Node {
    int ans;
    int size;
    int id;
    List<Node> adj = new ArrayList<>();

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}