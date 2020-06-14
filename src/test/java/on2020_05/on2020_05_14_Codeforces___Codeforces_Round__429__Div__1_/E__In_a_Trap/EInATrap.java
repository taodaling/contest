package on2020_05.on2020_05_14_Codeforces___Codeforces_Round__429__Div__1_.E__In_a_Trap;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class EInATrap {
    int k = 8;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].a = in.readInt();
        }

        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.next.add(a);
        }

        dfs(nodes[0], null, 0);
        for (int i = 0; i < n; i++) {
            prepare(nodes[i]);
        }

        for (int i = 0; i < q; i++) {
            Node u = nodes[in.readInt() - 1];
            Node v = nodes[in.readInt() - 1];
            int ans = solve(v, 0, 0, u.depth);
            out.println(ans);
        }
    }

    public int solve(Node root, int step, int dist, int targetHeight) {
        if (root == null || root.depth < targetHeight) {
            return 0;
        }

        int ans;
        if (root.depth - (1 << k) + 1 >= targetHeight) {
            ans = Math.max(root.dp[step], solve(root.prev, step + 1, dist + (1 << k), targetHeight));
        } else {
            ans = 0;
            while (root != null && root.depth >= targetHeight) {
                ans = Math.max(root.a ^ dist, ans);
                dist++;
                root = root.parent;
            }
        }

        return ans;
    }

    public void dfs(Node root, Node p, int depth) {
        root.depth = depth;
        root.parent = p;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfs(node, root, depth + 1);
        }
    }

    int height = 15;
    BNode btree = BNode.build(height);

    public void up(Node root, int dist, int x) {
        if (root == null || dist >= (1 << k)) {
            return;
        }
        btree.add(height, dist ^ root.a, x);
        up(root.parent, dist + 1, x);
    }

    public Node findPrev(Node root, int dist) {
        if (root == null || dist >= (1 << k)) {
            return root;
        }
        return findPrev(root.parent, dist + 1);
    }

    public void prepare(Node root) {
        root.prev = findPrev(root, 0);
        up(root, 0, 1);
        for (int i = 0; i < root.dp.length; i++) {
            root.dp[i] = btree.find(height, i << k, 0);
        }
        up(root, 0, -1);
    }
}

class BNode {
    BNode[] next = new BNode[2];
    int cnt;

    static BNode build(int bit) {
        if (bit < 0) {
            return new BNode();
        }
        BNode ans = new BNode();
        ans.next[0] = build(bit - 1);
        ans.next[1] = build(bit - 1);
        return ans;
    }

    public void add(int bit, int val, int x) {
        cnt += x;
        if (bit < 0) {
            return;
        }
        next[Bits.get(val, bit)].add(bit - 1, val, x);
    }

    public int find(int bit, int xor, int built) {
        if (cnt == 0) {
            return -1;
        }
        if (bit < 0) {
            return built;
        }
        int val = Bits.get(xor, bit);
        int ans = next[val ^ 1].find(bit - 1, xor, built | ((1 ^ val) << bit));
        if (ans == -1) {
            ans = next[val].find(bit - 1, xor, built | (val << bit));
        }
        return ans;
    }
}

class Node {
    int depth;
    List<Node> next = new ArrayList<>();
    int[] dp = new int[256];
    int id;
    int a;
    Node parent;
    Node prev;

    @Override
    public String toString() {
        return "" + id;
    }
}

