package contest;

import java.math.BigInteger;
import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.math.BigCombination;
import template.rand.RandomWrapper;
import template.utils.Pair;

public class MaximumTopologicalSortTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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
        int k = 1;//random.nextInt(1, 2);
        k = Math.min(k, n);
        int[][] edges = new int[n - 1][];
        for (int i = 0; i < n - 1; i++) {
            edges[i] = new int[]{
                    random.nextInt(1, i + 1),
                    i + 2
            };
        }

        Pair<Integer, Integer> ans = solve(n, edges, k);
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, n, k);
        for (int[] e : edges) {
            printLine(in, e);
        }
        StringBuilder out = new StringBuilder();
        printLine(out, ans.a, ans.b);
        return new Test(in.toString(), out.toString());
    }

    List<UndirectedEdge>[] g;
    BigInteger[] way;
    int[] size;

    void dfs(int root, int p) {
        way[root] = BigInteger.ONE;
        size[root] = 0;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root);
            size[root] += size[e.to];
            way[root] = way[root].multiply(way[e.to])
                    .multiply(BigCombination.combination(size[root], size[e.to]));
        }
        size[root]++;
    }

    public Pair<Integer, Integer> solve(int n, int[][] edges, int k) {
        g = Graph.createGraph(n);
        size = new int[n];
        for (int[] e : edges) {
            Graph.addUndirectedEdge(g, e[0] - 1, e[1] - 1);
        }
        way = new BigInteger[n];
        List<Pair<BigInteger, Integer>> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            dfs(i, -1);
            list.add(new Pair<>(way[i], i));
        }
        list.sort(Comparator.<Pair<BigInteger, Integer>, BigInteger>comparing(x -> x.a).thenComparing(x -> x.b).reversed());
        Pair<BigInteger, Integer> ans = list.get(k - 1);
        return new Pair<>(ans.b + 1, ans.a.mod(BigInteger.valueOf((int) 1e9 + 7)).intValueExact());
    }
}
