package on2020_05.on2020_05_14_Codeforces___Codeforces_Round__429__Div__1_.E__In_a_Trap0;




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

    int height = 7;
    BNode btree = BNode.build(height);
    int lowmask = (1 << k) - 1;

    public Node up(Node root, int dist) {
        if (root == null || dist >= (1 << k)) {
            return root;
        }
        btree.add(height, (dist ^ root.a) >> k, (dist ^ root.a) & lowmask);
        return up(root.parent, dist + 1);
    }


    public void prepare(Node root) {
        root.prev = up(root, 0);
        for (int i = 0; i < root.dp.length; i++) {
            root.dp[i] = btree.find(height, i, 0);
        }
        btree.clear(height);
    }
}

class BNode {
    BNode[] next = new BNode[2];
    int max = -1;

    public void clear(int bit) {
        max = -1;
        for (BNode node : next) {
            if (node == null) {
                continue;
            }
            node.clear(bit - 1);
        }
    }

    static BNode build(int bit) {
        if (bit < 0) {
            return new BNode();
        }
        BNode ans = new BNode();
        for(int i = 0; i < 2; i++){
            ans.next[i] = build(bit - 1);
        }
        return ans;
    }

    public void add(int bit, int val, int low) {
        max = Math.max(max, low);
        if (bit < 0) {
            return;
        }
        next[Bits.get(val, bit)].add(bit - 1, val, low);
    }

    public int find(int bit, int xor, int built) {
        if (max == -1) {
            return -1;
        }
        if (bit < 0) {
            return (built << 8) | max;
        }
        int val = Bits.get(xor, bit);
        int ans = next[val ^ 1].find(bit - 1, xor, built | 1 << bit);
        if (ans == -1) {
            ans = next[val].find(bit - 1, xor, built);
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

