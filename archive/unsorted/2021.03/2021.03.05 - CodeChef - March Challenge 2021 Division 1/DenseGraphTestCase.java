package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.graph.DirectedEdge;
import template.graph.Graph;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.rand.RandomWrapper;

public class DenseGraphTestCase {
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = (int) 30;
        int m = random.nextInt(1, 10);
        int[][] ranges = new int[m][];
        for (int i = 0; i < m; i++) {
            int l = random.nextInt(1, n);
            int r = random.nextInt(1, n);
            int L = random.nextInt(1, n);
            int R = random.nextInt(1, n);
            if (l > r) {
                l ^= r;
                r ^= l;
                l ^= r;
            }
            if(r - l >= 5){
                r = l + 5;
            }
            if (L > R) {
                L ^= R;
                R ^= L;
                L ^= R;
            }
            if(R - L >= 5){
                R = L + 5;
            }
            ranges[i] = new int[]{l, r, L, R};
        }
        int x = random.nextInt(1, n);
        int y = random.nextInt(1, n);

        int ans = solve(n, ranges, x, y);
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, n, m, x, y);
        for(int[] e : ranges){
            printLine(in, e);
        }
        return new Test(in.toString(), "" + ans);
    }

    public int solve(int n, int[][] edges, int x, int y) {
        List<DirectedEdge>[] g = Graph.createGraph(n);
        for (int[] e : edges) {
            int l = e[0] - 1;
            int r = e[1] - 1;
            int L = e[2] - 1;
            int R = e[3] - 1;
            for(int i = l; i <= r; i++){
                for(int j = L; j <= R; j++){
                    Graph.addEdge(g, i, j);
                }
            }
        }
        int[] dist = new int[n];
        int inf = (int)1e9;
        Graph.bfs(g, x - 1, dist, inf, new IntegerDequeImpl(n));
        return dist[y - 1] == inf ? -1 : dist[y - 1];
    }
}
