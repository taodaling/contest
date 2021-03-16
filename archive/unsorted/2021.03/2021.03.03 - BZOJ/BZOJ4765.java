package contest;

import template.algo.BlockSplit;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.LongBIT;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BZOJ4765 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        inv = new Node[n + 1];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].val = in.rl();
        }
        Node root = null;
        for (int i = 0; i < n; i++) {
            int aId = in.ri() - 1;
            int bId = in.ri() - 1;
            if (aId < 0) {
                root = nodes[bId];
                continue;
            }
            Node a = nodes[aId];
            Node b = nodes[bId];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfs(root, null);
        int B = (int) Math.ceil(Math.sqrt(n));
        B = Math.max(B, 1);
        LongBIT bit = new LongBIT(n);
        bit.clear(i -> inv[i].val, n);
        BlockSplit<Sum, Update> bs = new BlockSplit<>(n, B, (l, r) -> {
            return new BlockImpl(nodes, l, r, bit);
        });
        for (int i = 0; i < m; i++) {
            int op = in.ri();
            if (op == 1) {
                int u = in.ri() - 1;
                long v = in.rl();
                Update upd = new Update();
                upd.mod = v - nodes[u].val;
                nodes[u].val = v;
                upd.node = nodes[u];
                bs.update(0, n - 1, upd);
                bit.update(upd.node.open, upd.mod);
            } else {
                int l = in.ri() - 1;
                int r = in.ri() - 1;
                Sum sum = new Sum();
                bs.query(l, r, sum);
                BigInteger ans = BigInteger.valueOf(sum.high).multiply(BigInteger.valueOf(Sum.threshold))
                        .add(BigInteger.valueOf(sum.low));
                out.println(ans);
            }
        }
    }

    int order = 0;
    Node[] inv;

    public void dfs(Node root, Node p) {
        root.open = ++order;
        inv[root.open] = root;
        root.sum = root.val;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            root.sum += node.sum;
        }
        root.close = order;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int open;
    int close;
    int id;
    long val;
    long sum;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}

class Sum {
    long high;
    long low;
    static long threshold = (long) 1e18;

    public void add(long x) {
        low += x;
        while (low >= threshold) {
            low -= threshold;
            high++;
        }
    }
}

class Update {
    Node node;
    long mod;
}

class BlockImpl implements BlockSplit.Block<Sum, Update> {
    int[] depth;
    long sum;
    LongBIT global;
    Node[] nodes;
    int offset;

    public BlockImpl(Node[] nodes, int l, int r, LongBIT bit) {
        this.global = bit;
        this.nodes = nodes;
        this.offset = l;
        depth = new int[nodes.length + 2];
        for (int i = l; i <= r; i++) {
            depth[nodes[i].open]++;
            depth[nodes[i].close + 1]--;
            sum += nodes[i].sum;
        }
        for (int i = 1; i < depth.length; i++) {
            depth[i] += depth[i - 1];
        }
    }

    @Override
    public void fullyUpdate(Update upd) {
        int id = upd.node.open;
        sum += upd.mod * depth[id];
    }

    @Override
    public void partialUpdate(int index, Update upd) {
        //impossible
        assert false;
    }

    @Override
    public void fullyQuery(Sum sum) {
        sum.add(this.sum);
    }

    @Override
    public void partialQuery(int index, Sum sum) {
        Node node = nodes[offset + index];
        sum.add(global.query(node.open, node.close));
    }
}

