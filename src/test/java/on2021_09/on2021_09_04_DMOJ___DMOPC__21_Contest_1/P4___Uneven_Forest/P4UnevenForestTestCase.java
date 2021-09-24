package on2021_09.on2021_09_04_DMOJ___DMOPC__21_Contest_1.P4___Uneven_Forest;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.graph.Graph;
import template.primitve.generated.graph.IntegerWeightGraph;
import template.primitve.generated.graph.IntegerWeightUndirectedEdge;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class P4UnevenForestTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
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

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 3);
        int M = 10;
        int k = random.nextInt(1, M);
        List<int[]> edges = new ArrayList<>(n);
        for (int i = 1; i < n; i++) {
            if(random.nextInt(1, 5) <= 2){
                continue;
            }
            int a = random.nextInt(1, i);
            int b = i + 1;
            int len = random.nextInt(1, M);
            int cost = random.nextInt(1, M);

            edges.add(new int[]{a, b, len, cost});
        }
        int ans = solve(n, k, edges.toArray(new int[0][]));
        StringBuilder in = new StringBuilder();
        printLine(in, n, edges.size(), k);
        for(int[] e : edges){
            printLine(in, e);
        }
        return new Test(in.toString(), "" + ans);
    }

    List<IntegerWeightUndirectedEdge>[] g;
    int[] depth;

    public int dfs(int root, int p, int d) {
        depth[root] = d;
        int best = root;
        for (IntegerWeightUndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            int res = dfs(e.to, root, d + e.weight);
            if(depth[res] > depth[best]){
                best = res;
            }
        }
        return best;
    }

    public int diameter() {
        Arrays.fill(depth, -1);
        int d = 0;
        for(int i = 0; i < depth.length; i++) {
            if(depth[i] != -1){
                continue;
            }
            int end = dfs(i, -1, 0);
            int end2 = dfs(end, -1, 0);
            d = Math.max(d, depth[end2]);
        }
        return d;
    }

    public int solve(int n, int k, int[][] edges) {
        if(edges.length == 0){
            return 0;
        }
        int m = edges.length;
        depth = new int[n];
        int best = (int)1e9;
        for (int i = 0; i < 1 << m; i++) {
            g = Graph.createGraph(n);
            long cost = 0;
            for (int j = 0; j < m; j++) {
                if (Bits.get(i, j) == 0) {
                    cost += edges[j][3];
                } else {
                    IntegerWeightGraph.addUndirectedEdge(g, edges[j][0] - 1, edges[j][1] - 1, edges[j][2]);
                }
            }
            int d = diameter();
            if(cost <= k){
                best = Math.min(best, d);
            }
        }
        return best;
    }
}
