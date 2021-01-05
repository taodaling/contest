package contest;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.graph.DirectedEdge;
import template.graph.Graph;
import template.rand.RandomWrapper;

public class P5180TestCase {
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(2, 50);
        int m = random.nextInt(n, 100);
        int[][] edges = new int[m][2];
        for (int i = 0; i < m; i++) {
            int a = random.nextInt(1, n);
            int b = random.nextInt(1, n);
            if (a == b) {
                i--;
                continue;
            }
            edges[i][0] = a;
            edges[i][1] = b;
        }

        String ans = solve(n, edges);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for(int i = 0; i < m; i++){
            printLine(in, edges[i][0], edges[i][1]);
        }
        return new Test(in.toString(), ans);
    }

    public int dfs(List<DirectedEdge>[] g, boolean[] visited, int root, int deleted) {
        if (visited[root] || root == deleted) {
            return 0;
        }
        visited[root] = true;
        int ans = 1;
        for (DirectedEdge e : g[root]) {
            ans += dfs(g, visited, e.to, deleted);
        }
        return ans;
    }

    public String solve(int n, int[][] edges) {
        List<DirectedEdge>[] g = Graph.createDirectedGraph(n);
        for (int[] e : edges) {
            Graph.addEdge(g, e[0] - 1, e[1] - 1);
        }
        StringBuilder ans = new StringBuilder();
        int total = dfs(g, new boolean[n], 0, -1);
        for(int i = 0; i < n; i++){
            int count = dfs(g, new boolean[n], 0, i);
            ans.append(total - count).append(' ');
        }
        return ans.toString();
    }
}
