package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.datastructure.DSU;
import template.rand.RandomWrapper;

public class FSumOfAbsTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
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

    RandomWrapper random = new RandomWrapper(new Random(1));

    public Test create(int testNum) {
        int n = random.nextInt(1, 15);
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, 1000000);
            b[i] = random.nextInt(-1000000, 1000000);
        }
        int m = random.nextInt(1, n * n);
        int[][] edges = new int[m][2];
        for (int i = 0; i < m; i++) {
            edges[i][0] = random.nextInt(0, n - 1);
            edges[i][1] = random.nextInt(0, n - 1);
        }

        long ans = solve(n, a, b, edges);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        printLine(in, a);
        printLine(in, b);
        for (int[] e : edges) {
            printLine(in, e[0] + 1, e[1] + 1);
        }
        return new Test(in.toString(), "" + ans);
    }

    public long solve(int n, int[] a, int[] b, int[][] edges) {
        long ans = 0;
        for (int i = 0; i < 1 << n; i++) {
            long cost = 0;
            DSUExt ext = new DSUExt(n);
            ext.init();
            for (int j = 0; j < n; j++) {
                ext.weight[j] = b[j];
            }
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 0) {
                    cost += a[j];
                }
            }
            for (int[] e : edges) {
                int u = e[0];
                int v = e[1];
                if (Bits.get(i, u) == 0 || Bits.get(i, v) == 0) {
                    continue;
                }
                ext.merge(u, v);
            }
            long sum = 0;
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 1 && ext.find(j) == j) {
                    sum += Math.abs(ext.weight[j]);
                }
            }
            long local = sum - cost;
            ans = Math.max(ans, local);
        }
        return ans;
    }


    public static class DSUExt extends DSU {
        int[] weight;

        public DSUExt(int n) {
            super(n);
            weight = new int[n];
        }

        @Override
        protected void preMerge(int a, int b) {
            super.preMerge(a, b);
            weight[a] += weight[b];
        }
    }
}
