package contest;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.graph.Graph;
import template.primitve.generated.graph.IntegerFlow;
import template.primitve.generated.graph.IntegerFlowEdge;
import template.primitve.generated.graph.IntegerISAP;
import template.primitve.generated.graph.IntegerMaximumFlow;
import template.rand.RandomWrapper;

public class EGoodsTransportationTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
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

    RandomWrapper random = new RandomWrapper(1);

    public Test create(int testNum) {
        int n = random.nextInt(1, 3);
        int c = random.nextInt(1, 10);
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, 10);
            b[i] = random.nextInt(1, 10);
        }
        int ans = solve(a, b, c);
        StringBuilder in = new StringBuilder();
        printLine(in, n, c);
        printLine(in, a);
        printLine(in, b);
        return new Test(in.toString(), "" + ans);
    }

    public int solve(int[] a, int[] b, int c) {
        int n = a.length;
        List<IntegerFlowEdge>[] g = Graph.createGraph(n + 2);
        int src = n;
        int dst = n + 1;
        for (int i = 0; i < n; i++) {
            IntegerFlow.addFlowEdge(g, src, i, a[i]);
            IntegerFlow.addFlowEdge(g, i, dst, b[i]);
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                IntegerFlow.addFlowEdge(g, i, j, c);
            }
        }
        return new IntegerISAP().apply(g, src, dst, (int) 1e9);
    }
}
