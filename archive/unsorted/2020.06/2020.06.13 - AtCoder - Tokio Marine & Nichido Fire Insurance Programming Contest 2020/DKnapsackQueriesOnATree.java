package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBinaryFunction;
import template.utils.Debug;

import java.util.*;

public class DKnapsackQueriesOnATree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            Node p = nodes[i / 2];
            if (p != null) {
                p.adj.add(nodes[i]);
            }
            nodes[i].v = in.readInt();
            nodes[i].w = in.readInt();
        }
        int q = in.readInt();
        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            Node node = nodes[in.readInt()];
            qs[i] = new Query();
            qs[i].l = in.readInt();
            node.bind.add(qs[i]);
        }

        dfs(nodes[1], 0);
        for (Query query : qs) {
            out.println(query.ans);
        }

    }

    Debug debug = new Debug(true);

    Node[] head = new Node[20];
    Node[] tail = new Node[20];
    int threshold = 9;
    int maxL = (int) 1e5;
    int[] dp = new int[maxL + 1];



    private void dfsForSet(int i, int limit, int v, int w) {
        if (w > maxL) {
            return;
        }
        if (i > limit) {
            dp[w] = Math.max(dp[w], v);
            return;
        }
        dfsForSet(i + 1, limit, v, w);
        dfsForSet(i + 1, limit, v + head[i].v, w + head[i].w);
    }

    private void dfsForSolve(Node[] nodes, int i, int limit, int v, int w) {
        if (w > maxL) {
            return;
        }
        if (i > limit) {
            for (Query q : nodes[limit].bind) {
                if (q.l < w) {
                    continue;
                }
                q.ans = Math.max(q.ans, v + dp[q.l - w]);
            }
            return;
        }
        dfsForSolve(nodes, i + 1, limit, v, w);
        dfsForSolve(nodes, i + 1, limit, v + nodes[i].v, w + nodes[i].w);
    }

    public void dfsLow(Node root, int depth) {
        tail[depth] = root;

        if (!root.bind.isEmpty()) {
            dfsForSolve(tail, 0, depth, 0, 0);
        }

        for (Node node : root.adj) {
            dfsLow(node, depth + 1);
        }
    }

    public void dfs(Node root, int depth) {
        head[depth] = root;
        dfsForSolve(head, 0, depth, 0, 0);

        if (depth + 1 == threshold) {
            dfsForSet(0, depth, 0, 0);
            for (int i = 1; i <= maxL; i++) {
                dp[i] = Math.max(dp[i], dp[i - 1]);
            }
            for (Node node : root.adj) {
                dfsLow(node, 0);
            }
            Arrays.fill(dp, 0);
        } else {
            for (Node node : root.adj) {
                dfs(node, depth + 1);
            }
        }
    }
}

class IntegerGenericBIT {
    private int[] data;
    private int n;
    private IntegerBinaryFunction merger;
    private int unit;

    /**
     * 创建大小A[1...n]
     */
    public IntegerGenericBIT(int n, IntegerBinaryFunction merger, int unit) {
        this.n = n;
        data = new int[n + 1];
        this.merger = merger;
        this.unit = unit;
        clear();
    }

    /**
     * 查询A[1]+A[2]+...+A[i]
     */
    public int query(int i) {
        int sum = unit;
        for (; i > 0; i -= i & -i) {
            sum = merger.apply(sum, data[i]);
        }
        return sum;
    }

    /**
     * 将A[i]更新为A[i]+mod
     */
    public void update(int i, int mod) {
        if (i <= 0) {
            return;
        }
        for (; i <= n; i += i & -i) {
            data[i] = merger.apply(data[i], mod);
        }
    }

    /**
     * 将A[i]更新为A[i]+mod
     */
    public void revoke(int i) {
        if (i <= 0) {
            return;
        }
        for (; i <= n; i += i & -i) {
            data[i] = unit;
        }
    }

    /**
     * 将A全部清0
     */
    public void clear() {
        Arrays.fill(data, unit);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            builder.append(query(i)).append(',');
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}


class Query {
    int l;
    long ans;
}

class Node {
    List<Node> adj = new ArrayList<>();
    int w;
    int v;
    List<Query> bind = new ArrayList<>();
}
