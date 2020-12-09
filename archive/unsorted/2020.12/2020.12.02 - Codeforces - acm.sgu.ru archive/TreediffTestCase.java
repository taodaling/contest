package contest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.graph.DirectedEdge;
import template.graph.Graph;
import template.rand.RandomWrapper;

public class TreediffTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, int... vals) {
        for (int val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long... vals) {
        for (long val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(2, 100);
        int[] p = new int[n - 1];
        int possible = 1;
        for (int i = 2; i <= n; i++) {
            p[i - 2] = random.nextInt(1, possible);
            if (p[i - 2] == possible) {
                possible++;
            }
        }
        int m = n - (possible - 1);
        int[] value = new int[m];
        for (int i = 0; i < m; i++) {
            value[i] = random.nextInt(-1000, 1000);
        }

        int[] ans = solve(n, m, p, value);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        printLine(in, p);
        printLine(in, value);
        StringBuilder out = new StringBuilder();
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    List<DirectedEdge>[] g;
    int[] ans;

    public List<Integer> dfs(int root) {
        if (g[root].isEmpty()) {
            return Arrays.asList(ans[root]);
        }
        List<Integer> leaves = new ArrayList<>();
        for (DirectedEdge e : g[root]) {
            leaves.addAll(dfs(e.to));
        }
        leaves.sort(Comparator.naturalOrder());
        ans[root] = Integer.MAX_VALUE;
        for (int i = 1; i < leaves.size(); i++) {
            ans[root] = Math.min(ans[root], leaves.get(i) - leaves.get(i - 1));
        }
        return leaves;
    }

    public int[] solve(int n, int m, int[] p, int[] value) {
        g = Graph.createGraph(n);
        for (int i = 1; i < n; i++) {
            int fa = p[i - 1] - 1;
            Graph.addEdge(g, fa, i);
        }
        ans = new int[n];
        for (int i = 0; i < m; i++) {
            ans[i + n - m] = value[i];
        }
        dfs(0);
        return Arrays.copyOf(ans, n - m);
    }
}


