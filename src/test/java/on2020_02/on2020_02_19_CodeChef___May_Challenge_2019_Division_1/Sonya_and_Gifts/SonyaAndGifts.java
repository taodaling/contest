package on2020_02.on2020_02_19_CodeChef___May_Challenge_2019_Division_1.Sonya_and_Gifts;



import graphs.lca.Lca;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class SonyaAndGifts {
    Node[] nodes;
    LcaOnTree lca;
    List<UndirectedEdge>[] g;
    Node[] compressed;
    int n;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int q = in.readInt();
        nodes = new Node[n];
        g = Graph.createUndirectedGraph(n);
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].a = in.readLong();
        }
        for (int i = 0; i < n; i++) {
            nodes[i].b = in.readLong();
        }
        for (int i = 1; i < n; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            Graph.addUndirectedEdge(g, u, v);
        }
        List<Node> seq = new ArrayList<>(n);
        dfs(0, -1);
        compress(0, -1, seq);
        lca = new LcaOnTree(g, 0);
        compressed = seq.toArray(new Node[0]);

        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            if (t == 1) {
                int a = in.readInt();
                int b = in.readInt();
                path(u, v, node -> {
                    node.a += a;
                    node.b += b;
                });
            } else {
                long z = in.readLong();
                MaxRecord record = new MaxRecord();
                record.z = z;
                path(u, v, record);
                out.println(record.max);
            }
        }
    }

    class MaxRecord implements Consumer<Node> {
        long max = (long) -2e18;
        long z;

        @Override
        public void accept(Node value) {
            max = Math.max(value.a * z + value.b, max);
        }
    }

    public void path(int u, int v, Consumer<Node> consumer) {
        int l = lca.lca(u, v);
        consumer.accept(nodes[l]);
        upToBottom(l, u, consumer);
        upToBottom(l, v, consumer);
    }

    public void upToBottom(int ancestor, int v, Consumer<Node> consumer) {
        int l = nodes[v].l;
        int r = nodes[v].r;
        for (int i = nodes[ancestor].l + 1; i <= l; i++) {
            if (compressed[i].r >= l) {
                consumer.accept(compressed[i]);
            } else {
                i = compressed[i].r;
            }
        }
    }

    public void dfs(int root, int p) {
        nodes[root].size = 1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root);
            nodes[root].size += nodes[e.to].size;
        }
        g[root].sort((a, b) -> -Integer.compare(nodes[a.to].size, nodes[b.to].size));
    }

    public void compress(int root, int p, List<Node> seq) {
        seq.add(nodes[root]);
        nodes[root].l = nodes[root].r = nodes[root].order = seq.size() - 1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            compress(e.to, root, seq);
            nodes[root].r = nodes[e.to].r;
        }
    }
}

class Node {
    long a;
    long b;

    int l;
    int r;
    int order;
    int size;
}
