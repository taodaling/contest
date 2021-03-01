package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.geometry.geo2.IntegerTriangulation;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.rand.RandomWrapper;

public class P6329TestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 10);
        int m = random.nextInt(1, 10);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, n);
        }
        int[][] e = new int[n - 1][2];
        for (int i = 1; i < n; i++) {
            e[i - 1][0] = random.nextInt(1, i);
            e[i - 1][1] = i + 1;
        }
        int[][] op = new int[m][3];
        for (int i = 0; i < m; i++) {
            op[i][0] = random.nextInt(0, 1);
            op[i][1] = random.nextInt(1, n);
            op[i][2] = random.nextInt(1, n);
        }
        int[] ans = solve(n, e, a, op);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, m);
        printLine(in, a);
        for (int[] ab : e) {
            printLine(in, ab);
        }
        for (int[] q : op) {
            printLine(in, q);
        }
        for (int x : ans) {
            printLine(out, x);
        }
        return new Test(in.toString(), out.toString());
    }

    int[] a;
    List<UndirectedEdge>[] g;

    int query(int root, int p, int d, int k) {
        int ans = 0;
        if (d <= k) {
            ans += a[root];
        }
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            ans += query(e.to, root, d + 1, k);
        }
        return ans;
    }

    int[] solve(int n, int[][] e, int[] a, int[][] op) {
        List<Integer> ans = new ArrayList<>();
        g = Graph.createGraph(n);
        this.a = a.clone();
        for (int[] ab : e) {
            Graph.addUndirectedEdge(g, ab[0] - 1, ab[1] - 1);
        }
        for (int[] q : op) {
            if (q[0] == 0) {
                ans.add(query(q[1] - 1, -1, 0, q[2]));
            } else {
                this.a[q[1] - 1] = q[2];
            }
        }
        return ans.stream().mapToInt(Integer::intValue).toArray();
    }
}
