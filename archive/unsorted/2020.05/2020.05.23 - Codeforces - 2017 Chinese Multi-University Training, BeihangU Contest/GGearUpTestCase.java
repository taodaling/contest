package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Log2;
import template.datastructure.DSU;
import template.primitve.generated.datastructure.DoubleList;
import template.rand.RandomWrapper;

public class GGearUpTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object... vals) {
        for (Object val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 5);
        int m = random.nextInt(0, n - 1);
        int q = random.nextInt(1, 50);
        int[] init = new int[n];
        for (int i = 0; i < n; i++) {
            init[i] = 1 << random.nextInt(0, 30);
        }
        DSU dsu = new DSU(n);
        int[][] edges = new int[m][3];
        for (int i = 0; i < m; i++) {
            int a = random.nextInt(0, n - 1);
            int b = random.nextInt(0, n - 1);
            int t = random.nextInt(1, 2);
            if (dsu.find(a) == dsu.find(b)) {
                i--;
                continue;
            }
            dsu.merge(a, b);
            edges[i][0] = t;
            edges[i][1] = a;
            edges[i][2] = b;
        }

        int[][] qs = new int[q][3];
        for (int i = 0; i < q; i++) {
            qs[i][0] = random.nextInt(1, 2);
            qs[i][1] = random.nextInt(0, n - 1);
            qs[i][2] = 1 << random.nextInt(0, 30);
        }

        double[] ans = solve(n, m, q, init, edges, qs);

        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();

        printLine(in, n, m, q);
        for (int i = 0; i < n; i++) {
            in.append(init[i]).append(' ');
        }
        in.append('\n');
        for (int i = 0; i < m; i++) {
            printLine(in, edges[i][0], edges[i][1] + 1, edges[i][2] + 1);
        }
        in.append('\n');
        for (int i = 0; i < q; i++) {
            printLine(in, qs[i][0], qs[i][1] + 1, qs[i][2]);
        }

        printLine(out, "Case #1:");
        for (int i = 0; i < ans.length; i++) {
            out.append(String.format("%.3f", ans[i])).append('\n');
        }

        return new Test(in.toString(), out.toString());
    }

    public double[] solve(int n, int m, int q, int[] init, int[][] edges, int[][] qs) {
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].r = Log2.floorLog(init[i]);
        }
        for (int[] e : edges) {
            Edge edge = new Edge();
            edge.type = e[0];
            edge.a = nodes[e[1]];
            edge.b = nodes[e[2]];
            edge.a.adj.add(edge);
            edge.b.adj.add(edge);
        }

        DoubleList list = new DoubleList();
        for (int[] query : qs) {
            if (query[0] == 1) {
                nodes[query[1]].r = Log2.floorLog(query[2]);
            } else {
                Node root = nodes[query[1]];
                int ans = dfs(root, null, Log2.floorLog(query[2]));
                list.add(ans * Math.log(2));
            }
        }

        return list.toArray();
    }

    public int dfs(Node root, Node p, int v) {
        int ans = v;
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            int delta = e.type == 1 ? root.r - node.r : 0;
            ans = Math.max(ans, dfs(node, root, v + delta));
        }
        return ans;
    }

    static class Edge {
        Node a;
        Node b;
        int type;

        Node other(Node x) {
            return a == x ? b : a;
        }
    }

    static class Node {
        List<Edge> adj = new ArrayList<>();
        int r;
        int id;

        @Override
        public String toString() {
            return "" + id;
        }
    }
}
