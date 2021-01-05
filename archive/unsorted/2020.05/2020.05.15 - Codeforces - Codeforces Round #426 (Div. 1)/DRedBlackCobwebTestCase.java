package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.Modular;
import template.rand.RandomWrapper;

public class DRedBlackCobwebTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 4);
        int[][] edges = new int[n - 1][4];
        for(int i = 0; i < n - 1; i++){
            edges[i][0] = random.nextInt(0, i) + 1;
            edges[i][1] = i + 1 + 1;
            edges[i][2] = random.nextInt(1, 10);
            edges[i][3] = random.nextInt(0, 1);
        }

        int ans = solve(n, edges);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for(int[] e : edges){
            printLine(in, e[0], e[1], e[2], e[3]);
        }

        return new Test(in.toString(), "" + ans);
    }

    static class Edge {
        int w;
        int c0;
        int c1;
        Node a;
        Node b;

        Node other(Node x) {
            return a == x ? b : a;
        }
    }

    static class Node {
        List<Edge> adj = new ArrayList<>();
        int id;
    }

    int ans;
    Modular mod = new Modular(1e9 + 7);

    public void dfs(Node root, Node p, Node top, int prod, int a, int b) {
        if (root.id > top.id && a * 2 >= b && a <= b * 2) {
            ans = mod.mul(ans, prod);
        }
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            dfs(node, root, top, mod.mul(prod, e.w), a + e.c0, b + e.c1);
        }
    }

    public int solve(int n, int[][] edges) {
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int[] e : edges) {
            Edge edge = new Edge();
            Node a = nodes[e[0] - 1];
            Node b = nodes[e[1] - 1];
            edge.a = a;
            edge.b = b;
            edge.w = e[2];
            if (e[3] == 0) {
                edge.c0 = 1;
            } else {
                edge.c1 = 1;
            }

            a.adj.add(edge);
            b.adj.add(edge);
        }

        ans = 1;
        for(int i = 0; i < n; i++){
            dfs(nodes[i], null, nodes[i], 1, 0, 0);
        }

        return ans;
    }
}
